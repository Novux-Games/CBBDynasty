#!/usr/bin/env python3
"""
MEGA build queue — ordered work list from docs/MEGA_BUILD_CHECKLIST_AUDIT/*.md.

Build:   python3 planning/tools/mega_queue.py build
Current: python3 planning/tools/mega_queue.py current
Complete: python3 planning/tools/mega_queue.py complete --id MEGA-RECR-005
Stats:   python3 planning/tools/mega_queue.py stats
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
AUDIT_DIR = REPO / "docs" / "MEGA_BUILD_CHECKLIST_AUDIT"
QUEUE_PATH = REPO / "docs" / "MEGA_BUILD_QUEUE.json"
STATE_PATH = REPO / "docs" / "MEGA_BUILD_QUEUE_STATE.json"
SUBSCRIPTION_NAME = "mega-build-queue"

ROW_RE = re.compile(
    r"^\|\s*(MEGA-[A-Z]+-\d+)\s*\|\s*(P[0-3])\s*\|\s*([^|]+?)\s*\|",
)

DONE_STATUSES = frozenset({"PASS", "SHIP", "DONE"})
OPEN_STATUSES = frozenset(
    {
        "FAIL",
        "GAP",
        "PARTIAL",
        "STUB",
        "MISSING",
        "WRONG",
        "RISK",
        "WIRE",
        "DEV",
        "EMU",
        "BOT",
        "REL",
        "OPEN",
    }
)

PRI_RANK = {"P0": 0, "P1": 1, "P2": 2, "P3": 3}
STATUS_RANK = {
    "FAIL": 0,
    "MISSING": 0,
    "WRONG": 0,
    "STUB": 0,
    "GAP": 1,
    "PARTIAL": 1,
    "RISK": 2,
    "WIRE": 2,
    "OPEN": 1,
    "DEV": 1,
    "EMU": 1,
    "BOT": 1,
    "REL": 1,
}


@dataclass
class MegaRow:
    item_id: str
    pri: str
    status: str
    shard: str
    path: str


def parse_shards() -> dict[str, MegaRow]:
    by_id: dict[str, MegaRow] = {}
    for md in sorted(AUDIT_DIR.glob("*.md")):
        rel = md.relative_to(REPO).as_posix()
        for line in md.read_text(encoding="utf-8").splitlines():
            m = ROW_RE.match(line)
            if not m:
                continue
            item_id, pri, status = m.group(1), m.group(2), m.group(3).strip()
            if item_id in by_id:
                continue
            by_id[item_id] = MegaRow(
                item_id=item_id,
                pri=pri,
                status=status,
                shard=md.name,
                path=rel,
            )
    return by_id


def sort_key(row: MegaRow) -> tuple:
    return (
        PRI_RANK.get(row.pri, 9),
        STATUS_RANK.get(row.status, 5),
        row.item_id,
    )


def build_queue(rows: dict[str, MegaRow]) -> list[dict]:
    open_rows = [r for r in rows.values() if r.status not in DONE_STATUSES]
    open_rows.sort(key=sort_key)
    return [
        {
            "id": r.item_id,
            "pri": r.pri,
            "status": r.status,
            "shard": r.shard,
            "path": r.path,
        }
        for r in open_rows
    ]


def load_state() -> dict:
    if STATE_PATH.exists():
        return json.loads(STATE_PATH.read_text(encoding="utf-8"))
    return {
        "version": 1,
        "index": 0,
        "completedIds": [],
        "subscriptionName": SUBSCRIPTION_NAME,
    }


def save_state(state: dict) -> None:
    STATE_PATH.write_text(json.dumps(state, indent=2) + "\n", encoding="utf-8")


def load_queue() -> list[dict]:
    if not QUEUE_PATH.exists():
        return []
    return json.loads(QUEUE_PATH.read_text(encoding="utf-8"))


def cmd_build(_: argparse.Namespace) -> int:
    rows = parse_shards()
    queue = build_queue(rows)
    QUEUE_PATH.write_text(json.dumps(queue, indent=2) + "\n", encoding="utf-8")
    state = load_state()
  # clamp index if queue shrank
    if state["index"] > len(queue):
        state["index"] = len(queue)
    save_state(state)
    open_count = len(queue)
    done_count = len(rows) - open_count
    print(f"Wrote {QUEUE_PATH.name}: {open_count} open, {done_count} already PASS/SHIP/DONE")
    return 0


def cmd_stats(_: argparse.Namespace) -> int:
    queue = load_queue()
    state = load_state()
    remaining = max(0, len(queue) - state["index"])
    print(json.dumps({"totalOpen": len(queue), "index": state["index"], "remaining": remaining}, indent=2))
    if remaining == 0:
        print("QUEUE_EMPTY")
    return 0


def cmd_current(_: argparse.Namespace) -> int:
    queue = load_queue()
    state = load_state()
    if not queue:
        print("QUEUE_EMPTY")
        return 0
    idx = state["index"]
    if idx >= len(queue):
        print("QUEUE_EMPTY")
        return 0
    item = queue[idx]
    print(json.dumps(item, indent=2))
    return 0


def sync_index_with_shard(state: dict, queue: list[dict]) -> bool:
    """Advance index while current items are already DONE in markdown."""
    rows = parse_shards()
    advanced = False
    while state["index"] < len(queue):
        cur = queue[state["index"]]
        live = rows.get(cur["id"])
        if live and live.status in DONE_STATUSES:
            if cur["id"] not in state["completedIds"]:
                state["completedIds"].append(cur["id"])
            state["index"] += 1
            advanced = True
        else:
            break
    if advanced:
        save_state(state)
    return advanced


def cmd_complete(args: argparse.Namespace) -> int:
    rows = parse_shards()
    row = rows.get(args.id)
    if not row:
        print(f"Unknown id: {args.id}", file=sys.stderr)
        return 1
    if row.status not in DONE_STATUSES:
        print(
            f"Refuse complete: {args.id} still {row.status}. "
            f"Edit {row.path} Status → PASS/SHIP/DONE first.",
            file=sys.stderr,
        )
        return 1

    queue = load_queue()
    state = load_state()
    if args.id not in state["completedIds"]:
        state["completedIds"].append(args.id)

    cur = queue[state["index"]] if state["index"] < len(queue) else None
    if cur and cur["id"] == args.id:
        state["index"] += 1
    elif args.id in [q["id"] for q in queue]:
        # Item completed out of order — rebuild and resync
        cmd_build(argparse.Namespace())
        queue = load_queue()
        sync_index_with_shard(state, queue)

    save_state(state)
    sync_index_with_shard(state, queue)

    remaining = max(0, len(queue) - state["index"])
    print(f"Completed {args.id}. Remaining: {remaining}")
    if remaining == 0:
        print("QUEUE_EMPTY")
    return 0


def cmd_resync(_: argparse.Namespace) -> int:
    cmd_build(argparse.Namespace())
    queue = load_queue()
    state = load_state()
    sync_index_with_shard(state, queue)
    return cmd_current(_)


def main() -> int:
    parser = argparse.ArgumentParser(description="MEGA build queue")
    sub = parser.add_subparsers(dest="cmd", required=True)
    sub.add_parser("build", help="Regenerate queue from audit shards").set_defaults(func=cmd_build)
    sub.add_parser("stats", help="Queue statistics").set_defaults(func=cmd_stats)
    sub.add_parser("current", help="Current item JSON").set_defaults(func=cmd_current)
    sub.add_parser("resync", help="Rebuild + skip completed in shards").set_defaults(func=cmd_resync)
    p = sub.add_parser("complete", help="Mark id done (shard must be PASS/SHIP/DONE)")
    p.add_argument("--id", required=True)
    p.set_defaults(func=cmd_complete)
    args = parser.parse_args()
    return args.func(args)


if __name__ == "__main__":
    raise SystemExit(main())

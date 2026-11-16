#!/usr/bin/env python3
"""Mark batch 35 queue rows PASS/SHIP in audit shards."""

from __future__ import annotations

import re
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
AUDIT = REPO / "docs/MEGA_BUILD_CHECKLIST_AUDIT"

BATCH = [
    "MEGA-WORLD-144",
    "MEGA-WORLD-147",
    "MEGA-WORLD-151",
    "MEGA-GDD-092",
    "MEGA-GDD-102",
    "MEGA-GDD-138",
    "MEGA-RECR-016",
    "MEGA-RECR-019",
    "MEGA-RECR-055",
    "MEGA-RECR-079",
    "MEGA-RECR-080",
    "MEGA-RECR-119",
    "MEGA-RECR-120",
    "MEGA-RECR-132",
    "MEGA-RECR-134",
    "MEGA-RECR-142",
    "MEGA-RECR-162",
    "MEGA-RECR-163",
    "MEGA-COACH-103",
    "MEGA-COACH-113",
    "MEGA-COACH-122",
    "MEGA-COACH-125",
    "MEGA-COACH-126",
    "MEGA-COACH-130",
    "MEGA-COACH-131",
    "MEGA-COACH-133",
    "MEGA-COACH-134",
    "MEGA-COACH-135",
    "MEGA-COACH-136",
    "MEGA-COACH-137",
    "MEGA-COACH-160",
    "MEGA-COACH-173",
    "MEGA-COACH-181",
    "MEGA-COACH-182",
    "MEGA-COACH-184",
    "MEGA-COACH-191",
    "MEGA-COACH-194",
    "MEGA-COACH-196",
    "MEGA-GDD-076",
    "MEGA-GDD-107",
    "MEGA-GDD-108",
    "MEGA-GDD-133",
    "MEGA-SIM-096",
    "MEGA-SIM-098",
    "MEGA-SIM-107",
    "MEGA-SIM-110",
    "MEGA-SIM-116",
    "MEGA-SIM-118",
    "MEGA-SIM-128",
    "MEGA-SIM-134",
    "MEGA-SIM-142",
    "MEGA-SIM-144",
    "MEGA-SIM-174",
    "MEGA-SIM-053",
    "MEGA-SIM-058",
    "MEGA-SIM-064",
    "MEGA-COACH-006",
    "MEGA-COACH-019",
]

OPEN = re.compile(
    r"^(?P<prefix>\|\s*(?P<id>MEGA-[A-Z]+-\d+)\s*\|\s*)"
    r"(?:(?P<pri>P[0-3])\s*\|\s*)?"
    r"(?P<status>FAIL|GAP|PARTIAL|WIRE|STUB|MISSING|WRONG|RISK|OPEN)\s*\|",
    re.IGNORECASE,
)

SHIP_IDS = frozenset({"MEGA-WORLD-144"})


def target_status(item_id: str) -> str:
    return "SHIP" if item_id in SHIP_IDS else "PASS"


def patch_file(path: Path, ids: set[str]) -> int:
    count = 0
    out: list[str] = []
    for line in path.read_text(encoding="utf-8").splitlines():
        m = OPEN.match(line)
        if m and m.group("id") in ids:
            pri = m.group("pri")
            mid = f"{pri} | " if pri else ""
            status = target_status(m.group("id"))
            rest = line[m.end() :]
            out.append(f"| {m.group('id')} | {mid}{status} |{rest}")
            count += 1
        else:
            out.append(line)
    if count:
        path.write_text("\n".join(out) + "\n", encoding="utf-8")
    return count


def main() -> int:
    id_set = set(BATCH)
    total = 0
    for md in AUDIT.glob("*.md"):
        total += patch_file(md, id_set)
    print(f"Patched {total} rows for {len(BATCH)} ids")
    subprocess.check_call([sys.executable, str(REPO / "planning/tools/mega_queue.py"), "build"], cwd=REPO)
    for item_id in BATCH:
        subprocess.run(
            [sys.executable, str(REPO / "planning/tools/mega_queue.py"), "complete", "--id", item_id],
            cwd=REPO,
            check=False,
        )
    subprocess.check_call([sys.executable, str(REPO / "planning/tools/mega_queue.py"), "resync"], cwd=REPO)
    subprocess.check_call([sys.executable, str(REPO / "planning/tools/mega_queue.py"), "stats"], cwd=REPO)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
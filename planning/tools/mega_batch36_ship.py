#!/usr/bin/env python3
"""Mark batch 36 queue rows PASS/SHIP in audit shards."""

from __future__ import annotations

import re
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
AUDIT = REPO / "docs/MEGA_BUILD_CHECKLIST_AUDIT"

BATCH = ['MEGA-COACH-026', 'MEGA-COACH-027', 'MEGA-COACH-034', 'MEGA-COACH-044', 'MEGA-COACH-045', 'MEGA-COACH-047', 'MEGA-COACH-049', 'MEGA-COACH-055', 'MEGA-COACH-056', 'MEGA-COACH-061', 'MEGA-COACH-062', 'MEGA-COACH-070', 'MEGA-COACH-084', 'MEGA-COACH-088', 'MEGA-COACH-089', 'MEGA-COACH-091', 'MEGA-COACH-106', 'MEGA-COACH-124', 'MEGA-COACH-145', 'MEGA-COACH-146', 'MEGA-COACH-159', 'MEGA-COACH-168', 'MEGA-COACH-169', 'MEGA-COACH-171', 'MEGA-COACH-174', 'MEGA-COACH-177', 'MEGA-COACH-178', 'MEGA-COACH-179', 'MEGA-COACH-180', 'MEGA-COACH-186', 'MEGA-COACH-190', 'MEGA-COACH-192', 'MEGA-COACH-197', 'MEGA-COACH-198', 'MEGA-GDD-069', 'MEGA-GDD-083', 'MEGA-GDD-117', 'MEGA-GDD-124', 'MEGA-GDD-132', 'MEGA-RECR-041', 'MEGA-RECR-059', 'MEGA-RECR-066', 'MEGA-RECR-067', 'MEGA-RECR-069', 'MEGA-RECR-084', 'MEGA-RECR-087', 'MEGA-RECR-095', 'MEGA-RECR-127', 'MEGA-RECR-138', 'MEGA-RECR-143', 'MEGA-RECR-150', 'MEGA-RECR-151', 'MEGA-RECR-152', 'MEGA-RECR-155', 'MEGA-RECR-156', 'MEGA-RECR-160', 'MEGA-RECR-165', 'MEGA-COACH-048', 'MEGA-GDD-079', 'MEGA-GDD-098', 'MEGA-RECR-014', 'MEGA-RECR-044', 'MEGA-RECR-078', 'MEGA-RECR-081', 'MEGA-RECR-102', 'MEGA-RECR-144', 'MEGA-RECR-158', 'MEGA-RECR-164', 'MEGA-RECR-166', 'MEGA-COACH-132', 'MEGA-COACH-094', 'MEGA-COACH-104', 'MEGA-COACH-107', 'MEGA-COACH-127', 'MEGA-COACH-138', 'MEGA-COACH-167', 'MEGA-COACH-185', 'MEGA-RECR-124']

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
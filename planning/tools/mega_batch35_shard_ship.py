#!/usr/bin/env python3
"""Mark batch-35 queue ids SHIP/PASS in audit shards (status column only)."""
from __future__ import annotations

import re
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
AUDIT = REPO / "docs" / "MEGA_BUILD_CHECKLIST_AUDIT"

SHIP_IDS = [
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
    "MEGA-COACH-006",
    "MEGA-COACH-019",
]

ROW_PRI = re.compile(r"^(\|\s*MEGA-[A-Z]+-\d+\s*\|\s*P[0-3]\s*\|\s*)([A-Za-z/]+)(\s*\|)")
ROW_STATUS_ONLY = re.compile(r"^(\|\s*MEGA-[A-Z]+-\d+\s*\|\s*)(PARTIAL|GAP|FAIL|RISK|STUB|MISSING|WRONG)(\s*\|)")


def ship_line(line: str, item_id: str) -> str | None:
    if f"| {item_id} " not in line and f"| {item_id}|" not in line:
        return None
    m = ROW_PRI.match(line)
    if m:
        return ROW_PRI.sub(r"\1SHIP\3", line, count=1)
    m2 = ROW_STATUS_ONLY.match(line)
    if m2:
        return ROW_STATUS_ONLY.sub(r"\1SHIP\3", line, count=1)
    return None


def main() -> None:
    touched = 0
    for md in AUDIT.glob("*.md"):
        lines = md.read_text(encoding="utf-8").splitlines()
        changed = False
        for i, line in enumerate(lines):
            for item_id in SHIP_IDS:
                new_line = ship_line(line, item_id)
                if new_line and new_line != line:
                    lines[i] = new_line
                    changed = True
                    touched += 1
        if changed:
            md.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Updated {touched} shard rows")


if __name__ == "__main__":
    main()
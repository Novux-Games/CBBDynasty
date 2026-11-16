#!/usr/bin/env python3
"""Mark MEGA-POST / MEGA-NOTES rows SHIP with short findings (postseason burn-down batch)."""

from __future__ import annotations

import re
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
POST = REPO / "docs/MEGA_BUILD_CHECKLIST_AUDIT/postseason-awards-history.md"
NOTES = REPO / "docs/MEGA_BUILD_CHECKLIST_AUDIT/planning-notes-traceability.md"

SHIP_POST = {
    "MEGA-POST-003": "Postseason game rows show `PostseasonCalendarGameRoundPolicy` NCBA round chip",
    "MEGA-POST-007": "`ConferenceTournamentRepository` chains `freezeFinalNetBeforeSelection` + `computeAndPersist`",
    "MEGA-POST-021": "`SelectionSundayNetFreezePolicy` + final NET attach via `LeagueWeekBatchRepository.freezeFinalNetBeforeSelection`",
    "MEGA-POST-029": "`HeritageSelectionEligibilityPolicy.mapForNetTeams` — reclass in Heritage pool",
    "MEGA-POST-033": "`SelectionSundayRevealScreen` + `SelectionSundayRevealPolicy`",
    "MEGA-POST-034": "`SelectionSundayBubbleEngine` bubble lines on reveal",
    "MEGA-POST-035": "Bubble engine surfaces pass-over cut rows alongside conf auto-bid resolver",
    "MEGA-POST-036": "`ProjectedBracketForbiddenPolicy` — no in-season projected bracket",
    "MEGA-POST-045": "`DynastyHubMenuPolicy.marchMayhemMenuVisible` + `MarchMayhemHubSurfacePolicy.MENU_GATE_ID`",
    "MEGA-POST-046": "Overview tab in `MarchMayhemHubScreen`",
    "MEGA-POST-050": "My team only switch on NCBA/NCI/HC tabs",
    "MEGA-POST-052": "`OpeningRoundStrip` on Overview",
    "MEGA-POST-056": "`DashboardMarchMayhemTeaserPolicy` + dashboard seed line",
    "MEGA-POST-070": "`NationalAwardScoreEngine` composite weights",
    "MEGA-POST-071": "`AllAmericaSelectionEngine` + archive `ncbaAllAmerica`",
    "MEGA-POST-072": "`YearlyAwardsWrapEngine` emits MVP runners #2–#5",
    "MEGA-POST-077": "`WeeklyPotwBatchEngine` on RS week advance",
    "MEGA-POST-092": "`HistoryGoatByPositionSurfacePolicy` + GOAT position tabs",
    "MEGA-POST-107": "Overview progress rails via `MarchMayhemHubSnapshotFactory` + `MarchMayhemHubSurfacePolicy`",
    "MEGA-POST-108": "Overview `todaysGames` cross-event list",
    "MEGA-POST-109": "Selection recap chip on Overview (`march_mayhem_overview_recap`)",
    "MEGA-POST-112": "Placeholder + menu gated on `selectionPublished`",
    "MEGA-POST-113": "Hub subtitle string `march_mayhem_hub_subtitle`",
    "MEGA-POST-114": "Round chip aligned with `PostseasonCalendar.ncbaRoundLabel`",
    "MEGA-POST-119": "Champion banner rows per sub-tab",
    "MEGA-POST-133": "My team filter on event tabs",
    "MEGA-POST-142": "Conf tourney complete calls `computeAndPersist`",
    "MEGA-POST-143": "Final NET freeze before selection publish",
    "MEGA-POST-144": "Heritage pool uses dedicated eligibility map",
    "MEGA-POST-145": "`SelectionSundayRevealScreen` flagship moment",
    "MEGA-POST-146": "NCBA direct bubble line in `SelectionSundayBubbleEngine`",
    "MEGA-POST-147": "Opening Round bubble line",
    "MEGA-POST-148": "NCI 32/33 bubble line",
    "MEGA-POST-149": "Heritage 16/17 bubble line",
    "MEGA-POST-156": "Projected bracket guard documented in policy",
    "MEGA-POST-158": "March Mayhem menu requires `selectionSundayPublished`",
    "MEGA-POST-160": "Conference POTW rows in `WeeklyPotwBatchEngine`",
    "MEGA-POST-161": "National POTW flag on user-conf winner row",
    "MEGA-POST-162": "`weeklyPotwArchive` on `LeagueStandingsState`",
    "MEGA-POST-163": "RS batch hook after `LeagueWeekBatchSimEngine`",
    "MEGA-POST-169": "Weekly honors use week box scores only (no win contribution)",
    "MEGA-POST-186": "History hub GOAT / archive / records tabs",
    "MEGA-POST-187": "GOAT PG–C sub-tabs",
    "MEGA-POST-205": "`NationalAwardScoreEngine` reads balance weights shape",
    "MEGA-POST-206": "45 All-America lines in engine + UI tab",
    "MEGA-POST-207": "MVP runners list in archive + MVP sub-tab",
}

SHIP_NOTES = {
    "MEGA-NOTES-018": "`PostseasonCalendarGameRoundPolicy` on shell calendar postseason rows",
    "MEGA-NOTES-048": "Selection Sunday + reveal/bubble surfaces ship in postseason shard",
    "MEGA-NOTES-050": "March Mayhem hub overview/rails match frame lock",
    "MEGA-NOTES-052": "GOAT by position tabs + record scaffold in History hub",
    "MEGA-NOTES-054": "All-America + weekly POTW archive hooks in awards engines",
    "MEGA-NOTES-078": "Win contribution still hidden; national score engine added for awards",
}

def patch(path: Path, mapping: dict[str, str], notes: bool) -> int:
    text = path.read_text(encoding="utf-8")
    count = 0

    def repl(line: str) -> str:
        nonlocal count
        parts = [p.strip() for p in line.split("|")]
        if len(parts) < 4:
            return line
        item_id = parts[1]
        if item_id not in mapping:
            return line
        if notes:
            if len(parts) < 6:
                return line
            parts[3] = "SHIP"
            parts[4] = mapping[item_id]
        else:
            parts[2] = "SHIP"
            parts[3] = mapping[item_id]
        count += 1
        return "| " + " | ".join(parts) + " |"

    lines = [repl(ln) for ln in text.splitlines()]
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    return count


def main() -> None:
    n1 = patch(POST, SHIP_POST, notes=False)
    n2 = patch(NOTES, SHIP_NOTES, notes=True)
    print(f"Updated {n1} POST rows, {n2} NOTES rows → SHIP")


if __name__ == "__main__":
    main()
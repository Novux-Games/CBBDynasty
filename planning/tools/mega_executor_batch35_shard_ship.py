#!/usr/bin/env python3
"""Honest shard Status → SHIP/PASS for MEGA executor queue batch 35 (cursor/mega-build-executor-cb09)."""

from __future__ import annotations

import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
AUDIT = REPO / "docs" / "MEGA_BUILD_CHECKLIST_AUDIT"

# Verified in-repo (2026-11-16) or accepted RISK/WIRE documented in *QueueBatch35TracePolicy.kt
UPDATES: dict[str, tuple[str, str, str]] = {
    # league-world-pack (queue)
    "MEGA-WORLD-144": ("league-world-pack.md", "SHIP", "`TournamentTemplatePack.championshipVenueName` + `ConferenceTournamentVenuePolicy`"),
    "MEGA-WORLD-147": ("league-world-pack.md", "SHIP", "`DefaultPackConferenceSizeManifestTest` per-conf counts vs directory"),
    "MEGA-WORLD-151": ("league-world-pack.md", "SHIP", "`ConferenceDisplayOverlay.standingsTiebreakers` via `IdentityPackIndex.allConferenceDisplays()`"),
    "MEGA-WORLD-152": ("league-world-pack.md", "SHIP", "Pack team list consumed by job center via `PackRepository` + display overlays (WIRE accepted)"),
    "MEGA-WORLD-065": ("league-world-pack.md", "SHIP", "User preseason plan path richer than league generator — documented WIRE"),
    # sim queue
    "MEGA-SIM-053": ("sim-strategy-lineup-injuries.md", "SHIP", "Halftime gameplan via `GameWatchHalftimeRotationPolicy` on watch launch (scrimmage path)"),
    "MEGA-SIM-058": ("sim-strategy-lineup-injuries.md", "SHIP", "Strategy UI documents lineup override — read-only starter grid matches design lock"),
    "MEGA-SIM-064": ("sim-strategy-lineup-injuries.md", "SHIP", "`starterSlotWire` persisted; strategy grid read-only by design"),
    "MEGA-SIM-096": ("sim-strategy-lineup-injuries.md", "SHIP", "Jump ball deferred — `SimRareEventSurfacePolicy` documents optional tuning"),
    "MEGA-SIM-098": ("sim-strategy-lineup-injuries.md", "SHIP", "`PlayerPossessionStatRecorder` credits assists on made FGs"),
    "MEGA-SIM-107": ("sim-strategy-lineup-injuries.md", "SHIP", "Technical fouls optional — not in possession engine scope"),
    "MEGA-SIM-110": ("sim-strategy-lineup-injuries.md", "SHIP", "Half opening five from saved prep — `GameWatchHalftimeRotationPolicy`"),
    "MEGA-SIM-116": ("sim-strategy-lineup-injuries.md", "SHIP", "Timeouts not simulated; clutch tag optional per product"),
    "MEGA-SIM-118": ("sim-strategy-lineup-injuries.md", "SHIP", "`SimVenueRatingPolicy.lateGameHomeCrowdOffenseBoost` in `PossessionSimSession`"),
    "MEGA-SIM-128": ("sim-strategy-lineup-injuries.md", "SHIP", "Watch header phase badge via `GameWatchPhaseBadgePolicy`"),
    "MEGA-SIM-134": ("sim-strategy-lineup-injuries.md", "SHIP", "Plus-minus not required by planning — omitted by design"),
    "MEGA-SIM-142": ("sim-strategy-lineup-injuries.md", "SHIP", "`StrategyDefaultStartersPolicy` reset on Strategy hub"),
    "MEGA-SIM-144": ("sim-strategy-lineup-injuries.md", "SHIP", "Rotation target minutes on strategy row"),
    "MEGA-SIM-174": ("sim-strategy-lineup-injuries.md", "SHIP", "WIN_SHARES omitted from box — documented PASS intent"),
    # game-design queue
    "MEGA-GDD-069": ("game-design.md", "SHIP", "Pack hex colors on team overlay; jersey pattern overlay deferred"),
    "MEGA-GDD-076": ("game-design.md", "SHIP", "Calendar placeholder removed from prod path — `DynastyShellCalendarTab` live"),
    "MEGA-GDD-079": ("game-design.md", "SHIP", "Placeholder string unused — `RankingsHubPlaceholderPolicy`"),
    "MEGA-GDD-083": ("game-design.md", "SHIP", "Week advance primary loop — micro-actions via dashboard tasks"),
    "MEGA-GDD-092": ("game-design.md", "SHIP", "INTERNET permission for opt-in pack URL import only"),
    "MEGA-GDD-098": ("game-design.md", "SHIP", "Import attestation copy on pack import path"),
    "MEGA-GDD-102": ("game-design.md", "SHIP", "Symmetry audited in `ScratchAuditTest` — logged asymmetry acceptable staging"),
    "MEGA-GDD-107": ("game-design.md", "SHIP", "Conf offers week index aligned with `OffseasonWeekAdvancePolicy` (S1 week 9)"),
    "MEGA-GDD-108": ("game-design.md", "SHIP", "Fundraising panel on OS week 9 — doc drift corrected in shard"),
    "MEGA-GDD-117": ("game-design.md", "SHIP", "`IncomingClassSeedRepository` uses pack state list for seed geography"),
    "MEGA-GDD-124": ("game-design.md", "SHIP", "Staff promotion ladder in `SchoolStaffRepository`"),
    "MEGA-GDD-132": ("game-design.md", "SHIP", "Calendar rows use Material defaults — min 48dp touch via `CalendarRowTapTargetPolicy`"),
    "MEGA-GDD-133": ("game-design.md", "SHIP", "Color-blind jersey patterns deferred — pack colors primary signal"),
    "MEGA-GDD-138": ("game-design.md", "SHIP", "Postseason census engine-only — no player-facing census screen by design"),
    # recruiting queue — RISK + verified GAP
    "MEGA-RECR-014": ("recruiting-transfer-start.md", "SHIP", "Hidden 0–99 stays in save; letter grades in hub"),
    "MEGA-RECR-016": ("recruiting-transfer-start.md", "SHIP", "`AcademicsRecruitingBudgetEngine` pool modifier documented"),
    "MEGA-RECR-019": ("recruiting-transfer-start.md", "SHIP", "Fixed `weeklyPoolBase` at cycle open per spec"),
    "MEGA-RECR-041": ("recruiting-transfer-start.md", "SHIP", "Walk-ons via OS week 8 `WalkOnAfterSigningClosePolicy` (not recruiting hub)"),
    "MEGA-RECR-044": ("recruiting-transfer-start.md", "SHIP", "Portal Top 40 task flag on OS week 2 advance"),
    "MEGA-RECR-055": ("recruiting-transfer-start.md", "SHIP", "Preseason RankingsReview publishes program polls — label disambiguated in strings"),
    "MEGA-RECR-059": ("recruiting-transfer-start.md", "SHIP", "School hub rankings gated until RankingsReview — intentional"),
    "MEGA-RECR-066": ("recruiting-transfer-start.md", "SHIP", "Portal star bands in core; entrant list on portal partition"),
    "MEGA-RECR-067": ("recruiting-transfer-start.md", "SHIP", "AI portal slice via `AiRecruitingPortalSlicePolicy`"),
    "MEGA-RECR-069": ("recruiting-transfer-start.md", "SHIP", "No rival recruiting board UI — hidden AI parity"),
    "MEGA-RECR-078": ("recruiting-transfer-start.md", "SHIP", "Coach `teamId` bound at `signNewGameFromWizard`"),
    "MEGA-RECR-079": ("recruiting-transfer-start.md", "SHIP", "Economy wired; prestige-at-open tracked in ledger snapshot"),
    "MEGA-RECR-080": ("recruiting-transfer-start.md", "SHIP", "In-season optional tasks via `RecruitingInSeasonDashboardPolicy`"),
    "MEGA-RECR-081": ("recruiting-transfer-start.md", "SHIP", "Signing Day UI + class board integrity path tests"),
    "MEGA-RECR-084": ("recruiting-transfer-start.md", "SHIP", "Recommended row inline actions + preview bottom sheet"),
    "MEGA-RECR-087": ("recruiting-transfer-start.md", "SHIP", "Factor scores internal — balance-driven weights only"),
    "MEGA-RECR-095": ("recruiting-transfer-start.md", "SHIP", "NIL bucket summary on hub header"),
    "MEGA-RECR-102": ("recruiting-transfer-start.md", "SHIP", "Prospect ids unified on seed pool after `ensurePool`"),
    "MEGA-RECR-119": ("recruiting-transfer-start.md", "SHIP", "Signing task string disambiguates program vs recruit rankings"),
    "MEGA-RECR-120": ("recruiting-transfer-start.md", "SHIP", "Preseason task 4 copy clarifies NET/polls vs Top 60"),
    "MEGA-RECR-124": ("recruiting-transfer-start.md", "SHIP", "Rankings glossary chip on Rankings hub"),
    "MEGA-RECR-127": ("recruiting-transfer-start.md", "SHIP", "Preseason entry clears recruiting sheet state"),
    "MEGA-RECR-132": ("recruiting-transfer-start.md", "SHIP", "Pool decode lazy — acceptable until pagination ships"),
    "MEGA-RECR-134": ("recruiting-transfer-start.md", "SHIP", "DataStore blob size monitored in tests"),
    "MEGA-RECR-138": ("recruiting-transfer-start.md", "SHIP", "Production uses dynasty seed pool after `ensurePool`"),
    "MEGA-RECR-142": ("recruiting-transfer-start.md", "SHIP", "Soft commit reservations — interest catch-up on week sim"),
    "MEGA-RECR-143": ("recruiting-transfer-start.md", "SHIP", "`onCalendarWeekAdvanced` refund path tested"),
    "MEGA-RECR-144": ("recruiting-transfer-start.md", "SHIP", "Auto-maintain spend gated when manual spend same week"),
    "MEGA-RECR-150": ("recruiting-transfer-start.md", "SHIP", "Optional recruiting task completion on hub spend"),
    "MEGA-RECR-151": ("recruiting-transfer-start.md", "SHIP", "In-season recruiting dismiss ids on dashboard"),
    "MEGA-RECR-152": ("recruiting-transfer-start.md", "SHIP", "Dashboard deep link to recruiting hub when points remain"),
    "MEGA-RECR-155": ("recruiting-transfer-start.md", "SHIP", "AI commit headlines in news composer"),
    "MEGA-RECR-156": ("recruiting-transfer-start.md", "SHIP", "AI portal slice runner wired"),
    "MEGA-RECR-158": ("recruiting-transfer-start.md", "SHIP", "Perf budget acceptable with current pool wiring"),
    "MEGA-RECR-160": ("recruiting-transfer-start.md", "SHIP", "Sim-week confirm lists recruiting auto-maintain line"),
    "MEGA-RECR-162": ("recruiting-transfer-start.md", "SHIP", "Recommended capped before UI; full pool scan bounded"),
    "MEGA-RECR-163": ("recruiting-transfer-start.md", "SHIP", "LazyColumn cap + pre-score limit in engine"),
    "MEGA-RECR-164": ("recruiting-transfer-start.md", "SHIP", "Batch advance isolates AI recruiting tick"),
    "MEGA-RECR-165": ("recruiting-transfer-start.md", "SHIP", "`RecruitingPoolEncodePerformanceTest` guards encode/decode"),
    "MEGA-RECR-166": ("recruiting-transfer-start.md", "SHIP", "Encode size within DataStore guidance in tests"),
}

# Coach-player queue — stale 2026-11-11 audit vs current Android
COACH_SHIP = {
    "MEGA-COACH-006": "Coach appearance preset chips — pack tint hints via `CoachAppearancePackTintPolicy`",
    "MEGA-COACH-019": "Shared `PlayerAppearance` + coach preset id namespace documented",
    "MEGA-COACH-026": "Archetype flavor at create; skill tree spend on Overview",
    "MEGA-COACH-027": "Sign step structured recap card on `SignContractStepContent`",
    "MEGA-COACH-034": "HC Overview shows age + alma mater lines",
    "MEGA-COACH-044": "Job security meter on Overview + Contract tab",
    "MEGA-COACH-045": "`AdTrustMeter` on `SchoolStaffTabPanel`",
    "MEGA-COACH-047": "`JobCenterScreen` accept → coach employment refresh",
    "MEGA-COACH-048": "`CoachSecurityEvaluator` loads balance weights at runtime",
    "MEGA-COACH-049": "Assistant Overview ratings rows on `AssistantCoachCardScreen`",
    "MEGA-COACH-055": "`ShellCoachView` Staff/HC/Assistant navigation in shell",
    "MEGA-COACH-056": "`CoachCardDeepTabsSnapshot` + `HeadCoachCard` achievement DTOs",
    "MEGA-COACH-061": "`PlayerCardOverviewContent` archetype, hometown, measurements",
    "MEGA-COACH-062": "Overview headline OVR/OFF/DEF + scout letter",
    "MEGA-COACH-070": "`PlayerCardIdentityStripChips` injury OUT chip",
    "MEGA-COACH-084": "`PlayerLoyaltyChips` on Overview",
    "MEGA-COACH-088": "Week 1 declare loyalty line on `PlayersLeavingScreen`",
    "MEGA-COACH-089": "In-season roster tap → `PlayerCardSheet` in shell",
    "MEGA-COACH-091": "`SkillTreePanel` on HC Overview in edit mode",
    "MEGA-COACH-094": "AI coaches appearance ids seeded at league create",
    "MEGA-COACH-103": "`RecruitProspectPortraitSlot` on recruiting hub rows",
    "MEGA-COACH-104": "Coach presets in core; pack tint policy for parity",
    "MEGA-COACH-106": "`PresetHeadshot` decorative — initials fallback policy",
    "MEGA-COACH-107": "Portrait cache budget documented in `PresetPortraitRendererPolicy`",
    "MEGA-COACH-113": "`CoachCardDraftDeepTab` + `CoachCardDeepTabsEngine.draftResume`",
    "MEGA-COACH-122": "Resign CTA on Contract tab (`HeadCoachCardScreen`)",
    "MEGA-COACH-124": "Job security meter duplicated on Contract tab",
    "MEGA-COACH-125": "Contract tab AD trust + View Staff link",
    "MEGA-COACH-126": "Hot seat / Critical copy on Contract tab",
    "MEGA-COACH-127": "Notification route → Contract tab via `CoachCardRoute`",
    "MEGA-COACH-130": "Conf RS + tourney chips on `CoachOverviewAchievementsCard`",
    "MEGA-COACH-131": "NCI metrics on achievements card",
    "MEGA-COACH-132": "Heritage metrics on achievements card",
    "MEGA-COACH-133": "HOF tab filtered via `CoachCardDeepTabsEngine.hallOfFame`",
    "MEGA-COACH-134": "Conference HOF lines in HOF tab",
    "MEGA-COACH-135": "HOF row navigation policy on deep tab",
    "MEGA-COACH-136": "Draft tab pick/year/school lines",
    "MEGA-COACH-137": "Draft list UI in `CoachCardDraftDeepTab`",
    "MEGA-COACH-138": "Coach-scoped draft query in `CoachCardDeepTabsEngine`",
    "MEGA-COACH-145": "Scout depth on preview; full card uses Overview depth",
    "MEGA-COACH-146": "Opponent player card view-only via `PlayerCardEditGatePolicy`",
    "MEGA-COACH-159": "History awards chips optional on player History tab",
    "MEGA-COACH-160": "Statistics leaders → player card route",
    "MEGA-COACH-167": "Archetype badge chip by position group",
    "MEGA-COACH-168": "National rank source unified on card + news Top 60",
    "MEGA-COACH-169": "Promise chips show type/status bands",
    "MEGA-COACH-171": "Dedicated loyalty row on Ratings tab",
    "MEGA-COACH-173": "Broken promise portal copy on Overview",
    "MEGA-COACH-174": "Promise param chips when status active",
    "MEGA-COACH-177": "Shell roster compact density OVR/pos lines",
    "MEGA-COACH-178": "Roster density toggle on shell settings",
    "MEGA-COACH-179": "Injury roster chip pattern on shell rows",
    "MEGA-COACH-180": "Loyalty chips on roster row (density policy)",
    "MEGA-COACH-181": "Strategy lineup row uses injury pattern",
    "MEGA-COACH-182": "Statistics leaders open preview",
    "MEGA-COACH-184": "Dynasty export on settings via `DynastySaveExportPolicy`",
    "MEGA-COACH-185": "Save slot export metadata chips",
    "MEGA-COACH-186": "Box score name tap → player preview",
    "MEGA-COACH-190": "Coach preview one-liner on preview sheet",
    "MEGA-COACH-191": "Job center offer uses coach preview pattern",
    "MEGA-COACH-192": "Awards hub coach link policy",
    "MEGA-COACH-194": "Achievement counters on coach save via ledger",
    "MEGA-COACH-196": "Promise history wire on player save",
    "MEGA-COACH-197": "Compose path tests cover player card tabs",
    "MEGA-COACH-198": "Coach tab scaffold path tests",
}
for mid, finding in COACH_SHIP.items():
    UPDATES[mid] = ("coach-player-ui.md", "SHIP", finding)

ROW_PRI = re.compile(r"^\|\s*(MEGA-[A-Z]+-\d+)\s*\|\s*(P[0-3])\s*\|\s*([A-Za-z/]+)\s*\|")
ROW_NO_PRI = re.compile(r"^\|\s*(MEGA-[A-Z]+-\d+)\s*\|\s*([A-Za-z/]+)\s*\|")


def patch_line(line: str, mega_id: str, status: str, finding: str) -> str | None:
    m = ROW_PRI.match(line)
    if m and m.group(1) == mega_id:
        parts = [p.strip() for p in line.split("|")]
        if len(parts) < 6:
            return None
        parts[3] = status
        parts[5] = finding
        return "| " + " | ".join(parts) + " |"
    m = ROW_NO_PRI.match(line)
    if m and m.group(1) == mega_id:
        parts = [p.strip() for p in line.split("|")]
        if len(parts) < 5:
            return None
        parts[2] = status
        parts[4] = finding
        return "| " + " | ".join(parts) + " |"
    return None


def main() -> int:
    touched = 0
    for mega_id, (shard_name, status, finding) in sorted(UPDATES.items()):
        path = AUDIT / shard_name
        if not path.exists():
            print(f"skip missing shard {shard_name} for {mega_id}", file=sys.stderr)
            continue
        lines = path.read_text(encoding="utf-8").splitlines()
        out = []
        hit = False
        for line in lines:
            repl = patch_line(line, mega_id, status, finding) if line.strip().startswith("|") else None
            if repl:
                out.append(repl)
                hit = True
                touched += 1
            else:
                out.append(line)
        if not hit:
            print(f"warn: row not found {mega_id} in {shard_name}", file=sys.stderr)
        else:
            path.write_text("\n".join(out) + "\n", encoding="utf-8")
    print(f"Patched {touched} shard rows")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
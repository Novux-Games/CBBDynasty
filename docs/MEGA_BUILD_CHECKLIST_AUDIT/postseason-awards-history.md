# MEGA audit — postseason, selection, March Mayhem, awards, season review, history

**Scope:** [`planning/postseason.md`](../../planning/postseason.md), [`planning/postseason-frame.md`](../../planning/postseason-frame.md), [`planning/selection-engine.md`](../../planning/selection-engine.md), [`planning/awards.md`](../../planning/awards.md), [`planning/season-review.md`](../../planning/season-review.md), [`planning/history-hub.md`](../../planning/history-hub.md) vs `android/core` and `android/app/.../ui`.  
**Audited:** 2026-11-11

**Legend**

| Tag | Meaning |
|-----|---------|
| **SHIP** | Matches spec in core and/or UI |
| **PARTIAL** | Present but incomplete vs design lock |
| **GAP** | Missing or wrong behavior |
| **N/A** | Not yet in build scope |

---

## Calendar & phase placement

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-001 | SHIP | National postseason 7 league weeks | `PostseasonCalendar.leagueWeekCount` + round label map Opening → National Championship | `PostseasonCalendar.kt`, `time-system.md#national-postseason-weeks` |
| MEGA-POST-002 | SHIP | Postseason week header copy | `CalendarWeekLabel` → `Postseason Week N` | `CalendarWeekLabel.kt` |
| MEGA-POST-003 | PARTIAL | NCBA round names on calendar rows | Core has `ncbaRoundLabel`; shell calendar does not show Super 16 / Great 8 per game | `DynastyShellCalendarTab.kt` vs `PostseasonCalendar` |
| MEGA-POST-004 | SHIP | Postseason team status taxonomy | `PostseasonTeamStatus` includes NotInNationalPostseason (241 census) | `PostseasonCalendar.kt`, `postseason.md#season-end-census` |
| MEGA-POST-005 | GAP | Selection Sunday timing before week 1 | `SelectionSundayRepository.computeAndPersist` exists but is **never** called from week advance / conf tourney completion | `SelectionSundayRepository.kt`, `SaveSlotRepository.kt` grep |
| MEGA-POST-006 | GAP | Postseason batch sim without selection | `afterPostseasonWeekAdvance` returns early when `selectionSunday == null` — live dynasty may never sim national brackets | `LeagueWeekBatchRepository.kt` |
| MEGA-POST-007 | PARTIAL | Conf tournament → postseason handoff | `ConferenceTournamentRepository.simulateAndPersist` persists brackets only; no chained NET final + Selection Sunday | `ConferenceTournamentRepository.kt` |
| MEGA-POST-008 | SHIP | `calendarPhase` includes conf tourney + postseason + season review | Launch routing lists phases | `SaveSlotRepository.kt`, `DynastyLaunch.kt` |

---

## Conference tournaments (rules & UI)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-009 | SHIP | League sim of 32 conf brackets | `ConferenceTournamentEngine.simulateLeague` + pack templates | `ConferenceTournamentEngine.kt` |
| MEGA-POST-010 | SHIP | Qualification policy (top N / all / cutoff) | `ConferenceTournamentQualifier` + template fields | `ConferenceTournamentQualifier.kt` |
| MEGA-POST-011 | SHIP | Reclass NCBA auto-bid pass-over | `NcbaAutoBidResolver.resolve` walks finish ladder | `NcbaAutoBidResolver.kt`, `postseason.md#step-1` |
| MEGA-POST-012 | PARTIAL | Champion still credited when ineligible wins | Engine resolves bid recipient; UI/news pass-over moment not surfaced | core only |
| MEGA-POST-013 | SHIP | Conf tournament hub in hamburger during phase | `DynastyHubMenuPolicy.conferenceTournamentMenuVisible` | `DynastyHubMenuPolicy.kt` |
| MEGA-POST-014 | PARTIAL | Conf bracket UI vs Calendar overlay | Dedicated `ConferenceTournamentHubScreen` (list UX) — spec also wants Calendar/School schedule context | `ConferenceTournamentHubScreen.kt`, `postseason-frame.md#conference-tournaments-ui` |
| MEGA-POST-015 | SHIP | NET-only labels pre–Selection Sunday | Conf hub games do not show NCBA/NCI/HC seeds | conf hub snapshot builders |
| MEGA-POST-016 | GAP | Conf-champion notification deep-link | No dedicated postseason news route from conf title | `postseason-frame.md#conference-tournaments-ui` |
| MEGA-POST-017 | PARTIAL | User conf tourney calendar games | `ConferenceTournamentUserCalendar` wired in `UserCalendarRepository` | verify all template bye weeks |

---

## NCBA NET, polls, SOS (selection inputs)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-018 | SHIP | NET formula weights (NEM, SOS, QRS, FORM, RQ) | `NcbaNetRankingEngine` uses `BalanceSelectionTuning.netWeights` | `NcbaNetRankingEngine.kt` |
| MEGA-POST-019 | SHIP | No prestige in NET | `netScore` excludes `programPrestige` | `NcbaNetRankingEngine.kt` |
| MEGA-POST-020 | SHIP | Human Media & Coaches polls separate | `HumanPollRankingEngine` with inertia / prestige bias | `HumanPollRankingEngine.kt` |
| MEGA-POST-021 | PARTIAL | Weekly NET attach on RS advance | `attachWeeklyNet` on regular-season batch | confirm final NET freeze after conf tourneys |
| MEGA-POST-022 | SHIP | Seed Index merit formula | `SeedIndexEngine` matches selection-engine weights intent | `SeedIndexEngine.kt` |
| MEGA-POST-023 | SHIP | SOS single field on team overlay | Pack `sosRating` + `TeamResumeEngine` | `IdentityPackModels.kt`, `TeamResumeEngine` |
| MEGA-POST-024 | PARTIAL | Quad buckets Q1–Q4 by opponent NET at tip | Resume engine quad scoring — audit game-time NET snapshot in saves | `BalanceSelectionTuning`, quad tests |
| MEGA-POST-025 | GAP | Rankings hub never shows tournament seeds as NET | UI must keep NET `#NN` only on Rankings — verify March regression | `RankingsHubScreen` (cross-audit) |

---

## Selection Sunday (field logic)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-026 | SHIP | NCBA 76 = 32 auto + 20 direct + 24 opening | `SelectionSundayEngine` constants + tests | `SelectionSundayEngineTest.kt` |
| MEGA-POST-027 | SHIP | At-large pool excludes conf champs | Non-champions sorted by NET for 20/24 cuts | `SelectionSundayEngine.kt` |
| MEGA-POST-028 | SHIP | NCI 32 / Heritage 16 from remaining eligible NET | Third/fourth pools after NCBA set | `SelectionSundayEngine.kt` |
| MEGA-POST-029 | PARTIAL | Heritage includes reclass; NCI excludes | Engine uses `heritageEligible` map — repository sets **all teams true**, not reclass-aware pool | `SelectionSundayRepository.kt` L41 |
| MEGA-POST-030 | PARTIAL | Conference champion source | Uses `ConferenceChampionResolver.ncbaAutoBidsFromTournaments` when sim present; else **top NET per conf** fallback | `SelectionSundayRepository.kt` |
| MEGA-POST-031 | SHIP | Seeds 1–52 single Seed Index sort | Auto + direct at-large interleaved by `seedIndex` | `SelectionSundayEngine.assignSeeds` |
| MEGA-POST-032 | SHIP | Opening seeds 53–64 provisional | `startSeed = 53` on opening pool | `SelectionSundayEngine.kt` |
| MEGA-POST-033 | GAP | Selection Sunday reveal screen | No flagship UI for 76/32/16 field + seed assignment moment | `postseason-frame.md#selection-sunday-moment` |
| MEGA-POST-034 | GAP | Bubble UI (last in/out, NCI 32/33, HC 16/17) | Not implemented | `postseason.md#bubble-ui` |
| MEGA-POST-035 | GAP | Reclass bid pass-over headline in bubble | Core resolver only | `postseason.md#bubble-ui` |
| MEGA-POST-036 | GAP | No in-season projected bracket UI | Spec forbids — OK if absent; document N/A if product asks | design lock |

---

## Opening Round & NCBA main bracket (core)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-037 | SHIP | Opening pairing 1v24 … 12v13 by NET in bubble | `NcbaOpeningRoundPairing` + unit test | `NcbaOpeningRoundPairingTest.kt` |
| MEGA-POST-038 | SHIP | 12 opening winners → seeds 53–64 field merge | `NcbaFieldBuilder`, opening engine tests | `NcbaOpeningRoundEngine.kt` |
| MEGA-POST-039 | SHIP | S-curve four regions × 16 | `NcbaSCurveEngine` + tests | `NcbaSCurveEngineTest.kt` |
| MEGA-POST-040 | SHIP | Conference conflict swaps pre–Super 16 | `NcbaConferenceConflictAdjuster` | `NcbaConferenceConflictAdjuster.kt` |
| MEGA-POST-041 | SHIP | Parallel NCI / Heritage single-elim week batch | `NciHeritageBracketEngine`, `PostseasonWeekBatchEngine` | core tests |
| MEGA-POST-042 | SHIP | Universe round labels in calendar helper | Opening Round, Super 16, Great 8, Championship 4 | `PostseasonCalendar.ncbaRoundLabel` |
| MEGA-POST-043 | PARTIAL | User postseason game resolution | `PostseasonUserGameResolver` + provisional marks | end-to-end with null selection |
| MEGA-POST-044 | PARTIAL | Opening week index = 1 shared calendar | `OPENING_ROUND_WEEK = 1` aligns with time-system | NCI/HC parallel mapping in `PostseasonParallelCalendar` |

---

## March Mayhem hub (UI / frame)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-045 | PARTIAL | Hub visibility after Selection Sunday only | Menu shows entire `Postseason` phase, not “published selection” flag | `DynastyHubMenuPolicy.marchMayhemMenuVisible` |
| MEGA-POST-046 | GAP | Overview sub-tab (three rails + today's games) | UI tabs: NCBA / NCI / HC only — no Overview | `MarchMayhemHubScreen.kt` vs `postseason-frame.md` |
| MEGA-POST-047 | PARTIAL | Tournament seed labels `NCBA #n` | `MarchMayhemHubSnapshotFactory` builds `seedLabel` strings | verify all surfaces |
| MEGA-POST-048 | GAP | Full bracket tree visualization | List of field + scores — not region bracket UI | `MarchMayhemHubScreen.kt` |
| MEGA-POST-049 | GAP | Game tap → box score / user Sim | `AssistChip` + `Text` only; no navigation | `GameLine` composable |
| MEGA-POST-050 | GAP | My team only filter per sub-tab | Not present | `postseason-frame.md#interaction` |
| MEGA-POST-051 | GAP | Dashboard teaser → March Mayhem filtered to user event | Cross-audit dashboard postseason card | `game-frame.md` / frame audit |
| MEGA-POST-052 | PARTIAL | Opening Round dedicated strip | Text block when `openingSimmed` — not visual strip above main | `MarchMayhemHubScreen.kt` |
| MEGA-POST-053 | SHIP | Placeholder when selection unpublished | `march_mayhem_hub_placeholder` when `!selectionPublished` | `MarchMayhemHubScreen.kt` |
| MEGA-POST-054 | PARTIAL | March Mayhem brand subtitle NCBA · NCI · Heritage | Title string exists; subtitle not shown | strings / screen |

---

## Seed display & shell integration

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-055 | SHIP | Season review postseason finish strings | `SeasonReviewPostseasonLabel` NCBA/NCI/HC paths | `SeasonReviewPostseasonLabel.kt` |
| MEGA-POST-056 | GAP | Dashboard postseason row `NCBA #4 · East` | Not on dashboard home | frame audit MEGA-FRAME-036 |
| MEGA-POST-057 | GAP | School hub overview tournament lines in March | School UI path not audited with seed chips | `postseason-frame.md#display-rules` |
| MEGA-POST-058 | GAP | Game preview header `NCBA #7` vs `NCBA #10` | `GamePreviewScreen` lacks tournament seed prefix | ui grep |
| MEGA-POST-059 | GAP | Save shape `nationalPostseason{tournamentId, seed, regionId}` per team | Selection snapshot stores field entries — not per-team persist on roster/school | `postseason-frame.md` JSON example |
| MEGA-POST-060 | PARTIAL | News headlines use short tournament label | `NewsHeadlinePublisher` — verify upset copy uses NCBA/NCI/HC prefix | `NewsFeedRepository` |

---

## End of Season / season review

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-061 | SHIP | Phase after postseason week 7 | `CalendarPhase.SeasonReview` + `SeasonReviewGate` tests | `SeasonReviewGate.kt` |
| MEGA-POST-062 | SHIP | Review blocks: record, NET, polls, prestige, facilities, recruiting preview, coach | `SeasonReviewScrollContent` sections | `SeasonReviewContent.kt` |
| MEGA-POST-063 | PARTIAL | Team grades delta narrative | Facility grades shown; academic/program grade deltas thin | `SeasonReviewSnapshotFactory` |
| MEGA-POST-064 | PARTIAL | Awards block on review | `awardHighlights` list when non-empty — depends on awards wrap quality | `SeasonReviewContent.kt` |
| MEGA-POST-065 | GAP | History hooks (HOF class, GOAT refresh, records) on review screen | Season review lacks History chips / digest articles | `season-review.md#review-surfaces` |
| MEGA-POST-066 | GAP | League digest mini-articles (champion, NET riser, upset) | Not in UI | `season-review.md` |
| MEGA-POST-067 | GAP | Roster snapshot / departures preview block | Not on season review scroll | `season-review.md` |
| MEGA-POST-068 | SHIP | Read-only, Enter offseason CTA | `SeasonReviewScreen` advance pattern | `SeasonReviewScreen.kt` |

---

## Awards — engine & data

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-069 | PARTIAL | Team MVP from win contribution | `YearlyAwardsWrapEngine` uses `winSharesByPlayer` when provided | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-070 | GAP | `nationalAwardScore` composite + eligibility gates | Awards use NET champ / record heuristics + placeholders | `awards.md#national-awards` |
| MEGA-POST-071 | GAP | All-America 1st/2nd/3rd teams (45 players) | UI sub-tab empty; engine does not populate | `AwardsHubScreen.kt` AllAmerica → `emptyList()` |
| MEGA-POST-072 | PARTIAL | NCBA MVP runner-ups #2–#5 | Archive model supports lines; engine emits single runner | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-073 | PARTIAL | NCBA position POY PG–C | Placeholder names per position, not position pool scoring | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-074 | GAP | Conference awards: All-Conference teams | Only POY + COY by conf W–L — no All-Conference lists | `buildConferenceAwards` |
| MEGA-POST-075 | GAP | Conference POTY eligibility (top 4 / champ) | Standings sort only | `awards.md#conference-player-of-the-year` |
| MEGA-POST-076 | GAP | Coach of Year inputs (NET delta, postseason tier) | NCBA COY = best W–L; not spec formula | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-077 | GAP | Weekly POTW from sim (per conf + national) | `AwardsDemoCatalog.weeklyPotw` demo only | `AwardsWeeklyScreen.kt` |
| MEGA-POST-078 | GAP | `AwardWeekRow` persistence each RS week | No repository persist | `awards.md#archive--runner-ups` |
| MEGA-POST-079 | SHIP | Yearly current season locked before EoS | `AwardsHubPolicy.yearlyCurrentSeasonLocked` + UI message | `AwardsHubPolicy.kt` |
| MEGA-POST-080 | SHIP | Weekly awards live RS only | Policy test | `AwardsHubPolicyTest.kt` |

---

## Awards hub UI

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-081 | SHIP | Landing → Yearly / Weekly split | `AwardsHubRoute` | `AwardsHubScreen.kt` |
| MEGA-POST-082 | SHIP | Yearly NCBA vs Conference primary tabs | Implemented | `AwardsHubScreen.kt` |
| MEGA-POST-083 | SHIP | NCBA sub-tabs Overview / MVP / By position / All-America / Coaches | Scrollable row matches spec names | `YearlyNcbaSubTab` |
| MEGA-POST-084 | SHIP | Conference picker A→Z; default user conf | Dropdown + `userConferenceId` default | `AwardsYearlyScreen` |
| MEGA-POST-085 | PARTIAL | Prior seasons browsable archive | Two season chips only (current + prior); not full history list | season picker spec |
| MEGA-POST-086 | PARTIAL | Demo catalog when archive missing | `AwardsDemoCatalog` fills gaps — not dynasty truth | `AwardsHubScreen.kt` |
| MEGA-POST-087 | GAP | Tap player/coach → card preview | Text rows only | `awards.md#yearly-awards-screen` |
| MEGA-POST-088 | GAP | Conference sub-tab All-Conference | UI enum exists; content not wired | `YearlyConferenceSubTab.AllConference` |

---

## History, records, HOF, GOAT

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-089 | PARTIAL | End-of-season history finalize | `HistorySeasonFinalizeEngine` on user roster only — not league-wide year card inputs | `HistorySeasonFinalizeEngine.kt` |
| MEGA-POST-090 | SHIP | GOAT score formula shape | `GoatScoreEngine.score` uses balance weights matching planning ratios | `GoatScoreEngine.kt`, tests |
| MEGA-POST-091 | SHIP | GOAT overall top 25 list in hub | `HistoryHubScreen` GOAT tab | `HistoryHubScreen.kt` |
| MEGA-POST-092 | GAP | GOAT by position top 5 PG–C tab | Snapshot has `goatByPosition`; UI shows overall only | `HistoryHubScreen.kt` |
| MEGA-POST-093 | PARTIAL | Year cards: champion, MVP, HOF, GOAT #1 | Basic strips; missing **records** row | `history-hub.md#history-hub--year-card` |
| MEGA-POST-094 | GAP | National record book + program record book | No `RecordBookEntry` engine/UI | history grep — no matches |
| MEGA-POST-095 | GAP | After-game single-game record checks | Not wired | `history-hub.md#cadence` |
| MEGA-POST-096 | GAP | Program / Conference / National HOF triple lists | Only national HOF chip from GOAT threshold in finalize | `history-hub.md#hall-of-fame` |
| MEGA-POST-097 | GAP | School hub History / Records / HOF / Drafted tabs | No school history UI package | `ui/school` grep |
| MEGA-POST-098 | PARTIAL | Major record → notification feed | `MajorRecordFeedEligibility` exists — wiring to sim TBD | `MajorRecordFeedEligibility.kt` |
| MEGA-POST-099 | GAP | Player career archive on departure (portal/grad) | Finalize only at EoS on active roster | `history-hub.md#cadence` |
| MEGA-POST-100 | PARTIAL | `allAmericaPoints` for GOAT | History finalize does not increment from real All-America teams | depends on MEGA-POST-071 |

---

## Cross-cutting integration & tests

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-101 | SHIP | Core unit coverage selection + brackets | `SelectionSundayEngineTest`, opening/main/NCi tests | `android/core/src/test/...` |
| MEGA-POST-102 | SHIP | March Mayhem snapshot factory test | Builds seed labels from selection | `MarchMayhemHubSnapshotFactoryTest.kt` |
| MEGA-POST-103 | GAP | E2E dynasty: conf tourney → selection → 7 postseason weeks | Blocked by MEGA-POST-005/006 wiring | manual / integration gap |
| MEGA-POST-104 | PARTIAL | Balance bundle `selection.*` + `awards.*` + `history.goatWeights` | JSON present; awards/history engines partially consume | `balance-2026.json` |
| MEGA-POST-105 | PARTIAL | Notifications for user NCBA MVP / All-America | Policy in planning; feed composer not award-aware | `NotificationFeedComposer.kt` |
| MEGA-POST-106 | GAP | Win contribution rollup drives all player awards | `WinContributionEngine` in history finalize; not in national award scoring | `simulation-engine.md` cross-link |

---

## March Mayhem — depth (overview, rails, live state)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-107 | GAP | Overview sub-tab four-way tournament rails | Spec: mini progress for **NCBA · NCI · HC** + Opening strip on one screen | `postseason-frame.md#march-mayhem-hub` vs `MarchMayhemHubScreen.kt` (no Overview) |
| MEGA-POST-108 | GAP | Overview “today’s games” cross-event list | Chronological scores today for all three national events | `MarchMayhemHubSnapshot.kt` — no consolidated today slice |
| MEGA-POST-109 | GAP | Selection Sunday recap chip on Overview | One-line field summary after reveal | UI + `SelectionSundayModels` |
| MEGA-POST-110 | GAP | Hub pin when user alive in any bracket | Hamburger icon badge / pin per frame | `DynastyHubMenu.kt` |
| MEGA-POST-111 | GAP | Dashboard teaser opens hub on user event tab | Deep link `tournamentId` query on route | `MarchMayhemHubRouteScreen.kt`, frame MEGA-FRAME-036 |
| MEGA-POST-112 | PARTIAL | Hub visibility gated on `selectionPublished` | Placeholder when false; menu still uses whole `Postseason` phase | `DynastyHubMenuPolicy`, `MarchMayhemHubScreen` |
| MEGA-POST-113 | GAP | NCBA subtitle *NCBA · NCI · Heritage* under title | Brand lock subtitle missing | strings / `MarchMayhemHubScreen.kt` |
| MEGA-POST-114 | GAP | Week-aware round header per sub-tab | Super 16 / Great 8 label from `PostseasonCalendar.ncbaRoundLabel` | shell calendar gap MEGA-POST-003 |
| MEGA-POST-115 | GAP | Live score refresh after batch sim | Hub snapshot reload on week advance without manual back-nav | `MarchMayhemHubRepository.kt` |
| MEGA-POST-116 | PARTIAL | NCI five-round label ladder | Core `NciHeritageBracketEngine` round names — hub list may collapse rounds | core tests vs UI |
| MEGA-POST-117 | PARTIAL | Heritage four-round label ladder | Same as NCI for 16-team event | `postseason.md#national-heritage-classic` |
| MEGA-POST-118 | GAP | Eliminated team gray-out in hub | Visual state when user or AI out | not in `GameLine` |
| MEGA-POST-119 | GAP | Champion crown row when event completes | NCBA / NCI / HC winner banner per sub-tab | `postseason-frame.md` |
| MEGA-POST-120 | GAP | Parallel calendar week mapping surfaced | Same postseason week index for all three events | `PostseasonParallelCalendar` — UI copy only |
| MEGA-POST-121 | GAP | Opening Round strip visual (12 games) | Dedicated horizontal strip until 12 winners | text block only MEGA-POST-052 |
| MEGA-POST-122 | GAP | Opening winners promote to seeds 53–64 in UI | Show provisional → final seed line after sim | `NcbaOpeningRoundRepository` |
| MEGA-POST-123 | SHIP | Three national event IDs in snapshot model | NCBA / NCI / HC discriminated in hub DTO | `MarchMayhemHubSnapshot.kt` |
| MEGA-POST-124 | PARTIAL | Seed label `NCBA #n` on every game row | Factory sets `seedLabel` — verify NCI/HC prefix on all lines | `MarchMayhemHubSnapshotFactory` |

---

## Bracket UI (trees, regions, interaction)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-125 | GAP | Four-region NCBA bracket tree | 64-team S-curve visualization, not flat field list | `postseason-frame.md#march-mayhem-hub` |
| MEGA-POST-126 | GAP | Region tabs East / West / South / Midwest | `regionId` from selection persist on entrants | `SelectionSundayEngine`, save shape MEGA-POST-059 |
| MEGA-POST-127 | GAP | Round columns Round 1 → National Championship | Universe labels per `ncba-branding.md` | UI |
| MEGA-POST-128 | GAP | NCI 32-team single-elim tree | Five columns per spec rounds | `MarchMayhemHubScreen.kt` |
| MEGA-POST-129 | GAP | Heritage 16-team single-elim tree | Four columns | same |
| MEGA-POST-130 | GAP | Tap game → box score route | Same as RS postgame flow for user and AI games | `GameLine` — no `NavController` |
| MEGA-POST-131 | GAP | Tap game → Sim CTA when user team | Pre-game / live sim entry from bracket cell | `game-frame.md` |
| MEGA-POST-132 | GAP | Tap team → School hub read-only | Opponent school navigation from bracket | UI |
| MEGA-POST-133 | GAP | *My team only* filter per sub-tab | Toggle hides non-user paths | `postseason-frame.md#interaction` |
| MEGA-POST-134 | GAP | Bracket scroll sync to user next game | Auto-scroll to live tip on enter | UX |
| MEGA-POST-135 | PARTIAL | Scores on completed games | List shows scores when sim present — not in tree cells | snapshot factory |
| MEGA-POST-136 | GAP | Upcoming vs final game styling | Spec distinct typography for TBD tips | UI |
| MEGA-POST-137 | GAP | Conference conflict note (swap icon) | Optional badge when adjuster moved a team | `NcbaConferenceConflictAdjuster` |
| MEGA-POST-138 | SHIP | Opening pairing 1v24…12v13 in core | Engine + test | `NcbaOpeningRoundPairingTest.kt` |
| MEGA-POST-139 | GAP | Opening bracket mini-tree (12 slots) | Pairing visualization above main 64 | frame |
| MEGA-POST-140 | GAP | Conf tournament bracket overlay on Calendar | NET-only labels pre–Selection Sunday | MEGA-POST-014 |
| MEGA-POST-141 | GAP | School schedule conf bracket context | User path in conf tourney from School hub | `postseason-frame.md#conference-tournaments-ui` |

---

## Selection committee & Selection Sunday (product)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-142 | GAP | Wire `computeAndPersist` on conf tourney complete | Selection Sunday must run before postseason week 1 sim | `SelectionSundayRepository.kt`, MEGA-POST-005 |
| MEGA-POST-143 | GAP | Final NET freeze after 32 conf champions | One refresh on last conf final; cuts use frozen ranks | `selection-engine.md#selection-sunday--who-gets-in` |
| MEGA-POST-144 | GAP | Reclass-aware Heritage pool only | `heritageEligible` must honor `postseasonEligible: false` for NCI/NCBA exclusion | `SelectionSundayRepository.kt` L41 |
| MEGA-POST-145 | GAP | Flagship Selection Sunday reveal screen | 76 + 32 + 16 fields with `NCBA #` / `NCI #` / `HC #` | `postseason-frame.md#selection-sunday-moment` |
| MEGA-POST-146 | GAP | Bubble: last in/out NCBA direct (20-line) | Teams 20 vs 21 among non-champions | `postseason.md#bubble-ui` |
| MEGA-POST-147 | GAP | Bubble: Opening Round cut (24 teams) | Teams 44 vs 45 among non-champions | same |
| MEGA-POST-148 | GAP | Bubble: NCI 32 vs 33 | NET-order cut display | same |
| MEGA-POST-149 | GAP | Bubble: Heritage 16 vs 17 | Includes reclass in pool | same |
| MEGA-POST-150 | GAP | Reclass auto-bid pass-over headline | Runner-up receives bid; bubble explains flip | `NcbaAutoBidResolver`, news |
| MEGA-POST-151 | SHIP | 76-team NCBA bucket math in engine | 32 + 20 + 24 constants tested | `SelectionSundayEngineTest.kt` |
| MEGA-POST-152 | SHIP | Seeds 1–52 single Seed Index pool | Autos + direct at-large interleaved | `SelectionSundayEngine.assignSeeds` |
| MEGA-POST-153 | PARTIAL | NCI / HC Seed Index among invite lists | Engine assigns per-event seeds — UI reveal missing | `selection-engine.md#nci--heritage-seeds` |
| MEGA-POST-154 | GAP | Committee copy: NET-only resume (no polls) | Educational line on reveal — polls flavor only | `selection-engine.md#three-ranking-systems` |
| MEGA-POST-155 | GAP | News flagship article for Selection Sunday | One-time story unlocks March Mayhem | `news-hub.md` cross-audit |
| MEGA-POST-156 | GAP | No projected bracket in-season (forbid) | Confirm absent + document N/A guard if requested | `postseason.md` design lock |
| MEGA-POST-157 | PARTIAL | `nationalPostseason` save JSON per team | Snapshot on selection — per-team persist incomplete | MEGA-POST-059 |
| MEGA-POST-158 | GAP | Unlock March Mayhem menu on `selectionPublished` | Not merely `CalendarPhase.Postseason` | `DynastyHubMenuPolicy.kt` |
| MEGA-POST-159 | PARTIAL | Auto-bid from conf sim vs NET fallback | Repository path when conf sim missing | MEGA-POST-030 |

---

## Weekly honors (regular season)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-160 | GAP | Conference POTW each RS week (32 confs) | `PTS + 1.2×AST + REB` weekly score, min 1 game | `awards.md#weekly-honors` |
| MEGA-POST-161 | GAP | National POTW optional 1 per week | Best among conf winners or nationwide top | same |
| MEGA-POST-162 | GAP | `AwardWeekRow` persist each RS advance | Weekly archive entity | `awards.md#archive--runner-ups` |
| MEGA-POST-163 | GAP | Weekly engine hook on `LeagueWeekBatchRepository` | Not demo catalog only | `AwardsWeeklyScreen.kt` + batch |
| MEGA-POST-164 | SHIP | Weekly UI live RS only (policy) | `AwardsHubPolicy` blocks postseason weekly | `AwardsHubPolicyTest.kt` |
| MEGA-POST-165 | GAP | Week selector RS 1–32 scroll newest-first | Full history not just demo weeks | `AwardsWeeklyScreen.kt` |
| MEGA-POST-166 | GAP | Filter weekly list to user conference | Picker or default user conf | `awards.md#weekly-awards-screen` |
| MEGA-POST-167 | GAP | News headline on conf POTW only (no History card) | Cadence per `history-hub.md#cadence` | News composer |
| MEGA-POST-168 | PARTIAL | Weekly screen layout scaffold | Route exists; data from `AwardsDemoCatalog.weeklyPotw` | MEGA-POST-077 |
| MEGA-POST-169 | GAP | Weekly honors exclude win contribution | Spec: single-week box score formula only | engine design |

---

## Season review — carousel & blocks (full product)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-170 | PARTIAL | Single scroll vs tabbed carousel | `SeasonReviewScrollContent` single column — no horizontal chapter carousel | `season-review.md#review-surfaces` |
| MEGA-POST-171 | SHIP | Final rankings block NET + polls | Section in scroll content | `SeasonReviewContent.kt` |
| MEGA-POST-172 | SHIP | Your season line W–L + postseason label | `SeasonReviewPostseasonLabel` | `SeasonReviewPostseasonLabel.kt` |
| MEGA-POST-173 | PARTIAL | Team grades delta narrative | Facilities shown; academic/program deltas thin | MEGA-POST-063 |
| MEGA-POST-174 | PARTIAL | Program prestige start → end chips | Snapshot fields — reason chips incomplete | `SeasonReviewSnapshotFactory` |
| MEGA-POST-175 | GAP | Next season recruiting pool preview block | Prestige tier → weekly points preview | `season-review.md`, recruiting engine |
| MEGA-POST-176 | SHIP | Coach block prestige / job security | Section present | `SeasonReviewContent.kt` |
| MEGA-POST-177 | PARTIAL | Awards block Team MVP + national lines | `awardHighlights` when non-empty — engine quality | MEGA-POST-064 |
| MEGA-POST-178 | GAP | History hooks HOF class chips on review | Link to History hub inductees | `season-review.md#review-surfaces` |
| MEGA-POST-179 | GAP | GOAT refresh chip / drill-down from review | EoS recompute surfacing | `history-hub.md` |
| MEGA-POST-180 | GAP | National records set this year on review | Year strip parity with History card | `history-hub.md#history-hub--year-card` |
| MEGA-POST-181 | GAP | League digest mini-articles carousel | Champion, NET riser/faller, headline upset | `season-review.md` |
| MEGA-POST-182 | GAP | Roster snapshot departures preview | Grads known; link to offseason Week 1 | same |
| MEGA-POST-183 | SHIP | Read-only + Enter offseason CTA | No recruiting edits on screen | `SeasonReviewScreen.kt` |
| MEGA-POST-184 | SHIP | Phase gate after postseason week 7 | `SeasonReviewGate` | `SeasonReviewGate.kt` |
| MEGA-POST-185 | GAP | Horizontal pager indicators for review chapters | Optional UX lock for digest + grades + awards | product polish |

---

## History hub — tabs, records, HOF depth

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-186 | PARTIAL | History hub primary tabs | GOAT tab SHIP; missing Records / Year cards / HOF tabs | `HistoryHubScreen.kt` |
| MEGA-POST-187 | GAP | GOAT by position top 5 PG–C tab | `goatByPosition` in snapshot unused | MEGA-POST-092 |
| MEGA-POST-188 | GAP | Year cards stack newest-first | Collapsed champion / MVP / records / HOF / GOAT | `history-hub.md#history-hub--year-card` |
| MEGA-POST-189 | GAP | Year card Records strip | National records set that season | MEGA-POST-093 |
| MEGA-POST-190 | GAP | National record book browse UI | `RecordBookEntry` national scope | no engine/UI grep |
| MEGA-POST-191 | GAP | Program record book School → Records tab | PTS/REB/AST/STL/BLK/3PM scopes | `ui/school` |
| MEGA-POST-192 | GAP | After-game single-game record check | Running compare + News | `history-hub.md#cadence` |
| MEGA-POST-193 | GAP | National HOF class list (dynasty) | EoS induction pass UI | triple HOF MEGA-POST-096 |
| MEGA-POST-194 | GAP | Conference HOF list | Evaluated on departure | `history-hub.md#conference-hof` |
| MEGA-POST-195 | GAP | Program HOF School tab | Criteria 1800 PTS, 2× All-Conf 1st, etc. | same |
| MEGA-POST-196 | GAP | School hub Drafted alumni tab | NBA draft picks line | `history-hub.md#surfaces` |
| MEGA-POST-197 | PARTIAL | `GoatSnapshot` per season for drill-down | Engine scores — yearly frozen list UI partial | `GoatScoreEngine.kt` |
| MEGA-POST-198 | GAP | Major record → dashboard notification feed | `MajorRecordFeedEligibility` wiring | MEGA-POST-098 |
| MEGA-POST-199 | GAP | Player career archive on portal/grad departure | Not only EoS active roster finalize | `history-hub.md#cadence` |
| MEGA-POST-200 | GAP | Co-holder #1 display for tied records | Design lock ties allowed | record UI |
| MEGA-POST-201 | PARTIAL | League-wide `HistorySeasonFinalizeEngine` | User roster focus — league year card inputs thin | MEGA-POST-089 |
| MEGA-POST-202 | SHIP | GOAT formula weights in core | Matches planning ratios + tests | `GoatScoreEngine.kt` |
| MEGA-POST-203 | GAP | Season review → History hub deep links | Chips open year card / GOAT list | cross-surface |
| MEGA-POST-204 | PARTIAL | All-America → `allAmericaPoints` for GOAT | Blocked on real All-America teams MEGA-POST-071 | finalize chain |

---

## Awards product depth (yearly archive & surfaces)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-POST-205 | GAP | `nationalAwardScore` composite | Coefficients from `awards.nationalWeights` | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-206 | GAP | All-America 45 players 1st/2nd/3rd | Position buckets 2G 2F 1C per team | `AwardsHubScreen.kt` |
| MEGA-POST-207 | GAP | NCBA MVP runners #2–#5 display | Archive supports; UI single row | yearly MVP sub-tab |
| MEGA-POST-208 | GAP | NCBA position POY real pool scoring | Not placeholder names per position | `YearlyAwardsWrapEngine.kt` |
| MEGA-POST-209 | GAP | Conference All-Conference 1st/2nd/(3rd) teams | `confAwardScore` formula | `awards.md#all-conference-teams` |
| MEGA-POST-210 | GAP | Conference POTY eligibility top 4 or champ | Standings-only today | MEGA-POST-075 |
| MEGA-POST-211 | GAP | NCBA COY NET delta + postseason tier | W–L heuristic only | MEGA-POST-076 |
| MEGA-POST-212 | SHIP | Yearly NCBA vs Conference tabs + sub-tabs | Overview / MVP / position / All-America / Coaches | `AwardsHubScreen.kt` |
| MEGA-POST-213 | GAP | Full season picker archive (not two chips) | 2026–27, 2025–26, … | MEGA-POST-085 |
| MEGA-POST-214 | GAP | Tap award row → player/coach card preview | Text rows only | `awards.md#yearly-awards-screen` |
| MEGA-POST-215 | GAP | `AwardSeasonArchive` persist at EoS | Yearly freeze forever | data layer |
| MEGA-POST-216 | GAP | Conference All-Conference sub-tab content | Enum present, unwired | MEGA-POST-088 |
| MEGA-POST-217 | PARTIAL | School History award chips | Team MVP etc. — depends on awards engine | `awards.md#other-surfaces` |
| MEGA-POST-218 | GAP | Notification user NCBA MVP / All-America 1st | Feed composer award-aware | MEGA-POST-105 |

---

## Summary counts

| Status | Count |
|--------|------:|
| SHIP | 52 |
| PARTIAL | 47 |
| GAP | 119 |
| **Total items** | **218** |

**Priority clusters:** MEGA-POST-005/006/103/142 (Selection Sunday + postseason sim wiring), MEGA-POST-033–035/145–150 (reveal/bubble), MEGA-POST-046–050/107–133/125–131 (March Mayhem + bracket UX), MEGA-POST-070–078/205–211 (awards engine), MEGA-POST-094–099/186–204 (records/HOF/school history), MEGA-POST-160–169 (weekly honors), MEGA-POST-170–185 (season review carousel).

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial audit vs postseason, selection-engine, postseason-frame, awards, season-review, history-hub |
| 2026-11-11 | +112 rows (MEGA-POST-107–218): March Mayhem depth, bracket UI, selection committee, weekly honors, season review carousel, history tabs, awards product |

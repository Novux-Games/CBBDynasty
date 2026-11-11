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

## Summary counts

| Status | Count |
|--------|------:|
| SHIP | 40 |
| PARTIAL | 33 |
| GAP | 33 |
| **Total items** | **106** |

**Priority clusters:** MEGA-POST-005/006/103 (Selection Sunday + postseason sim wiring), MEGA-POST-033–035 (reveal/bubble), MEGA-POST-046–050 (March Mayhem UX), MEGA-POST-070–078 (awards engine), MEGA-POST-094–099 (records/HOF/school history).

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial audit vs postseason, selection-engine, postseason-frame, awards, season-review, history-hub |

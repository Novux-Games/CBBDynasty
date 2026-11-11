# MEGA audit — game frame, dashboard tasks, time system (offseason / preseason UI)

**Scope:** [`planning/game-frame.md`](../../planning/game-frame.md), [`planning/dashboard-tasks.md`](../../planning/dashboard-tasks.md), [`planning/time-system.md`](../../planning/time-system.md), [`planning/offseason.md`](../../planning/offseason.md), [`planning/preseason.md`](../../planning/preseason.md) vs Android Compose UI under `android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/`.  
**Audited:** 2026-11-11 · **Android shell:** `DynastyShellChrome`, `DynastyShellScreen`, `OffseasonScreen`, `PreseasonScreen`, hub routes.

**Legend**

| Tag | Meaning |
|-----|---------|
| **SHIP** | Matches spec in UI (or core wired from same screen) |
| **PARTIAL** | Present but incomplete vs design lock |
| **GAP** | Missing or wrong behavior |
| **N/A** | Not applicable to current phase routing |

---

## Entry & persistent chrome

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-001 | SHIP | Single dynasty shell all phases | Offseason, preseason, in-season, season review reuse `DynastyShellChrome` + four bottom tabs | `OffseasonScreen`, `PreseasonScreen`, `DynastyShellScreen`, `SeasonReviewScreen` |
| MEGA-FRAME-002 | SHIP | Bottom nav order Roster → Calendar → Dashboard → News | Tab order matches game-frame | `DynastyShellChrome.kt` |
| MEGA-FRAME-003 | SHIP | Dashboard default tab | Screens initialize `selectedTab = DynastyShellTab.Dashboard` | Shell screen `remember` state |
| MEGA-FRAME-004 | PARTIAL | Header: hamburger + school + phase/week | Top bar shows school + `CalendarWeekLabel`; menu is dropdown not slide drawer | `DynastyShellChrome.kt` |
| MEGA-FRAME-005 | GAP | No separate offseason/preseason app routes in career loop | Preseason still separate `AppRoute` composable without unified in-shell phase switch for all years | `DynastyApp.kt`, `DynastyLaunch.kt` |
| MEGA-FRAME-006 | SHIP | Coaching Carousel not “Week 0” | Carousel is `CoachingCarouselScreen` with title only — no week index in UI | `CoachingCarouselScreen.kt`, `carousel_title` |
| MEGA-FRAME-007 | GAP | Carousel uses non-shell chrome | Carousel screen is full-screen route, not hamburger + bottom nav shell | `CoachingCarouselScreen.kt` vs `game-frame.md#entry--shell` |

---

## Time — week advance only

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-008 | SHIP | No Advance day control | No UI string or button for day advance | repo grep; `game-frame.md#time-week-advance-only` |
| MEGA-FRAME-009 | SHIP | Week labels — exhibition 1–3 | `CalendarWeekLabel` → `Exhibition Week N` | `CalendarWeekLabel.kt` |
| MEGA-FRAME-010 | SHIP | Week labels — RS 1–32 | `Week N` for `RegularSeason` | `CalendarWeekLabel.kt` |
| MEGA-FRAME-011 | SHIP | Conf tournament week indices | `Conference Tournament Week N` | `CalendarWeekLabel.kt` |
| MEGA-FRAME-012 | SHIP | Postseason weeks 1–7 | `Postseason Week N` | `CalendarWeekLabel.kt`, `time-system.md#national-postseason-weeks` |
| MEGA-FRAME-013 | SHIP | End of Season label | `End of Season — {year}` | `CalendarWeekLabel.kt` |
| MEGA-FRAME-014 | SHIP | Offseason week header 1–10 | `Offseason Week N` | `CalendarWeekLabel.kt` |
| MEGA-FRAME-015 | PARTIAL | Offseason theme subtitles | Theme lines on dashboard (Players Leaving, etc.) — not duplicated in global header subtitle | `OffseasonWeekHeading`, `OffseasonScreen` |
| MEGA-FRAME-016 | GAP | NCBA round names on calendar | Postseason rows do not show Opening Round / Super 16 labels per `ncba-branding` | `DynastyShellCalendarTab.kt` |
| MEGA-FRAME-017 | PARTIAL | Calendar lists games by date | Rows are week-bucketed; game line has no calendar date field | `UserCalendarGameEntry`, `CalendarGameLine` |
| MEGA-FRAME-018 | SHIP | Bye week chip | `UserCalendarRowKind.Bye` shows bye chip | `DynastyShellCalendarTab.kt` |
| MEGA-FRAME-019 | GAP | Empty exhibition week advance UX | No dedicated “empty week” copy for exhibition slots with zero games | `time-system.md#exhibition-weeks` |

---

## Dashboard task gate & advance confirmation

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-020 | SHIP | Required tasks block advance | `DashboardAdvanceGate.blocksWeekAdvance` disables button | `DynastyShellScreen`, `OffseasonScreen` |
| MEGA-FRAME-021 | SHIP | Optional tasks do not block | In-season optional panel + advance still enabled when required clear | `DashboardOptionalTasksPanel`, gate tests |
| MEGA-FRAME-022 | SHIP | Sim-week confirmation dialog | `SimWeekAdvanceDialog` for fast-sim / recruiting / simple advance lines | `SimWeekAdvanceDialog.kt` |
| MEGA-FRAME-023 | PARTIAL | Confirmation copy intent | Lines exist but may not name opponent for unplayed game | `SimWeekConfirmationLineKind`, strings.xml |
| MEGA-FRAME-024 | GAP | Task cards Required vs Optional badge | Panels list tasks; no consistent Required/Optional chip per `game-frame.md#dashboard-task-gate` | `DashboardRequiredTasksPanel` |
| MEGA-FRAME-025 | SHIP | Preseason 8 required tasks | `PreseasonTaskId.entries` + `PreseasonStartGate` | `PreseasonScreen`, `dashboard-tasks.md#preseason` |
| MEGA-FRAME-026 | GAP | Preseason task deep links | Checklist rows use generic `PreseasonRequired`; limited per-task navigation from card | `PreseasonRequiredDashboardTasks.kt` |
| MEGA-FRAME-027 | SHIP | Preseason Start season gate | Button gated on all tasks complete (not “Advance week”) | `PreseasonScreen`, `preseason_start_season` |
| MEGA-FRAME-028 | PARTIAL | Offseason required by week | Signing Day, roster lock, carousel cards wired in core — UI coverage uneven by week | `OffseasonScreen`, `dashboard-tasks.md#offseason` |
| MEGA-FRAME-029 | GAP | Offseason Week 1 not on dashboard-only | Players Leaving is week panel — spec also allows dashboard task pattern for declare flows | `PlayersLeavingPanel` |
| MEGA-FRAME-030 | GAP | Coaching Carousel dashboard tasks | Offer/assistant cards on carousel screen — not dashboard checklist cards | `CoachingCarouselScreen` |
| MEGA-FRAME-031 | SHIP | In-season play/sim optional task | `DashboardTaskGate.inSeason` optional game tasks | `DynastyShellScreen` |
| MEGA-FRAME-032 | PARTIAL | Discipline required task | Panel hooks exist; end-to-end sheet flow depends on discipline route | `DashboardRequiredTasksPanel` |
| MEGA-FRAME-033 | GAP | End of Season required acknowledge | Season review advance — verify single required task UX vs season-review spec | `SeasonReviewScreen` |

---

## Dashboard (home) content

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-034 | GAP | Team overview card | No W–L, NET #, conf rank, T25, SOS on dashboard | `game-frame.md#team-overview` |
| MEGA-FRAME-035 | GAP | NCBA NET tap → Rankings | Dashboard lacks NET row / deep link | Rankings hub exists but not on dashboard |
| MEGA-FRAME-036 | GAP | Postseason seed row on dashboard | No `{NCBA\|NCI\|HC} #{seed}` when invited | `game-frame.md#postseason-row-dashboard` |
| MEGA-FRAME-037 | PARTIAL | Notifications feed | `DashboardNotificationFeedPanel` on in-season shell | Offseason/preseason use placeholder news, feed not always shown |
| MEGA-FRAME-038 | GAP | Last game summary | No opponent/score/W–L + box score / postgame actions on dashboard | `game-frame.md#last-game` |
| MEGA-FRAME-039 | PARTIAL | Next game card | `NextGameCard` with preview + opponent school — missing date/location line | `DynastyShellScreen.kt` `NextGameCard` |
| MEGA-FRAME-040 | SHIP | Game preview from next game | Opens `GamePreviewScreen` with win %, score, pregame article | `GamePreviewScreen.kt` |
| MEGA-FRAME-041 | PARTIAL | Game preview sections | Win %, predicted score, pregame report — team side-by-side / matchup history thin | `HeadToHeadHistory.summarize(emptyList())` in shell |
| MEGA-FRAME-042 | GAP | Primary CTA lineup/gameplan from preview | Strategy button on preview — verify prominent “set lineup” path | `GamePreviewScreen` tail |
| MEGA-FRAME-043 | GAP | Dashboard roster teaser vs spec | Shows 6-player preview cards, not team overview metrics | `DynastyShellScreen` roster preview block |

---

## Bottom tabs — Roster, Calendar, News

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-044 | GAP | Roster starters / reserves / bench | Flat sort by `depthOrder` — no section headers | `DynastyShellRosterTab.kt` |
| MEGA-FRAME-045 | PARTIAL | Roster tap → player card | In-season opens sheet; offseason/preseason `onPlayerClick = { }` | `OffseasonScreen`, `PreseasonScreen` |
| MEGA-FRAME-046 | PARTIAL | Calendar your games only | Week cards from `UserCalendarRepository` — scope OK | `DynastyShellCalendarTab.kt` |
| MEGA-FRAME-047 | GAP | Calendar conf vs non-conf distinction | Kind shown inline only — no section headers / badges per row | `gameKindLabel` chip text only |
| MEGA-FRAME-048 | GAP | Calendar tap → preview / box score | Game lines are non-clickable `Text` | `CalendarGameLine` |
| MEGA-FRAME-049 | PARTIAL | News tab shallow headlines | In-season `DynastyShellNewsTab` with headlines | Offseason/preseason: `DynastyShellNewsTabPlaceholder` |
| MEGA-FRAME-050 | GAP | News top N truncate policy | No explicit top-N cap vs News hub | `NewsFeedRepository` usage in shell |
| MEGA-FRAME-051 | GAP | News headline tap → detail / hub | Cards not clickable | `DynastyShellNewsTab` |

---

## Hamburger hubs & navigation

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-052 | SHIP | Core hub menu items | School, Recruiting, Strategy, Statistics, Rankings, News, History, Awards | `buildDynastyHubMenu` |
| MEGA-FRAME-053 | PARTIAL | March Mayhem visibility | Menu when `Postseason` phase — spec: after Selection Sunday only | `DynastyHubMenuPolicy.marchMayhemMenuVisible` |
| MEGA-FRAME-054 | SHIP | Conf tournament menu in phase | Extra item in conf tournament phase | `DynastyHubMenuPolicy` |
| MEGA-FRAME-055 | PARTIAL | Rankings hub 365 + polls | `RankingsHubScreen` tabs when `published` | Placeholder when unpublished |
| MEGA-FRAME-056 | GAP | Dashboard default after load | Launch routing splits Offseason / Preseason / Shell routes | `DynastyLaunch.kt` |

---

## Offseason UI (weeks & screens)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-057 | SHIP | Season 1 skip Week 10 | Policy in core + launch | `offseason.md#season-1-vs-later-seasons` |
| MEGA-FRAME-058 | SHIP | Week 1 Players Leaving panel | `PlayersLeavingPanel` on week 1 dashboard | `OffseasonScreen` |
| MEGA-FRAME-059 | PARTIAL | Week 1 Roster / Transfer tabs | Dedicated screen vs transfer-portal spec — audit transfer tab parity separately | `PlayersLeavingScreen.kt` |
| MEGA-FRAME-060 | SHIP | Week 2 draft results panel | `DraftResultsWeekTwoPanel` | `OffseasonScreen` week 2 |
| MEGA-FRAME-061 | PARTIAL | Weeks 2–5 portal optional tasks | `OffseasonOptionalDashboardTasks` + recruiting deep links | UI density on dashboard |
| MEGA-FRAME-062 | SHIP | Week 6 Signing Day required tasks | `SigningDayRequiredDashboardTasks` + signing screen route | `SigningDayScreen` |
| MEGA-FRAME-063 | PARTIAL | Week 7 summer workouts optional report | Core constants + repository; sheet UX thin on dashboard | `SummerWorkoutConstants` |
| MEGA-FRAME-064 | SHIP | Week 8 roster ≤15 gate | `RosterCutsWeekEightPanel` + advance gate | `OffseasonScreen` |
| MEGA-FRAME-065 | PARTIAL | Week 9 fundraising / upgrades | `FundraisingWeekNinePanel` | Optional tasks tuning |
| MEGA-FRAME-066 | SHIP | Week 10 conference offers Season 2+ | `ConferenceOffersWeekTenPanel` + advance block | `OffseasonScreen` |
| MEGA-FRAME-067 | PARTIAL | Advance to preseason CTA | Week 9→10→preseason transition via `SaveSlotRepository` — label “Advance to preseason” on final week | strings.xml |
| MEGA-FRAME-068 | GAP | Offseason sim-week on optional portal spend | Offseason advance confirmation wiring lighter than in-season | compare `OffseasonScreen` vs `DynastyShellScreen` |

---

## Preseason UI (single week)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-069 | SHIP | Single week Preseason label | Header `Preseason` | `CalendarWeekLabel` |
| MEGA-FRAME-070 | SHIP | Tasks 7–8 locked until invites | `preseason_task_locked_invites` + progress gate | `PreseasonScreen` |
| MEGA-FRAME-071 | PARTIAL | Task 1 Incoming class seed UX | Repository + progress; UI may be button-only vs “Run seed” progress | `IncomingClassSeedRepository` |
| MEGA-FRAME-072 | PARTIAL | Task 4 NET + polls review sheet | Rankings sheet — confirm Continue dismiss | preseason rankings flow |
| MEGA-FRAME-073 | SHIP | Task 5 preseason report article | `PreseasonReportPanel` + `ReportArticleView` pattern | `preseason.md#view-preseason-report` |
| MEGA-FRAME-074 | PARTIAL | Task 6 tournament invites prestige tiers | `TournamentInvitesState` — UI cards for multi-invite | `PreseasonScreen` |
| MEGA-FRAME-075 | PARTIAL | Task 7 exhibitions 0–3 / skip | Strings + schedule repo | Builder UX |
| MEGA-FRAME-076 | PARTIAL | Task 8 RS schedule builder | Solver + validation in core; UI shows summary / build | `PreseasonScheduleSolver` |
| MEGA-FRAME-077 | GAP | Preseason shell News tab live feed | Placeholder only — Top 60 headline should surface post seed | `PreseasonScreen` News tab |
| MEGA-FRAME-078 | SHIP | Start season → first playable focus | `PreseasonSeasonStartPolicy` + exhibition vs RS | `SaveSlotRepository.completePreseason` |

---

## Cross-cutting time & advance behavior

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-079 | SHIP | Bye week advance allowed | `ByeWeekPolicy.canAdvanceWeek` always true in-season | core tests |
| MEGA-FRAME-080 | PARTIAL | Injury weeks tick on bye advance | Confirmation line `InjuryWeeksTick` | `SimWeekAdvanceDialog` |
| MEGA-FRAME-081 | GAP | Early-event bye weeks on calendar | Calendar may not mark eliminated-team event bye weeks distinctly | `time-system.md#byes-after-elimination` |
| MEGA-FRAME-082 | GAP | Conf tourney shorter-bracket bye | Cross-conference bye weeks not called out in UI | `time-system.md#conference-tournaments` |
| MEGA-FRAME-083 | PARTIAL | Week advance league batch sim | Repository advances checkpoint — full league batch visibility N/A in UI | `SaveSlotRepository.advanceDynastyWeek` |
| MEGA-FRAME-084 | GAP | Season review week no bottom-tab games | Season review screen — confirm calendar empty / copy | `SeasonReviewScreen` |

---

## Summary counts

| Status | Count |
|--------|------:|
| SHIP | 30 |
| PARTIAL | 28 |
| GAP | 26 |
| **Total items** | **84** |

**Priority clusters for frame/dashboard/time:** MEGA-FRAME-034–038 (dashboard home), MEGA-FRAME-044–051 (tabs), MEGA-FRAME-007/005 (shell unity), MEGA-FRAME-016–017/048 (calendar polish), MEGA-FRAME-077 (preseason news).

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial UI audit vs game-frame, dashboard-tasks, time-system, offseason, preseason planning docs |

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

## User-reported regressions (dashboard / advance)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-085 | PASS | **Bug: tasks disappear (preseason)** | Preseason dashboard uses single **LazyColumn** checklist (done + remaining); advance gate still uses incomplete-only required tasks | `PreseasonScreen.kt` |
| MEGA-FRAME-086 | PASS | Preseason progress loading UX | Dashboard shows skeleton while `PreseasonTaskProgress` loads; shell chrome remains visible | `PreseasonScreen.kt` |
| MEGA-FRAME-087 | PASS | **Bug: tasks disappear (dual list)** | Removed duplicate `DashboardRequiredTasksPanel` on preseason dashboard — one checklist surface | `PreseasonScreen.kt` |
| MEGA-FRAME-088 | PASS | **Bug: tasks disappear (spurious complete)** | Workouts/coach/rankings rows use feature panels; generic Complete removed; rankings publish via hub Continue | `PreseasonScreen.kt`, `PreseasonFeatureTaskPanels.kt`, `RankingsHubRepository.publishPreseasonRankings` |
| MEGA-FRAME-089 | GAP | **Bug: recruiting UI after Signing Day** | Offseason **Weeks 7–10** still show **Open Recruiting hub** CTA when `hubAccessible` (week ≥ 2); spec Week 6 **closes cycle** — administration weeks feel like portal crunch | `OffseasonScreen.kt`, `RecruitingCyclePolicy.hubAccessible` |
| MEGA-FRAME-090 | PASS | **Bug: recruiting between signing & preseason** | Hamburger **Recruiting** gated on `cycleClosed` — closed label + toast or `RecruitingCycleClosedBody` on route | `buildDynastyHubMenu`, `RecruitingCyclePolicy.hubNavigable` |
| MEGA-FRAME-091 | PASS | Sim week advance loading UX | Advance button and confirm dialog show spinner + disable while suspend advance runs | `DynastyShellScreen.kt`, `OffseasonScreen.kt` |
| MEGA-FRAME-092 | PASS | Sim week confirm loading UX | Dialog confirm shows spinner; dismiss blocked while advancing | `SimWeekAdvanceDialog.kt` |
| MEGA-FRAME-093 | GAP | **Bug: sim week lag (pre-dialog)** | `buildSimWeekAdvanceConfirmation` loads ledger + builds AI estimate **synchronously** on first tap — perceived lag **before** dialog appears | `SaveSlotRepository.buildSimWeekAdvanceInputSuspended` |
| MEGA-FRAME-094 | GAP | **Bug: tasks disappear (portal weeks)** | Offseason optional portal/recruiting cards only render for **`PORTAL_RECRUITING_WEEKS`** — advancing to Week 6+ **removes entire optional block** abruptly | `OffseasonScreen.kt` weeks 2–5 only |

---

## Dashboard task catalog & persistence

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-095 | GAP | In-season recruiting optional tasks | `DashboardTaskGate.inSeason` never emits `SpendRecruitingPoints` / watchlist nudges — year-long optional recruiting missing on shell dashboard | `DashboardTaskGate.kt`, `InSeasonOptionalDashboardTasks.kt` |
| MEGA-FRAME-096 | GAP | Shell recruiting ledger | `DynastyShellScreen` does not load `RecruitingEconomyRepository` for task counts or sim-week unresolved recruiting line | `DynastyShellScreen.kt` |
| MEGA-FRAME-097 | GAP | Required / Optional chips | Task cards lack visible **Required** vs **Optional** labels per `game-frame.md#dashboard-task-gate` (extends MEGA-FRAME-024) | `DashboardRequiredTasksPanel`, `DashboardOptionalTasksPanel` |
| MEGA-FRAME-098 | GAP | Preseason required deep link dead | `DashboardRequiredTasksPanel.onOpenSigningDay` **no-op** on `PreseasonScreen` — **Open** button does not scroll/focus lazy task row | `PreseasonScreen.kt` `onOpenSigningDay = { }` |
| MEGA-FRAME-099 | GAP | `PreseasonChecklist` deep link | All eight preseason tasks share `DashboardTaskId.PreseasonRequired` — cannot distinguish cards in analytics or dismiss sets | `PreseasonRequiredDashboardTasks.kt` |
| MEGA-FRAME-100 | PARTIAL | Optional dismiss scope | `dismissedIds` keyed by `DashboardTaskId` only — multiple injury weeks / portal rows share ids | `DashboardOptionalTasksPanel`, `DynastyShellScreen` |
| MEGA-FRAME-101 | GAP | Offseason dismiss persistence | `dismissedTasks` is `remember` only — not saved; week advance does not reset but **process death** resurrects nags user dismissed | `OffseasonScreen.kt` |
| MEGA-FRAME-102 | GAP | Week 1 required dashboard pattern | Declare / pro-day flows live on **Players Leaving** screen — no mirrored **required** dashboard cards per `dashboard-tasks.md#offseason` | `PlayersLeavingPanel` vs dashboard |
| MEGA-FRAME-103 | SHIP | Required blocks advance core | `DashboardAdvanceGate.blocksWeekAdvance` used on shell, offseason, preseason | `DashboardAdvanceGate.kt` |
| MEGA-FRAME-104 | GAP | Discipline task visibility | Pending discipline blocks advance in repository but task panel depends on `DisciplineRequiredDashboardTasks` wiring from load | `SaveSlotRepository.advanceDynastyWeek` early return |
| MEGA-FRAME-105 | GAP | Signing Day panel null race | Week 6 required panel gated on `signingProgress?.let` — null progress hides **all** signing tasks until `LaunchedEffect` finishes | `OffseasonScreen.kt` |
| MEGA-FRAME-106 | PARTIAL | In-season optional game task | Only **first** scheduled game becomes optional task when multiple user games same week | `InSeasonOptionalDashboardTasks.build` |

---

## Sim-week advance pipeline & confirmation

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-107 | PARTIAL | Unplayed game copy | `UnplayedGameFastSim` confirmation string is generic — **opponent name** not passed from `ScheduledGameStub` | `SimWeekAdvanceDialog.kt`, `SimWeekConfirmationLineKind` |
| MEGA-FRAME-108 | GAP | Offseason lightweight confirm | Offseason advance often skips dialog when `unresolvedOptionalTaskCount == 0` even crossing **Signing Day** / roster lock — no “Advance to Offseason Week N?” | `OffseasonScreen.kt`, `SimWeekAdvanceResolver` |
| MEGA-FRAME-109 | PARTIAL | Portal line on advance | `PortalRecruitingWeek` confirmation line when portal window open — OK; **absent** after Week 5 | `SimWeekAdvanceResolver.kt` |
| MEGA-FRAME-110 | GAP | Manual recruiting spend param | UI always calls `advanceDynastyWeek(slot)` with default **`userManualRecruitingSpent = 0`** — no hook from Recruiting hub | `DynastyShellScreen.kt`, `OffseasonScreen.kt` |
| MEGA-FRAME-111 | GAP | Advance failure UX | Failed `DynastyLoadResult` paths use `else -> Unit` — user gets **no toast** on discipline block or load failure | Shell/offseason advance handlers |
| MEGA-FRAME-112 | PARTIAL | Injury tick on bye | `InjuryWeeksTick` line emitted in resolver — calendar bye weeks may not explain tick in UI | `SimWeekAdvanceDialog`, `ByeWeekPolicy` |
| MEGA-FRAME-113 | GAP | Double-tap advance | No **debounce** / `enabled = false` while `scope.launch` advance in flight — duplicate week commits risk | `DynastyShellScreen` advance `Button` |
| MEGA-FRAME-114 | GAP | Advance work ordering | `advanceDynastyWeek` chains checkpoint, **AI recruiting**, league batch, injuries, fatigue, chemistry, coach Sunday — all on confirm path (lag source) | `SaveSlotRepository.advanceDynastyWeek` |
| MEGA-FRAME-115 | PARTIAL | Simple advance line | Short “Advance to {label}?” exists when no optional work — in-season still may skip if `needsDialog` false | `SimWeekAdvanceResolver.buildConfirmation` |
| MEGA-FRAME-116 | GAP | Sim-week vs Start season | Preseason uses **Start season** CTA only — no sim-week dialog (correct) but shares heavy `startSeason` repository path without progress UI | `PreseasonSeasonStartRepository` |

---

## Offseason dashboard by week (shell body)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-117 | GAP | Weeks 3–5 dashboard body | Only optional task panel + placeholder hint — no week theme recap / draft crunch copy on dashboard | `offseason_shell_placeholder`, `OffseasonWeekHeading` |
| MEGA-FRAME-118 | PARTIAL | Theme line placement | `OffseasonWeekHeading.themeLine` on dashboard column — **not** in global `DynastyShellChrome` subtitle (extends MEGA-FRAME-015) | `OffseasonScreen.kt` |
| MEGA-FRAME-119 | GAP | Week 7 workout optional task | Summer workouts auto-run in core; **optional “review report”** dashboard card not surfaced (spec `offseason.md` Week 7) | `SummerWorkoutReportPanel` only on week 7 body |
| MEGA-FRAME-120 | PARTIAL | Week 8 scholarship gate | Advance enabled when `rosterPlayerCount <= 15` — may not match **scholarship** subset vs walk-ons | `OffseasonScreen` `SCHOLARSHIP_LIMIT` |
| MEGA-FRAME-121 | PARTIAL | Week 9 fundraising tasks | `FundraisingWeekNinePanel` on dashboard — optional fundraising **dashboard task cards** thin vs spec table | `dashboard-tasks.md#offseason` |
| MEGA-FRAME-122 | GAP | Week 10 → preseason CTA | Final week button label / routing via `SaveSlotRepository` — verify copy “Advance to preseason” after Week 9 (S1) vs Week 10 | `OffseasonWeekAdvancePolicy`, strings |
| MEGA-FRAME-123 | GAP | Offseason calendar tab | `DynastyShellCalendarTab` during offseason likely **empty** — no “no games this phase” copy | `UserCalendarRepository` + shell tabs |
| MEGA-FRAME-124 | GAP | Week 2 draft results + tasks | `DraftResultsWeekTwoPanel` present — optional **open scholarships** / portal tasks depend on context flags from `LaunchedEffect` | `OffseasonScreen` context builder |
| MEGA-FRAME-125 | GAP | Signing Day screen vs dashboard | Full **SigningDayScreen** route separate from shell — required tasks duplicated between screen and Week 6 panel | `SigningDayScreen.kt`, `AppRoute` |
| MEGA-FRAME-126 | PARTIAL | Recruiting hub offseason W1 | Policy blocks hub Week 1 — **correct** for portal; shared points economy still inaccessible Week 1 via hub | `RecruitingCyclePolicy` week >= 2 |
| MEGA-FRAME-127 | GAP | Close cycle UX after hard sign | `closeCycleAfterHardSign` runs on screen — hub CTA on Weeks 7+ should hide or show “administration only” | `SigningDayScreen`, `OffseasonScreen` |

---

## Preseason gate, checklist & transition

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-128 | GAP | Top 60 → News tab | Task 1 publishes seed — **News tab** remains `DynastyShellNewsTabPlaceholder` — Top 60 headline not on shallow feed | `PreseasonScreen` News tab |
| MEGA-FRAME-129 | SHIP | Tasks 7–8 invite gate | Locked copy + `schedulingUnlocked()` matches `preseason.md` dependency on task 6 | `PreseasonScreen.kt` |
| MEGA-FRAME-130 | GAP | Start season navigation | `onSeasonStarted()` pops NavHost — **not** in-place phase swap inside one shell route | `DynastyApp.kt`, `DynastyLaunch.kt` |
| MEGA-FRAME-131 | PARTIAL | Task order UX | Spec allows tasks 1–5 in any order — UI lists fixed enum order only | `PreseasonTaskId.entries` |
| MEGA-FRAME-132 | GAP | Coach upgrades wiring | Task 3 completable via generic button — **skill tree** screen not required path | `PreseasonScreen` vs `game-design` coach upgrades |
| MEGA-FRAME-133 | PARTIAL | Rankings review task | NET/Media/Coaches sheet exists in flow — completion not tied to **Continue** on rankings slice only | `RankingsHubScreen` / preseason |
| MEGA-FRAME-134 | PARTIAL | Exhibition / RS builders | Dedicated panels for tasks 7–8 when unlocked — good; validation errors stay in builder not on task card | `ExhibitionSchedulePanel`, `RegularSeasonSchedulePanel` |
| MEGA-FRAME-135 | GAP | Preseason header hint | Header shows **Preseason** label only — no “complete all tasks to start season” in chrome | `CalendarWeekLabel`, `DynastyShellChrome` |
| MEGA-FRAME-136 | GAP | Redshirt in task 2 | Optional redshirt during workouts — not surfaced on generic workout complete row | `preseason.md` redshirt note |
| MEGA-FRAME-137 | SHIP | Eight-task gate to start | `PreseasonStartGate.canStartSeason` + `DashboardAdvanceGate` on start button | `PreseasonScreen.kt` |

---

## Phase routing, hubs & shell parity

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-138 | GAP | Multi-route career loop | `DynastyLaunch` maps phase → **distinct** `AppRoute` composables — not unified shell with `calendarPhase` swap only | `DynastyLaunch.kt` (extends MEGA-FRAME-005) |
| MEGA-FRAME-139 | GAP | Hub back stack / tab reset | Returning from Recruiting/SigningDay hubs does not force **Dashboard** tab restore | NavHost patterns in `DynastyApp.kt` |
| MEGA-FRAME-140 | PARTIAL | Season review chrome | `SeasonReviewScreen` separate from four-tab shell — partial spec compliance for review week | `SeasonReviewScreen.kt` |
| MEGA-FRAME-141 | GAP | Players Leaving routing | Week 1 retention may be full-screen route vs in-shell panel — breaks “one chrome” mental model | `PlayersLeavingScreen.kt` |
| MEGA-FRAME-142 | GAP | Recruiting hub during preseason | `hubAccessible(Preseason) == true` — placeholder HS/Portal tabs during setup week (user confusion with signing) | `RecruitingCyclePolicy`, `RecruitingHubScreen` |
| MEGA-FRAME-143 | PARTIAL | Carousel → Week 1 handoff | Carousel exit navigates to Offseason route — shell tabs present but carousel itself non-shell | `CoachingCarouselScreen.kt` |
| MEGA-FRAME-144 | GAP | Job center unemployed | Unemployed coaches land **JobCenter** — outside dynasty shell until employed | `DynastyLaunch.kt` |
| MEGA-FRAME-145 | PARTIAL | Awards hub shell access | Menu includes Awards — opens hub overlay; bottom nav persists (implementation choice OK) | `buildDynastyHubMenu` |

---

## Time labels, calendar & bye semantics

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-146 | GAP | Conf tournament week N in header | `CalendarWeekLabel` has conf tournament strings — verify **N** tracks pack max rounds in live save | `CalendarWeekLabel.kt`, `time-system.md#conference-tournaments` |
| MEGA-FRAME-147 | GAP | Postseason round labels | Dashboard calendar rows lack **Opening Round / Super 16** copy (extends MEGA-FRAME-016) | `DynastyShellCalendarTab.kt`, `ncba-branding.md` |
| MEGA-FRAME-148 | GAP | Exhibition empty week copy | No dedicated empty-slot message when 0 exhibitions in a window | `time-system.md#exhibition-weeks` |
| MEGA-FRAME-149 | GAP | Event bye after elimination | Eliminated early-event teams’ bye weeks not visually distinct from generic empty week | `time-system.md#byes-after-elimination` |
| MEGA-FRAME-150 | PARTIAL | Preseason single-week label | `CalendarWeekLabel` → “Preseason” — does not echo “setup week” in calendar tab | `CalendarWeekLabel.kt` |
| MEGA-FRAME-151 | GAP | End of Season vs review | Label **End of Season — {year}** exists — season review screen phase copy alignment check | `SeasonReviewGate`, `CalendarWeekLabel` |
| MEGA-FRAME-152 | PARTIAL | RS week 1–32 batching | UI week index matches save checkpoint — league-wide batch invisible (expected) | `WeekCheckpointRepository.advanceWeek` |
| MEGA-FRAME-153 | GAP | NCBA top-52 bye week 1 | Postseason week 1 bye for auto-bids not annotated on user calendar | `time-system.md#national-postseason-weeks` |

---

## Notifications, feeds & advance feedback

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-FRAME-154 | GAP | Offseason/preseason notification feed | `DashboardNotificationFeedPanel` **in-season only** — offseason/preseason miss in-app injury/commit nudges on home tab | `DynastyShellScreen.kt` vs phase routes |
| MEGA-FRAME-155 | PARTIAL | Session refresh after advance | `ActiveDynastySession.set` on success — child tabs may stale until manual recomposition triggers | Shell `loaded` state |
| MEGA-FRAME-156 | PASS | Hub Recruiting when cycle closed | Menu shows **Recruiting (cycle closed)** with explanation toast when ledger `cycleClosed` | `buildDynastyHubMenu`, `notifyRecruitingCycleClosed` |
| MEGA-FRAME-157 | SHIP | No Advance day control | Reconfirmed — no day-advance UI anywhere in shell/offseason/preseason | repo grep |
| MEGA-FRAME-158 | GAP | Checkpoint transparency | Week advance commits save — **no** “Week saved” / sync indicator after long advance | UX gap tied to MEGA-FRAME-091 |

---

## Summary counts

| Status | Count |
|--------|------:|
| SHIP | 36 |
| PARTIAL | 45 |
| GAP | 77 |
| **Total items** | **158** |

**New rows this pass:** **74** (`MEGA-FRAME-085` … `MEGA-FRAME-158`).

| Metric | Value |
|--------|------:|
| Prior total (2026-11-11 initial) | 84 |
| **User-reported bug rows** | 10 |
| **GAP** (all rows) | 77 (48.7%) |
| **PARTIAL** | 45 (28.5%) |
| **SHIP** | 36 (22.8%) |

**Priority clusters for frame/dashboard/time:** MEGA-FRAME-085–094 (user bugs: tasks, recruiting gap, sim-week lag), MEGA-FRAME-034–038 (dashboard home), MEGA-FRAME-095–096 (in-season recruiting tasks), MEGA-FRAME-091–094 / 113–114 (advance perf UX), MEGA-FRAME-007/005/138 (shell unity), MEGA-FRAME-089–090/127/156 (recruiting hub after signing), MEGA-FRAME-016–017/147–148 (calendar polish), MEGA-FRAME-077/128 (preseason news).

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial UI audit vs game-frame, dashboard-tasks, time-system, offseason, preseason planning docs |
| 2026-11-11 | +74 rows (085–158): user-reported tasks/recruiting/sim-lag bugs; dashboard persistence; sim-week pipeline; offseason-by-week; preseason transition; routing; calendar/byes; notifications |

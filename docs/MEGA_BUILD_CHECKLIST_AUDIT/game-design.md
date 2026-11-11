# MEGA build checklist audit — `planning/game-design.md`

**Source GDD:** [`planning/game-design.md`](../../planning/game-design.md) (read in full, 2026-11-09)  
**Code scope:** Deep grep + file review under [`android/`](../../android/) (`:app`, `:core`, `:database`)  
**Audit date:** 2026-11-11  
**Legend — Status:** `Done` = matches GDD intent in prod path; `Partial` = engine or thin UI; `Missing` = no meaningful implementation; `N/A` = explicit non-goal  

| ID | Planning | Expected (GDD) | Code | Status | Gap |
|----|----------|----------------|------|--------|-----|
| MEGA-GDD-001 | Vision & pillars · Vision | Text-based **2D** offline men's CBB dynasty on **Android** | `ui/DynastyApp.kt`, `PossessionSimEngine`, `GameWatchScreen` | Done | — |
| MEGA-GDD-002 | Vision · Scale pillar | **365** D-I programs in default league graph | `embedded-default.json`, `DefaultPackAssetTest`, `LeagueWeekBatchPerformanceBudget.FULL_LEAGUE_TEAM_COUNT` | Done | — |
| MEGA-GDD-003 | Vision · Reclass | **4** D-II→D-I reclass programs; **no NCBA/NCI** until transition | Pack `postseasonEligible: false` (×4); `NcbaAutoBidResolver`, `ConferenceTournamentRepository` | Done | Heritage/conf tourney eligibility per postseason spec—verify UX copy on reclass schools |
| MEGA-GDD-004 | Core loop | OFFSEASON → PRESEASON → RS → POSTSEASON → year rollover | `calendar/PhaseWeekPolicy`, `WeekCheckpointRepository`, `CalendarPhase` | Done | — |
| MEGA-GDD-005 | Non-goals | No real-time on-court player control (prep + sim only) | `StrategyScreen`, `GameWatchScreen`, `PossessionSimEngine` | Done | — |
| MEGA-GDD-006 | Non-goals | **Offline** local saves; no cloud / multiplayer / always-on net | `DynastyDatabase`, `SaveSlotRepository`; no cloud SDK usage | Done | Pack **URL** fetch is optional online import only |
| MEGA-GDD-007 | Non-goals | No licensed NCAA trademarks / real likenesses in base APK | `PlayStoreListingCopy`, generic pack names, `CreditsScreen` | Done | User-imported packs remain user responsibility |
| MEGA-GDD-008 | Simulation (brief) | **40:00** clock; possession time bands; full box score | `GameSimClock`, `PossessionSimEngine`, `BoxScoreCoherence` | Done | — |
| MEGA-GDD-009 | Simulation | User **lineup / rotation / gameplan** before tip; **halftime** adjust | `GameLineupPrep`, `StrategyScreen`, `GameWatchLaunch` halftime hook | Partial | Away rotation / full rotation UI depth below player-cards spec |
| MEGA-GDD-010 | Simulation | **Watch** ~2s/possession; **fast sim** same engine | `GameWatchPlayback`, `LeagueFastSim` | Done | — |
| MEGA-GDD-011 | World scale · Week advance | **Advance week** commits save; batch sim league games that week | `SaveSlotRepository.advanceWeek`, `LeagueWeekBatchSimEngine` | Done | — |
| MEGA-GDD-012 | World scale · Replays | Re-sim user games **before** week advance; locked after | `WeekCheckpointState.canReplay`, `provisionalGames` in checkpoint | Partial | App-layer re-watch path not fully wired for every calendar entry point |
| MEGA-GDD-013 | World scale · Dashboard gate | **Required** dashboard tasks block advance; optional warn on sim-week | `DashboardAdvanceGate`, `DashboardTaskGate`, `SimWeekAdvanceDialog` | Done | — |
| MEGA-GDD-014 | Identity packs | Default generic ship; **opaque IDs**; display in pack overlay | `PackRepository`, `IdentityPackIndex`, `embedded-default.json` | Done | — |
| MEGA-GDD-015 | Identity packs · Import | Local **JSON/TXT** + remote **HTTPS URL**; atomic validation | `IdentityPackImportPipeline`, `PackRepository.import*` | Done | — |
| MEGA-GDD-016 | Identity packs · Legal UX | Import attestation + unofficial disclaimer | `PackImportAttestation`, `PackImportError.AttestationRequired`, pipeline tests | Partial | **New-game wizard** still shows `new_settings_import_stub`—no in-wizard import UI |
| MEGA-GDD-017 | Identity packs · Legal UX | URL **domain allowlist**, **5 MB** cap | `PackUrlPolicy`, `PackImportConstants`, `PackPayloadPolicy` | Done | — |
| MEGA-GDD-018 | Identity packs · Legal UX | Optional pack manifest **signature** verification | — | Missing | GDD remaining decision; only comment in `PackConstants.kt` |
| MEGA-GDD-019 | Identity packs · Swap | Pack swap never rewrites dynasty history; rebuild display cache | `PackOverlaySession`, save uses IDs; `IdentityPackOverlayMerger` | Partial | Post-swap non-blocking year mismatch warning not located in app UI |
| MEGA-GDD-020 | World scale · Ruleset | `rulesetYear` + balance/sim **pinned at dynasty create** | ADR-002/003; `BalanceRuleset` on metadata; `SimTuningCodec` | Done | — |
| MEGA-GDD-021 | Roster | **15** roster max; ≤15 scholarships; walk-ons allowed | `GameConstants`, `RosterLockPolicy`, `OffseasonRosterLockConstants` | Done | — |
| MEGA-GDD-022 | Roster · Walk-ons | OS tryouts; home-state pool; ≤2 new walk-ons/year | `WalkOnFactory`, `RosterCutsWeekEightPanel` (UI week mapping) | Partial | GDD text says OS week 7 tryouts; code modules under `offseason/weekeight/`—aligned to [`offseason.md`](../../planning/offseason.md) not stale GDD table |
| MEGA-GDD-023 | Roster · Redshirt | Standard + **medical** RS rules | `MedicalRedshirtPolicy`, injury week advance | Partial | Five-year clock / “development year” copy on cards not fully surfaced |
| MEGA-GDD-024 | Roster · Events | Grads, portal, cuts, **early draft** + **coach promises** | `BalanceLoyaltyTuning`, `PortalWeekOnePartition`, `PlayerLoyaltyChips` | Partial | Declare-intent / pro-day / withdrawal UI thinner than loyalty spec |
| MEGA-GDD-025 | Offseason (GDD summary) | Weeks 1–10 phases per authoritative calendar | `OffseasonWeekHeading`, `weekone`…`weekten` packages, `OffseasonScreen` | Partial | **game-design.md** offseason **summary table is stale** (week 6–9 labels); code tracks [`offseason.md`](../../planning/offseason.md) |
| MEGA-GDD-026 | Offseason · Carousel | Season 2+ **coaching carousel** before OS week 1 | `CoachingCarouselEngine`, `CoachingCarouselScreen`, policy tests | Partial | `stubUserApprovalCards()`—not full league carousel AI from GDD behavior table |
| MEGA-GDD-027 | Recruiting | Year-long cycle; prestige **weekly points**; HS/JUCO pool scale | `RecruitingEconomyRepository`, `AiRecruitingRepository`, balance `recruiting` | Partial | Core economy wired; **RecruitingHubScreen** HS/JUCO + portal tabs are **placeholders** |
| MEGA-GDD-028 | Recruiting · Signing | **Signing Day** hard sign, class lock, OS week 6 gate | `SigningDayScreen`, `SigningDayRepository`, `SigningDayProgress` | Partial | Board actions / crunch 1.5× UX mostly task-driven—not full hub |
| MEGA-GDD-029 | Recruiting · Portal | Offseason portal weeks 2–5; **no in-season** portal | `PortalCyclePolicy`, `PortalSimulationEngine`, dashboard portal tasks | Partial | Portal **UI** tab placeholder; simulation + policy in core |
| MEGA-GDD-030 | NIL & economy | Annual **collective pool**; ledger committed/reserved/remaining; **3× ask** cap | `NilAnnualPoolEngine`, `NilBudgetLedger`, `NilBudgetRepository`, `NilInterestScoring` | Partial | Recruit assign-NIL UX lives on signing/tasks—not full slider on recruit sheet everywhere GDD lists |
| MEGA-GDD-031 | NIL & economy | **Retention** bumps OS week 1 from same ledger | `NilBudgetRepository`, offseason week-one data paths | Partial | Dedicated retention sheet vs chips-only flows |
| MEGA-GDD-032 | NIL · Fundraising | OS fundraising events → cash / NIL bump / facilities seed | `FundraisingSimulator`, `FundraisingWeekNinePanel`, `weeknine/` | Done | Week index = authoritative OS week 9 (fundraising), not GDD summary week 8 |
| MEGA-GDD-033 | NIL · Academics | Program academics **grade 0–100** affects recruiting budget path | `ProgramAcademicsGradeEngine`, `AcademicsRecruitingBudgetEngine` | Partial | Grade not prominent on School hub overview per game-design tables |
| MEGA-GDD-034 | Coaching carousel | HC **skill tree**, contract, **job security** meter | `HeadCoachCardScreen`, `JobSecurityMeter`, `CoachSecurityRepository` | Partial | Extension/interview/poach flows incomplete vs carousel AI table |
| MEGA-GDD-035 | Coaching carousel | **Unemployed** coach; job center; world keeps simming | `JobCenterScreen`, `CoachEmploymentRepository`, `JobCenterEngine` | Done | — |
| MEGA-GDD-036 | Coaching carousel · Staff | **3** assistants (+4th at prestige ≥85); staff market | `StaffRosterBootstrap`, `SchoolStaffTabPanel`, DB staff entities | Partial | Poach/hire/promotion GA→assistant sim thin vs GDD labor market |
| MEGA-GDD-037 | Facilities | Six facility categories grades **1–10**; upgrade cash + weeks | `FacilityGrades`, `FacilityUpgradePolicy`, `FacilityGradeEngine` | Done | Arena naming rights display override lightly wired |
| MEGA-GDD-038 | Conference realignment | OS week **10** invite cards; invite score; skip season 1 | `ConferenceOfferGenerator`, `OffseasonWeekTenConstants`, week-ten UI hooks | Partial | User accept/decline cards need full offseason shell polish (`offseason_shell_placeholder`) |
| MEGA-GDD-039 | Preseason | Single setup week; **8 tasks** (invites, exhibitions, RS builder, …) | `PreseasonTaskId`, `PreseasonScreen`, task panels | Partial | Several tasks auto-complete or stubby vs full [`preseason.md`](../../planning/preseason.md) |
| MEGA-GDD-040 | Scheduling | **32** RS games max; early event **4** + **28** builder | `GameConstants.MAX_REGULAR_SEASON_GAMES`, `RegularSeasonSlotBudget`, `PreseasonScheduleSolver` | Done | League generator symmetry issues logged in `ScratchAuditTest` (stubs/asymmetry) |
| MEGA-GDD-041 | Scheduling · Tiebreakers | Pack-driven **standingsTiebreakers** stack (h2h, PD, …) | Pack field `standingsTiebreakers`; `RankingsHubSnapshotFactory` sorts W-L only | Missing | No `ConferenceStandingsTiebreakerEngine`—conf tables ignore pack tiebreaker modules |
| MEGA-GDD-042 | Time system | Exhibition W1–3; RS W1–32; conf tourney; **7** national PS weeks | `ByeWeekPolicy`, `PostseasonCalendar`, phase tests | Done | — |
| MEGA-GDD-043 | Season gameplay | Injuries, discipline, **chemistry**, **fatigue** in-season | `InjuryOccurrenceEngine`, `DisciplineRepository`, `ChemistryRepository`, fatigue tick on advance | Done | — |
| MEGA-GDD-044 | Selection | **NCBA NET** all 365; Media & Coaches polls; selection uses **NET** | `NcbaNetRankingEngine`, `HumanPollRankingEngine`, `SelectionSundayEngine` | Done | — |
| MEGA-GDD-045 | Postseason | Selection Sunday → **Opening Round** (24) + **64** main; seeds 1–52 | `NcbaOpeningRoundEngine`, `SelectionSundayRepository`, bracket repos | Done | — |
| MEGA-GDD-046 | Postseason | **NCI (32)** + **Heritage (16)** parallel brackets | `NciHeritageBracketEngine`, `ParallelNationalBracketRepository` | Done | Sim runs in batch; user bracket UI thin |
| MEGA-GDD-047 | Postseason · Conf tournaments | Per-conf **TournamentTemplate** from pack | `ConferenceTournamentEngine`, pack `tournamentTemplates`, embedded JSON templates | Done | — |
| MEGA-GDD-048 | Postseason UI | **March Mayhem** hub (NCBA / NCI / Heritage) | `MarchMayhemHubScreen`, `MarchMayhemHubSnapshot` | Partial | Hub body still `march_mayhem_hub_placeholder` string for main panel |
| MEGA-GDD-049 | Awards & media | Weekly honors; EoS team/National MVP; archive | `YearlyAwardsWrapEngine`, `AwardsHubPolicy`, news publisher | Partial | Awards hub vs full [`awards.md`](../../planning/awards.md) cadence |
| MEGA-GDD-050 | Mobile UX | Automation: coach auto-spend, recruiting **Recommended** maintain, batch league sim | `DynastyAutomationScreen`, `AiRecruitingWeekRunner`, `SimWeekAdvanceResolver` | Partial | Coach auto-spend toggle on card; priority tree depth limited |
| MEGA-GDD-051 | Mobile UX · Notifications | **In-app** dashboard feed tiers; **no OS push** | `NotificationFeedComposer`, `DashboardNotificationFeedPanel` | Done | — |
| MEGA-GDD-052 | Mobile UX · News | League stories in **News** hub, not notification feed | `NewsHubScreen`, `NewsHubPolicy` | Done | — |
| MEGA-GDD-053 | In-season frame | Bottom tabs: Roster · Calendar · Dashboard · News; hamburger hubs | `DynastyShellScreen`, `DynastyShellTabContent`, `DynastyHubMenu` | Partial | **Calendar tab** = `shell_calendar_placeholder`; **Statistics hub** placeholder |
| MEGA-GDD-054 | In-season frame | Dashboard: last/next game, **game preview** (%, score, compare, H2H) | `GamePreviewEngine`, dashboard overview wiring in shell | Partial | Preview not on every dashboard layout pass—verify parity with `game-frame.md` |
| MEGA-GDD-055 | Game start | New dynasty **2026–27** season 1; wizard coach → school → contract → **OS week 1** | `NewGameWizardScreen`, `SeasonMembershipYear`, save bootstrap | Partial | Wizard **Settings** step placeholder; pack import not in wizard |
| MEGA-GDD-056 | Game start · NIL preview | Contract sheet shows **NIL budget** preview for chosen school | `NilAnnualPoolEngine`, school step / contract—grep shows economy repo used in preseason panels with default prestige | Partial | School pick may still use `DEFAULT_PROGRAM_PRESTIGE` in places vs live school prestige |
| MEGA-GDD-057 | Player UI | Player card tabs, ratings, NIL valuation on overview | `PlayerCardSheet` (overview-only sheet), `DynastyPlayer` | Partial | Full multi-tab card per `player-cards.md` not shipped |
| MEGA-GDD-058 | Save model | **SQLite/Room** dynasty persistence (ADR-001) | `DynastyDatabase`, `SaveSlotRepository`, migrations test | Done | — |
| MEGA-GDD-059 | Engineering milestones · Ship | Identity **JSON/URL import** in shipping app | Core pipeline + `PackRepository`; settings stub in wizard | Partial | Import surfaced via settings/repo hooks—not primary new-game path |
| MEGA-GDD-060 | Engineering milestones | `:app` + `:core` + `:database` modules; CI test/assemble | `android/settings.gradle.kts`, `.github/workflows/*` | Done | — |
| MEGA-GDD-061 | Season review | End-of-year before offseason; prestige/NIL preview next cycle | `SeasonReviewScreen`, `SeasonReviewRepository` | Partial | `season_review_placeholder` when snapshot empty |
| MEGA-GDD-062 | History | Dynasty archive, HOF, drafted players in school/history hubs | `HistoryHubScreen`, `HistoryRepository` | Partial | Depth vs `history-hub.md` (GOAT, records) |
| MEGA-GDD-063 | Rankings hub UI | NET #1–365, Media, Coaches, conf standings tabs | `RankingsHubScreen`, `RankingsHubRepository` | Done | Unpublished state until league rankings published |
| MEGA-GDD-064 | League batch perf | 365-team week batch within performance budget | `LeagueWeekBatchPerformanceTest`, profiling doc | Done | Median budget enforced in test |
| MEGA-GDD-065 | Strategy | O/D scheme presets; halftime linkage to sim | `SchemePresets`, `SchemeMatchupResolver`, `StrategyScreen` | Done | — |
| MEGA-GDD-066 | Positions & lineup | PG–C slots, default starters algorithm | `LineupSlotPolicy`, `DefaultStarterAlgorithm`, `RotationMinutesAllocator` | Partial | Interchange / full rotation UI |
| MEGA-GDD-067 | Offseason shell UX | Full offseason chrome matching in-season frame | `OffseasonScreen` | Partial | `offseason_shell_placeholder`—week panels exist but shell copy is stub |
| MEGA-GDD-068 | AI recruiting · NIL | AI programs same ledger, 3× cap, Recommended spend | `AiRecruitingRepository`, `NilOfferEngine`, tests | Partial | Tied to placeholder user recruiting UI for parity testing |
| MEGA-GDD-069 | Accessibility | Large tap targets; color-blind safe pack colors | Compose Material defaults; pack `colors` in JSON | Partial | Pattern overlays for jersey sim not found |
| MEGA-GDD-070 | Women's basketball | Future SKU (non-goal) | Men's-only codebase | N/A | By design |

---

## Summary counts

| Status | Count |
|--------|------:|
| Done | 33 |
| Partial | 34 |
| Missing | 2 |
| N/A | 1 |
| **Total items** | **70** |

## Cross-cutting gaps (no deferrals)

1. **GDD vs authoritative specs:** [`planning/offseason.md`](../../planning/offseason.md) supersedes the abbreviated offseason table still present in `game-design.md` (week 6–9 labels); Android calendar follows `offseason.md`.
2. **UI placeholders blocking “ship” milestone:** recruiting HS/portal tabs, march mayhem hub body, calendar tab, statistics hub, parts of offseason/season-review shells.
3. **Pack import UX:** hardened core (`IdentityPackImportPipeline`) without new-game/settings import flow; **signature** verification not implemented.
4. **Conference tiebreakers:** declared in pack schema only; standings UI/engine use win-loss sort.
5. **Coaching carousel:** gating works; league-wide AI carousel and approval catalog are stub-level.

**Audit method:** Full read of `planning/game-design.md`; ripgrep across `android/**/*.kt` for pillars, phases, engines, and `placeholder`/`stub` UI markers; spot-checked embedded pack (`365` teams, `4` reclass), tests (`DefaultPackAssetTest`, `LeagueWeekBatchPerformanceTest`, import hardening tests).

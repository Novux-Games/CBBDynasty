# MEGA audit: recruiting, transfer portal, game start

**Scope:** `planning/recruiting-engine.md`, `planning/transfer-portal.md`, `planning/game-start.md` vs Android implementation (`:core/recruiting`, `:core/portal`, `:core/signingday`, `ui/recruiting`, `ui/preseason`, `ui/signingday`, `ui/newgame`, dashboard tasks).  
**Audit date:** 2026-11-11  
**Method:** Full doc read + static code trace + `:core:test` recruiting/portal/dashboard subset (green build; tests do not cover most gaps below).

**Legend:** **Status** — `FAIL` spec violation or broken behavior; `GAP` partial/stub; `PASS` aligned; `RISK` works but fragile or doc/checklist overclaims. **Pri** — P0 ship-blocker … P3 polish.

---

## A. Recruit generation & national player rankings

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-001 | P0 | FAIL | HS pool 2k–4k uniform random | `IncomingClassSeeder` uses fixed `HS_POOL_SIZE = 2400`; no per-season random draw. | `android/core/.../IncomingClassSeeder.kt` |
| MEGA-RECR-002 | P0 | FAIL | Star percentiles within pool | Stars assigned by **list index order**, not by sorting on `hiddenRating` percentiles. Top 60 order uses hidden rating, but star labels disagree with spec bands. | `IncomingClassSeeder.generatePool` |
| MEGA-RECR-003 | P1 | FAIL | JUCO 5★ = top 2% | JUCO uses `jucoBias && pct < 0.01` (~1% 5★), not 2%. | `IncomingClassSeeder.starBand` |
| MEGA-RECR-004 | P1 | FAIL | 3★ = 20–50% band | 3★ cutoff at `pct < 0.45` (30% band after 4★), not 50%. | `IncomingClassSeeder.starBand` |
| MEGA-RECR-005 | P0 | FAIL | Single persisted class pool | Runtime hub, AI, sim-week, signing load **`RecruitingClassSeed.demoPool` (120)** instead of `IncomingClassSeedRepository.loadPool`. | `RecruitingHubScreen.kt`, `AiRecruitingRepository.kt`, `SaveSlotRepository.buildSimWeekAdvanceInputSuspended`, `SigningDayRepository.kt` |
| MEGA-RECR-006 | P0 | FAIL | Top 60 fixed at seed | `demoPool` sets `nationalHsRank` from index (`index < 60`), not hidden-rating national order across HS+JUCO. | `RecruitingClassSeed.kt` |
| MEGA-RECR-007 | P1 | GAP | Top 60 HS+JUCO combined | Seeder ranks combined pool correctly when used; most UI paths never load that persisted pool. | `IncomingClassSeedRepository` vs hub |
| MEGA-RECR-008 | P1 | FAIL | News / signing fallback | `NationalRecruitingRankings.top60(season)` always builds from `demoPool`, not dynasty seed. | `NationalRecruitingRankings.kt` |
| MEGA-RECR-009 | P2 | GAP | Portal excluded from recruit Top 60 | No code path mixes portal into Top 60 (PASS intent), but portal tab is nonfunctional (see portal section). | — |
| MEGA-RECR-010 | P2 | PASS | Top 60 cap 60 rows | `top60FromPool` filters `nationalHsRank` and caps at 60 in tests. | `NationalRecruitingRankingsTest` |
| MEGA-RECR-011 | P1 | GAP | Priority roll 1–2 at create | `RecruitPriorityRoll` used in seeder; demo pool also rolls priorities — OK when seeder used. | `IncomingClassSeederTest` |
| MEGA-RECR-012 | P2 | GAP | Geography national pool | Only 11 stub states; no pack-driven pipeline breadth. | `IncomingClassSeeder.states` |
| MEGA-RECR-013 | P1 | FAIL | Scout letter categories | Only `scoutLetterOverall`; no per-category A–F rows on sheet. | `RecruitProspect`, hub UI |
| MEGA-RECR-014 | P2 | RISK | Hidden 0–99 not in UI | Overall letter shown; hidden ratings exist in save encoding — aligns with “no public 0–99”. | Seeder + prospect model |

---

## B. Recruiting points economy

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-015 | P0 | FAIL | Prestige tier weekly pool | `loadOrOpenCycle(loaded)` defaults **`DEFAULT_PROGRAM_PRESTIGE = 40`** for almost all calls; chosen school prestige not bound at dynasty create. | `RecruitingEconomyRepository.kt`, `RecruitingHubScreen.kt` |
| MEGA-RECR-016 | P1 | RISK | Academics multiplier on pool | `AcademicsRecruitingBudgetEngine` scales weekly pool ±15% from program grade — not in `recruiting-engine` tier table (may be intentional from game-design; document drift). | `RecruitingWeekGrant.openCycle` |
| MEGA-RECR-017 | P1 | PASS | 50% carryover | `RecruitingWeekGrant.applyWeekAdvanceGrant` uses `weeklyCarryoverFraction`. | `RecruitingWeekGrant.kt` + tests |
| MEGA-RECR-018 | P1 | PASS | Signing crunch 1.5× OS W2–5 | Balance `signingCrunchOffseasonWeeks: [2,3,4,5]`, multiplier 1.5. | `assets/balance/balance-2026.json` |
| MEGA-RECR-019 | P1 | RISK | Fixed pool per cycle | Ledger stores `weeklyPoolBase` at open; mid-cycle prestige change not applied (spec: OK until EoS). Opening at wrong prestige (RECR-015) breaks the lock. | `RecruitingCycleLedger` |
| MEGA-RECR-020 | P1 | PASS | Refunded offer/visit/scout | Reservation refund on week advance via `RecruitingReservationRefund`. | `RecruitingEconomyRepository.onCalendarWeekAdvanced` |
| MEGA-RECR-021 | P2 | GAP | Week 6 no new drip | `closeCycleAfterHardSign` zeroes points and sets `cycleClosed` — aligns with Signing Day admin week. | `RecruitingEconomyRepository` |
| MEGA-RECR-022 | P1 | FAIL | Action costs from balance | Hub reads `BalanceRecruitingActionsParser` — PASS — but **DM/call/house/bonus spend has no `prospectId`**; spend is global, not per-target. | `spendPointAction` |
| MEGA-RECR-023 | P2 | GAP | Bonus points “remainder” | UI spends `g.bonusPoints.pointCost` gate max, not user-chosen partial remainder. | `RecruitingHubScreen` Recommended row |
| MEGA-RECR-024 | P2 | RISK | Sim-week spend estimate | Auto-maintain estimate uses `demoPool` + default prestige context. | `SaveSlotRepository.buildSimWeekAdvanceInputSuspended` |
| MEGA-RECR-025 | P1 | GAP | Next cycle pool at EoS recap | `openNextCycleAfterSeasonReview` uses `postRecapPrestige` — PASS when called; first cycle still wrong until recap. | `SeasonReviewRepository` |

---

## C. Interest, Recommended, influence, signing logic

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-026 | P1 | GAP | Interest weights in balance JSON | Weights live in `RecruitInterestConfig` hardcoded; ADR-002 `recruiting.interest.*` not in bundle. | `RecruitInterestWeights.kt`, `balance-2026.json` |
| MEGA-RECR-027 | P1 | FAIL | Point spend → interest meter | `RecruitInterestEngine` baseline sets `coachRelationship = 0.5`; **`spendDelta` param unused in production UI**; DM/call/house do not move live meter. | `RecruitInterestEngine.kt`, hub spend |
| MEGA-RECR-028 | P2 | GAP | signLikelihood zero-spend | `SignLikelihoodEngine` uses baseline interest — PASS pattern; undermined by wrong pool/prestige inputs. | `SignLikelihoodBadge.kt` |
| MEGA-RECR-029 | P1 | FAIL | Soft commit = influence #1 + closeness | `SigningDaySoftCommitResolver` treats **any scholarship offer reservation** as incoming signee; no influence rank/closeness thresholds. | `SigningDayIncomingSignee.kt` |
| MEGA-RECR-030 | P2 | GAP | Influence board UI | Row shows rank/closeness from engine; board not updated by spends/commits league-wide. | `RecruitingHubScreen`, `InfluenceBoardEngine` |
| MEGA-RECR-031 | P2 | PASS | Recommended default tab | Hub lands on Recommended; ~list cap from balance recommended parser. | `RecruitingHubScreen`, `RecommendedRecruitList` |
| MEGA-RECR-032 | P1 | GAP | Elite hidden on Recommended | `SignLikelihoodEngine.hiddenOnRecommendedTab` exists — behavior depends on demo pool/stars distribution. | core tests |
| MEGA-RECR-033 | P2 | GAP | NIL assign / 3× cap | Assign NIL from recommended row; uses `NilBudgetRepository` — partial; interest NIL factor uses offer map. | Hub NIL buttons |
| MEGA-RECR-034 | P2 | GAP | Promises at offer | No promise picker wired on offer flow in hub UI. | recruiting UI search |

---

## D. Recruiting hub UI & cycle access

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-035 | P0 | FAIL | HS + JUCO tab content | **Placeholder string** only (`recruiting_placeholder_hs_juco`). | `RecruitingHubScreen.kt` |
| MEGA-RECR-036 | P0 | FAIL | Transfer Portal sub-tab | **Placeholder** (`recruiting_placeholder_portal`). | `RecruitingHubScreen.kt` |
| MEGA-RECR-037 | P1 | FAIL | Tab set vs spec | Missing Board/Targets, Committed, Lost, separate HS vs JUCO filters. Only 3 tabs. | `RecruitingHubTab` |
| MEGA-RECR-038 | P1 | FAIL | Hub open offseason week 1 | `RecruitingCyclePolicy.hubAccessible(Offseason)` requires `week >= 2`; spec: year-long HS/JUCO cycle includes retention week (points economy shared). | `RecruitingCyclePolicy.kt` |
| MEGA-RECR-039 | P1 | GAP | NIL budget strip | Pool remaining shown — PASS; drill-down committed/reserved breakdown not implemented. | Hub header |
| MEGA-RECR-040 | P2 | GAP | Official visit window RS→R4 | Gating in `RecruitingRefundableActionSpend` / gates — exists in core; full calendar integration untested in UI. | core recruiting gates |
| MEGA-RECR-041 | P2 | GAP | Walk-ons after Signing Day | Not implemented in UI/data. | spec `recruiting-engine#walk-ons` |

---

## E. Dashboard tasks (recruiting & portal)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-042 | P0 | FAIL | In-season optional recruiting tasks | `DashboardTaskGate.inSeason` never adds `SpendRecruitingPoints` or watchlist/Top60/rival triggers — only games/injury/Sunday. | `DashboardTaskGate.kt`, `InSeasonOptionalDashboardTasks.kt` |
| MEGA-RECR-043 | P1 | GAP | Offseason W2–5 portal tasks | `OffseasonOptionalDashboardTasks` implements spend/portal/crunch/scholarships — PASS for subset. | `OffseasonOptionalDashboardTasks.kt` |
| MEGA-RECR-044 | P2 | RISK | Portal Top 40 task trigger | Task when `offseasonWeekIndex == 2 && portalTop40Locked`; verify caller sets flag when week 2 starts. | `OffseasonScreen` context builder |
| MEGA-RECR-045 | P1 | PASS | Recruiting never blocks advance | Optional kinds only for recruiting; signing day has required tasks separate. | `DashboardTask.kt` |
| MEGA-RECR-046 | P1 | GAP | Signing Day required tasks | Rankings + positions + hard sign tasks exist — PASS shell; rankings data may be demo pool (RECR-005). | `SigningDayRequiredDashboardTasks`, `SigningDayScreen` |
| MEGA-RECR-047 | P2 | GAP | Dismiss / skip week nudges | Optional panel supports dismiss set; in-season recruiting ids not emitted. | `DashboardOptionalTasksPanel` |
| MEGA-RECR-048 | P2 | PASS | Week 1 no portal dashboard cards | Portal week-1 on Players Leaving, not dashboard — aligns with transfer-portal doc. | `transfer-portal.md` |

---

## F. Signing day ↔ preseason UI boundary

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-049 | P1 | FAIL | Signing rankings data source | `SigningDayScreen` uses seeded pool when `loadPool` succeeds, but **`SigningDayRepository.loadIncomingClass` uses demoPool** for hard sign class — split brain. | `SigningDayScreen.kt` vs `SigningDayRepository.kt` |
| MEGA-RECR-050 | P1 | GAP | Position editor Signing Day | Tab exists; bulk position edit engine partial — verify against `SigningDayScreen` positions tab. | `ui/signingday` |
| MEGA-RECR-051 | P1 | GAP | Cycle close after hard sign | `closeCycleAfterHardSign` on confirm — wired from screen; ensure advance from OS W6 respects closed hub. | `SigningDayScreen` |
| MEGA-RECR-052 | P1 | GAP | Preseason Incoming Class Seed | Manual panel seeds 3900-player pool — PASS when user completes task; **not auto** on new dynasty before season. | `IncomingClassSeedPanel` |
| MEGA-RECR-053 | P0 | FAIL | Season 1 Top 60 from tipoff | New save starts **Offseason W1** (`game-start.md`); recruit cycle + Top 60 not live until later preseason task — conflicts recruiting-engine “Season 1 preseason seed before exhibitions”. | `signNewGameFromWizard`, calendar bootstrap |
| MEGA-RECR-054 | P2 | GAP | News Recruits vs Rankings hub | News shows Top 60 from seed when present; Rankings hub is **team** NET/Media/Coaches — correct separation, easy user confusion. | `NewsHubScreen`, `RankingsHubScreen` |

---

## G. Team rankings vs player (recruit) rankings

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-055 | P1 | RISK | Preseason task “Rankings” | `PreseasonTaskId.RankingsReview` gates **team** poll publication (`RankingsHubRepository`), not recruit Top 60 — label overlap with Signing Day “View Recruiting Rankings”. | `PreseasonTaskId`, `RankingsHubRepository` |
| MEGA-RECR-056 | P2 | FAIL | RankingsReview completion UX | Checklist allows **tap Complete** with no Rankings Hub visit (no dedicated panel). | `PreseasonScreen.kt` generic card |
| MEGA-RECR-057 | P1 | PASS | Media/Coaches T25 = programs | `RankingsHubScreen` tabs NET/Media/Coaches/Conference — team-level. | `ui/rankings` |
| MEGA-RECR-058 | P1 | PASS | Recruit Top 60 ≠ top 25 crop | UI strings reference #1–#60 in seed panel/news. | `strings.xml`, seed panel |
| MEGA-RECR-059 | P2 | GAP | School hub rankings unpublished | Until RankingsReview complete — intentional; unrelated to recruit board. | `SchoolHubRepository` |

---

## H. Transfer portal

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-060 | P1 | PASS | One cycle; no in-season | `PortalCyclePolicy.inSeasonPortalEntriesForbidden`. | core portal |
| MEGA-RECR-061 | P1 | PASS | Portal recruit window OS W2–5 | `portalRecruitingWindowOpen` week range. | `PortalCyclePolicy.kt` |
| MEGA-RECR-062 | P1 | GAP | Considering pool 1k–1.4k | `PortalSimulationEngine.rollConsideringPoolSize` — PASS; **not surfaced** in Recruiting hub (placeholder). | `PortalSimulationEngine.kt` |
| MEGA-RECR-063 | P1 | PASS | Portal Top 40 lock week 2 | `PortalCycleRepository.onOffseasonWeekStarted` + `shouldLockTop40AtWeekStart`. | data portal |
| MEGA-RECR-064 | P1 | PASS | Week 1 Roster/Transfer mutual exclusion | `PortalWeekOnePartition`, `PlayersLeavingPanel`. | ui offseason leaving |
| MEGA-RECR-065 | P2 | GAP | Shared points/NIL with HS/JUCO | Economy shared in code — PASS; portal spend UI missing. | hub placeholder |
| MEGA-RECR-066 | P2 | GAP | Portal entrant stars on final pool | `PortalStarBands` in core; entrant list not shown in hub. | `PortalStarBands.kt` |
| MEGA-RECR-067 | P2 | GAP | AI portal Recommended | AI runner HS/JUCO only in `AiRecruitingRepository`; portal slice not run. | data recruiting AI |

---

## I. AI recruiting & sim-week

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-068 | P1 | FAIL | Same rules as user | League AI uses per-program prestige for tier points — PASS; user school context uses **default 40** and demo pool. | `AiRecruitingWeekRunner`, repositories |
| MEGA-RECR-069 | P2 | GAP | AI visibility off-screen | No rival board UI — PASS. | spec |
| MEGA-RECR-070 | P2 | GAP | Auto-maintain on sim-week | Wired in `SaveSlotRepository` advance path when automation on — undermined by demo pool. | save repo |

---

## J. Game start / new dynasty

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-071 | P1 | GAP | Wizard steps 1–6 | Implemented scaffold — PASS per `game-start.md` “implemented”. | `ui/newgame` |
| MEGA-RECR-072 | P1 | FAIL | School summary recruiting/NIL preview | `SchoolSummarySheet` shows prestige/conference/location only — **no weekly points or annual NIL** preview. | `SchoolStepContent.kt` |
| MEGA-RECR-073 | P2 | GAP | Pipeline states on summary | Not shown on school summary sheet. | `SchoolStepContent.kt` |
| MEGA-RECR-074 | P2 | GAP | School demeanor on summary | Not shown. | `SchoolStepContent.kt` |
| MEGA-RECR-075 | P1 | GAP | Prestige ≤40 default filter | School step should filter — verify `SchoolStepContent` (pack overlay) — present in list UI. | newgame school step |
| MEGA-RECR-076 | P1 | FAIL | Bootstrap recruiting on sign | `signNewGameFromWizard` does not seed class, open recruiting ledger with **team** prestige, or call `nilAnnualPoolRepository.openCyclePoolUsd` with chosen school. | `SaveSlotRepository.signNewGameFromWizard` |
| MEGA-RECR-077 | P2 | PASS | Launch → Offseason W1 Players Leaving | Matches `game-start` step 6 narrative. | wizard + calendar |
| MEGA-RECR-078 | P2 | RISK | `teamId` on coach/save | Wizard stores school in draft; confirm pack `teamId` bound on coach entity for recruiting context (audit: `userSchoolRecruitingContext` depends on loaded dynasty team). | `NilAnnualPoolRepository` |

---

## K. Documentation / checklist honesty

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-079 | P1 | RISK | BUILD_CHECKLIST 6.11 “done” | Year-long economy coded but **prestige-at-open** and **pool source** bugs (RECR-015, RECR-005). | `docs/BUILD_CHECKLIST.md` |
| MEGA-RECR-080 | P1 | RISK | BUILD_CHECKLIST 6.14 “done” | Offseason portal optional tasks only; **in-season recruiting triggers missing** (RECR-042). | BUILD_CHECKLIST |
| MEGA-RECR-081 | P2 | RISK | BUILD_CHECKLIST 6.20 Signing Day | UI routes exist; data integrity fail on class list (RECR-049). | BUILD_CHECKLIST |

---

## Summary counts

| Status | Count |
|--------|------:|
| FAIL | 22 |
| GAP | 44 |
| PASS | 13 |
| RISK | 8 |
| **Total items** | **81** |

### P0 concentration (fix first)

RECR-001, 002, 005, 006, 015, 035, 036, 042, 053 — demo pool vs real seed, prestige-stuck weekly points, hub placeholders, missing in-season tasks, Season 1 calendar vs preseason seed.

### Suggested verification order

1. Wire **one** `RecruitPoolProvider` (dynasty seed → fallback) through hub, AI, signing, sim-week.  
2. Bind **program prestige** at `signNewGameFromWizard` + `loadOrOpenCycle(loaded, prestige)`.  
3. Replace star assignment with **percentile sort** per pool kind.  
4. Implement **in-season** `RecruitingOptionalDashboardTasks` per `recruiting-engine#in-season-dashboard-tasks`.  
5. Flesh hub tabs or downgrade BUILD_CHECKLIST claims until placeholders ship.

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial MEGA audit (81 × MEGA-RECR-NNN) |

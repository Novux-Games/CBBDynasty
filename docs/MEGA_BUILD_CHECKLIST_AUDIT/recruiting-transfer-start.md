# MEGA audit: recruiting, transfer portal, game start

**Scope:** `planning/recruiting-engine.md`, `planning/transfer-portal.md`, `planning/game-start.md` vs Android implementation (`:core/recruiting`, `:core/portal`, `:core/signingday`, `ui/recruiting`, `ui/preseason`, `ui/signingday`, `ui/newgame`, dashboard tasks).  
**Audit date:** 2026-11-11  
**Method:** Full doc read + static code trace + `:core:test` recruiting/portal/dashboard subset (green build; tests do not cover most gaps below).

**Legend:** **Status** — `FAIL` spec violation or broken behavior; `GAP` partial/stub; `PASS` aligned; `RISK` works but fragile or doc/checklist overclaims. **Pri** — P0 ship-blocker … P3 polish.

---

## A. Recruit generation & national player rankings

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-001 | P0 | PASS | HS pool 2k–4k uniform random | `resolveHsPoolSize` draws uniform 2k–4k per enrolling class seed. | `IncomingClassSeeder.kt` |
| MEGA-RECR-002 | P0 | FAIL | Star percentiles within pool | Stars assigned by **list index order**, not by sorting on `hiddenRating` percentiles. Top 60 order uses hidden rating, but star labels disagree with spec bands. | `IncomingClassSeeder.generatePool` |
| MEGA-RECR-003 | P1 | FAIL | JUCO 5★ = top 2% | JUCO uses `jucoBias && pct < 0.01` (~1% 5★), not 2%. | `IncomingClassSeeder.starBand` |
| MEGA-RECR-004 | P1 | FAIL | 3★ = 20–50% band | 3★ cutoff at `pct < 0.45` (30% band after 4★), not 50%. | `IncomingClassSeeder.starBand` |
| MEGA-RECR-005 | P0 | PASS | Single persisted class pool | Hub, AI, signing, sim-week advance use `ensurePool`. | `IncomingClassSeedRepository.kt`, `SaveSlotRepository.kt` |
| MEGA-RECR-006 | P0 | FAIL | Top 60 fixed at seed | `demoPool` sets `nationalHsRank` from index (`index < 60`), not hidden-rating national order across HS+JUCO. | `RecruitingClassSeed.kt` |
| MEGA-RECR-007 | P1 | GAP | Top 60 HS+JUCO combined | Seeder ranks combined pool correctly when used; most UI paths never load that persisted pool. | `IncomingClassSeedRepository` vs hub |
| MEGA-RECR-008 | P1 | PASS | News / signing fallback | Signing/news/hub use `ensurePool` + `top60FromPool`; demo `top60()` deprecated. | `SigningDayScreen.kt`, `NewsHubScreen.kt` |
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
| MEGA-RECR-015 | P0 | PASS | Prestige tier weekly pool | `resolveUserProgramPrestige` + `loadOrOpenCycle` use chosen school's pack prestige. | `RecruitingEconomyRepository.kt` |
| MEGA-RECR-016 | P1 | RISK | Academics multiplier on pool | `AcademicsRecruitingBudgetEngine` scales weekly pool ±15% from program grade — not in `recruiting-engine` tier table (may be intentional from game-design; document drift). | `RecruitingWeekGrant.openCycle` |
| MEGA-RECR-017 | P1 | PASS | 50% carryover | `RecruitingWeekGrant.applyWeekAdvanceGrant` uses `weeklyCarryoverFraction`. | `RecruitingWeekGrant.kt` + tests |
| MEGA-RECR-018 | P1 | PASS | Signing crunch 1.5× OS W2–5 | Balance `signingCrunchOffseasonWeeks: [2,3,4,5]`, multiplier 1.5. | `assets/balance/balance-2026.json` |
| MEGA-RECR-019 | P1 | RISK | Fixed pool per cycle | Ledger stores `weeklyPoolBase` at open; mid-cycle prestige change not applied (spec: OK until EoS). Opening at wrong prestige (RECR-015) breaks the lock. | `RecruitingCycleLedger` |
| MEGA-RECR-020 | P1 | PASS | Refunded offer/visit/scout | Reservation refund on week advance via `RecruitingReservationRefund`. | `RecruitingEconomyRepository.onCalendarWeekAdvanced` |
| MEGA-RECR-021 | P2 | GAP | Week 6 no new drip | `closeCycleAfterHardSign` zeroes points and sets `cycleClosed` — aligns with Signing Day admin week. | `RecruitingEconomyRepository` |
| MEGA-RECR-022 | P1 | PASS | Action costs from balance | Hub spends DM/call/house/bonus per `prospectId` with balance costs + interest deltas. | `RecruitingHubScreen.kt` |
| MEGA-RECR-023 | P2 | GAP | Bonus points “remainder” | UI spends `g.bonusPoints.pointCost` gate max, not user-chosen partial remainder. | `RecruitingHubScreen` Recommended row |
| MEGA-RECR-024 | P2 | PASS | Sim-week spend estimate | Auto-maintain estimate uses `ensurePool` + resolved prestige context. | `SaveSlotRepository.buildSimWeekAdvanceInputSuspended` |
| MEGA-RECR-025 | P1 | GAP | Next cycle pool at EoS recap | `openNextCycleAfterSeasonReview` uses `postRecapPrestige` — PASS when called; first cycle still wrong until recap. | `SeasonReviewRepository` |

---

## C. Interest, Recommended, influence, signing logic

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-026 | P1 | GAP | Interest weights in balance JSON | Weights live in `RecruitInterestConfig` hardcoded; ADR-002 `recruiting.interest.*` not in bundle. | `RecruitInterestWeights.kt`, `balance-2026.json` |
| MEGA-RECR-027 | P1 | PASS | Point spend → interest meter | `interestDelta` in balance; persisted `spendDelta` per prospect; hub meter uses `liveInterest`. | `RecruitingInterestSpendRepository.kt` |
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
| MEGA-RECR-035 | P0 | PASS | HS + JUCO tab content | Full pool partitions + Top 60 + HS/JUCO lists. | `HsJucoTabBody.kt` |
| MEGA-RECR-036 | P0 | PASS | Transfer Portal sub-tab | Top 40 + portal Recommended with full spend/NIL surface. | `RecruitingHubTabBodies.kt` |
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
| MEGA-RECR-049 | P1 | PASS | Signing rankings data source | Screen rankings and `loadIncomingClass` both use persisted `ensurePool`. | `SigningDayRepository.kt` |
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
| MEGA-RECR-065 | P2 | PASS | Shared points/NIL with HS/JUCO | Portal Recommended reuses hub spend/NIL actions. | `RecruitingHubScreen.kt` |
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

## L. Interest meter UI & recruit sheet

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-082 | P0 | FAIL | Live **interest** meter 0–100 | Row string `recruiting_influence_row` prints **baselineInterest** with “(zero spend)” copy — no **`liveInterest`** bar or post-spend value. | `strings.xml`, `RecruitingHubScreen.kt` |
| MEGA-RECR-083 | P1 | FAIL | Two surfaces (`baseline` vs `interest`) | Spec separates Recommended **`signLikelihood`** (zero spend) from sheet **meter** (includes spend). UI conflates influence line with baseline only. | `recruiting-engine.md#two-surfaces`, hub row |
| MEGA-RECR-084 | P2 | GAP | Dedicated recruit sheet route | Actions live inline on Recommended list; no full-screen sheet with meter, factor chips, ranked rivals. | `RecruitingHubScreen.kt` |
| MEGA-RECR-085 | P2 | GAP | Visual meter control | No `LinearProgressIndicator` / gauge for interest or closeness — text labels only. | Hub compose |
| MEGA-RECR-086 | P1 | PASS | Spend → meter feedback | Hub reloads spend map on `gateRefresh` after each action. | `RecruitingHubScreen.kt` |
| MEGA-RECR-087 | P2 | GAP | Factor score debug | `factorScores` / `effectiveWeights` not exposed in UI or dev overlay. | `RecruitInterestSnapshot` |
| MEGA-RECR-088 | P2 | FAIL | Influence closeness league sim | `InfluenceBoardEngine` ranks user school; **closeness** not advanced on league week advance / AI spend. | core recruiting influence |
| MEGA-RECR-089 | P2 | GAP | Official visit → campus bump | Visit spend wired with `prospectId` — PASS reservation; no `campusLife` delta surfaced on meter. | `spendRefundableAction` |
| MEGA-RECR-090 | P1 | GAP | Promise picker → `promiseDelta` | Offer flow lacks promise UI; interest model’s promise bump unused. | hub + `player-loyalty-promises.md` |
| MEGA-RECR-091 | P2 | GAP | Post-spend haptics / snackbar | Points pool header refreshes; no per-recruit confirmation that relationship moved. | `gateRefresh` pattern |
| MEGA-RECR-092 | P2 | GAP | Dashboard deep link scroll-to-target | `DashboardDeepLink.RecruitingRecommended` opens hub; no `prospectId` scroll/highlight. | `DynastyApp.kt` |

---

## M. NIL (hub, interest, signing)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-093 | P1 | GAP | Assign NIL partial offers | Buttons **Meet ask** / **Clear** only — no slider for partial $ between 0 and **3× ask**. | `RecruitingHubScreen` NIL row |
| MEGA-RECR-094 | P1 | FAIL | NIL drill-down sheet | Strip shows remaining / annual total — spec **committed / reserved** breakdown sheet missing. | `recruiting-engine.md#nil-budget-strip` |
| MEGA-RECR-095 | P2 | GAP | Reserved vs committed copy | `NilBudgetLedger` tracks buckets in core; hub shows aggregate remaining only. | `NilBudgetRepository` |
| MEGA-RECR-096 | P2 | GAP | 3× cap affordance | `NilOfferEngine.validateAssign` enforces cap; UI does not show max assignable for this recruit. | core nil tests |
| MEGA-RECR-097 | P1 | GAP | Interest NIL tops at ask | `NilInterestScoring` tested; assign path does not refresh row **badge** / meter when offer changes. | hub `gateRefresh` |
| MEGA-RECR-098 | P1 | GAP | NIL at hard sign snapshot | `IncomingSignee.nilSignedUsd` defaults **0**; hard sign does not persist offer map into signee rows. | `SigningDayIncomingSignee.kt` |
| MEGA-RECR-099 | P2 | PASS | Annual pool + week-9 boost | Hub loads `nilPoolBoostUsd` from facilities week — PASS wiring. | `RecruitingHubScreen` LaunchedEffect |
| MEGA-RECR-100 | P0 | FAIL | Dynasty bootstrap NIL cycle | `signNewGameFromWizard` skips `NilAnnualPoolRepository.openCyclePoolUsd` for chosen school (RECR-076). | `SaveSlotRepository` |
| MEGA-RECR-101 | P2 | PASS | Portal NIL same ledger | Portal targets use `assignOffer` by entrant id. | `NilBudgetRepository` |
| MEGA-RECR-102 | P2 | RISK | NIL offers keyed demo ids | Offers stored by `prospectId`; if user recruits from **demoPool** but seed uses different ids, ledger orphans. | RECR-005 + `NilBudgetRepository` |

---

## N. Signing Day flow, hard sign, prep

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-103 | P1 | FAIL | Required task gating | `SigningDayRequiredDashboardTasks` built from progress flags; **hard sign** button always on Positions tab — not blocked until rankings + positions tasks done in UI. | `SigningDayScreen.kt` |
| MEGA-RECR-104 | P1 | GAP | Admin week — no new drip | Spec: week 6 **no chasing**; verify `RecruitingWeekGrant` skips grant when `cycleClosed` after hard sign — partial wiring via `closeCycleAfterHardSign`. | `RecruitingEconomyRepository` |
| MEGA-RECR-105 | P2 | GAP | Walk-ons after close | No in-app feed / board after hard sign per `recruiting-engine#walk-ons`. | spec; no UI |
| MEGA-RECR-106 | P1 | GAP | Unsigned targets sim elsewhere | Hard sign finalizes offer-based class only; no league AI “sign elsewhere” for open elite targets. | `SigningDayHardSignEngine` |
| MEGA-RECR-107 | P2 | GAP | Position editor depth | Positions tab **cycles enum** per signee — not bulk roster editor / flex validation from spec. | `SigningDayScreen` `onCyclePosition` |
| MEGA-RECR-108 | P2 | GAP | Rankings task deep link | Dashboard opens Signing Day rankings tab — does not mirror **News → Recruits** full Top 60 context copy. | `DashboardOptionalTasksPanel` |
| MEGA-RECR-109 | P1 | GAP | Roster scholarship attach | Hard sign engine finalizes class object; roster / 15-cap attach path not traced to `DynastyRosterRepository`. | `SigningDayRepository.hardSign` |
| MEGA-RECR-110 | P1 | FAIL | Soft vs hard commit semantics | `SigningDaySoftCommitResolver` = any active **scholarship offer** reservation → signee; ignores influence #1 + closeness band. | `SigningDayIncomingSignee.kt` |
| MEGA-RECR-111 | P2 | PASS | Rankings tab uses seed when present | `SigningDayScreen` `loadPool` → `top60FromPool` — PASS when preseason task done. | `SigningDayScreen.kt` |
| MEGA-RECR-112 | P2 | GAP | Prep before Signing Day | No **recruiting prep brief** / R1–R4 copy surface tying crunch multiplier to dashboard (offseason optional flavor only). | `OffseasonOptionalDashboardTasks` |
| MEGA-RECR-113 | P2 | GAP | Signing close → hub read-only | `cycleClosed` set on hard sign; hub still navigable from shell — enforce read-only Committed-only not implemented. | `RecruitingCyclePolicy` |

---

## O. Prep brief & preseason class narrative

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-114 | P2 | GAP | Preseason report × recruiting | `PreseasonReportPanel` article does not reference **Top 60** / class outlook from seed headline. | `PreseasonReportRepository` |
| MEGA-RECR-115 | P2 | GAP | Incoming seed progress UX | `IncomingClassSeedPanel` toggles `seeding` boolean — no stepped progress / cancel for **~3.9k** player generation. | `IncomingClassSeedPanel.kt` |
| MEGA-RECR-116 | P1 | GAP | Season 1 prep brief timing | Spec: Top 60 from tipoff year one; wizard lands **Offseason W1** with no class narrative until later preseason week — missing “class preview” brief at all. | `game-start.md`, RECR-053 |
| MEGA-RECR-117 | P2 | PASS | Top 60 preview in seed task | Panel lists **60** rows after `runSeed` — PASS local preview. | `IncomingClassSeedPanel` |
| MEGA-RECR-118 | P3 | GAP | Workouts task recruiting cross-link | Preseason workouts task does not nudge open Recruiting for new commits on roster. | `preseason.md` task 2 |

---

## P. Rankings confusion (team vs recruit)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-119 | P1 | RISK | Signing dashboard copy | `dashboard_task_signing_rankings` = “View **recruiting** rankings (Top 60)” while Rankings hub tab is **programs**. | `strings.xml` |
| MEGA-RECR-120 | P1 | RISK | Preseason task 4 label | `preseason_task_rankings` = NET + **Media & Coaches** — same word “rankings” as recruit national list. | `PreseasonScreen.kt` |
| MEGA-RECR-121 | P2 | PASS | News Recruits tab separation | `NewsHubScreen` loads seed Top 60 when seeded — distinct from team polls. | `NewsHubScreen.kt` |
| MEGA-RECR-122 | P2 | GAP | Signing Day disclaimer | Rankings tab lacks helper text “**Player** Top 60 — not team poll”. | `SigningDayScreen` |
| MEGA-RECR-123 | P2 | GAP | Rankings hub route naming | Hamburger **Rankings** → NET/Media/Coaches only; no link to recruit list for confused users. | `RankingsHubScreen` |
| MEGA-RECR-124 | P3 | GAP | In-app glossary | No coach-tip explaining Media T25 vs Recruit #1–#60. | product copy |

---

## Q. Phase transitions (Signing Day → next preseason)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-125 | P1 | GAP | Next cycle open timing | `openNextCycleAfterSeasonReview` sets **next** weekly pool from recap prestige — must align with **preseason Incoming Class Seed** opening same enrolling class index. | `SeasonReviewRepository`, recruiting doc |
| MEGA-RECR-126 | P1 | GAP | Enrolling season index | Seed repository keys `seasonIndex` on metadata; hard sign same index — next class seed season bump not audited in one helper. | `IncomingClassSeedRepository` |
| MEGA-RECR-127 | P2 | GAP | Offseason W8 → Preseason gate | Advance into preseason does not auto-close prior recruiting UI state / reservations cleanup. | `enterInSeasonFromPreseason` path |
| MEGA-RECR-128 | P2 | GAP | Walk-on window calendar | Spec places walk-ons after Signing Day; calendar phase after OS W6 advance not wired to walk-on feed. | `recruiting-engine#walk-ons` |
| MEGA-RECR-129 | P1 | GAP | Preseason without re-seed | Re-entering preseason year 2+ must run **new** 2k–4k draw; no guard preventing duplicate `runSeed` on same key. | `IncomingClassSeedPanel` `enabled = headline == null` |
| MEGA-RECR-130 | P2 | PASS | Hub open RS + OS W2+ | `RecruitingCyclePolicy` year-long access in RS — PASS in tests. | `RecruitingHubDefaultsTest` |
| MEGA-RECR-131 | P0 | RISK | **Lag:** main-thread seed | `runSeed` on UI button coroutine generates **3900** prospects + `encodePool` string in DataStore on device thread — frame drops / ANR risk. | `IncomingClassSeedRepository.runSeed` |
| MEGA-RECR-132 | P1 | RISK | **Lag:** load full pool | `loadPool` decodes entire newline blob when hub/AI wired to seed — **O(n)** parse per screen open. | `IncomingClassSeedRepository.decodePool` |

---

## R. Recruit generation (2k–4k) & data integrity

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-133 | P0 | PASS | Uniform HS **2,000–4,000** | `runSeed` → `seed()` without override rolls `resolveHsPoolSize` from dynasty hash + season. | `IncomingClassSeeder.kt`, `IncomingClassSeedRepository.kt` |
| MEGA-RECR-134 | P1 | RISK | **Lag:** DataStore blob size | ~3900-row `encodePool` single preference string — large read/write on every seed and load. | `IncomingClassSeedRepository` |
| MEGA-RECR-135 | P2 | PASS | JUCO +1,500 fixed add-on | `JUCO_POOL_SIZE = 1_500` matches spec table. | `IncomingClassSeeder.kt` |
| MEGA-RECR-136 | P1 | FAIL | Star assignment order bug | `starBand(index, count)` uses **creation index**, not **hiddenRating** percentile sort — stars decorrelated from talent (RECR-002). | `generatePool` + `starBand` |
| MEGA-RECR-137 | P1 | FAIL | Hidden vs star mismatch | `hiddenRatingForStars` drawn after star band from index — reinforces wrong percentile bands for 3★/2★ split. | `IncomingClassSeeder.kt` |
| MEGA-RECR-138 | P2 | GAP | Demo pool star distribution | `RecruitingClassSeed.demoPool(120)` used in production paths — not representative of 2k–4k tail. | RECR-005 |

---

## S. Point assignment & economy bugs

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-139 | P0 | PASS | DM / call / house **per recruit** | Point actions pass `prospectId` into `spendPointAction`. | `RecruitingHubScreen.kt` |
| MEGA-RECR-140 | P1 | FAIL | Bonus points amount | `onBonusPoints` spends `g.bonusPoints.pointCost` max gate, not user-entered **remainder** partial dump. | RECR-023 |
| MEGA-RECR-141 | P2 | PASS | Refundable per prospect | Scholarship / scout / visit use `spendRefundableAction` with `prospectId` — PASS. | hub `onSpend` |
| MEGA-RECR-142 | P1 | RISK | Reservations without interest | Offer reservations persist; interest/influence not updated — soft commit resolver still offer-only. | RECR-029, RECR-088 |
| MEGA-RECR-143 | P2 | GAP | Week advance refund batch | `onCalendarWeekAdvanced` refunds reservations — no test tying refund to **same** prospect offer set across hub reload. | `RecruitingEconomyRepository` |
| MEGA-RECR-144 | P2 | RISK | Sim-week auto-maintain double spend | User manual spend + `estimateUserAutoMaintainSpend` both debit ledger in advance path when automation on — risk over-spend unless gated. | `SaveSlotRepository.buildSimWeekAdvanceInputSuspended` |

---

## T. Dashboard tasks (recruiting, portal, signing)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-145 | P0 | FAIL | In-season drip nudge | No `SpendRecruitingPoints` when `pointsAvailable > 0` after week grant in RS — `InSeasonOptionalDashboardTasks` games only. | `InSeasonOptionalDashboardTasks.kt` |
| MEGA-RECR-146 | P1 | FAIL | Watchlist threshold tasks | No `DashboardTask` kinds for watchlist / re-engage / rival commit — spec table unimplemented. | `DashboardTask.kt` |
| MEGA-RECR-147 | P1 | FAIL | Top 60 movement nudge | No trigger when board rank changes for user targets (no persisted watchlist). | `recruiting-engine#in-season-dashboard-tasks` |
| MEGA-RECR-148 | P1 | GAP | Portal pipeline entrant task | `OffseasonOptionalDashboardTasks` has portal window flavor; **pipeline entrant** `{Name} in portal` trigger not built. | `transfer-portal.md` dashboard table |
| MEGA-RECR-149 | P2 | PASS | Signing required trio | Rankings + positions + hard sign task ids emitted on OS W6 — PASS shell. | `SigningDayRequiredDashboardTasks` |
| MEGA-RECR-150 | P2 | GAP | Task complete detection | Optional recruiting complete-on-action not implemented — no hub spend listener marks task done. | `dashboard-tasks.md` |
| MEGA-RECR-151 | P2 | GAP | Dismiss / skip week hiding | Offseason portal dismiss flags exist; in-season recruiting dismiss ids never emitted. | RECR-047 |
| MEGA-RECR-152 | P2 | GAP | Deep link Recommended default | `SpendRecruitingPoints` string exists; in-season never surfaces link to hub. | `DashboardOptionalTasksPanel` |

---

## U. AI recruiting & sim-week

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-153 | P1 | PASS | User school prestige in AI week | AI fallback prestige from `resolveUserProgramPrestige`. | `AiRecruitingRepository.kt` |
| MEGA-RECR-154 | P1 | PASS | AI pool = persisted class | `runLeagueWeekIfActive` uses `ensurePool` for full national class. | `AiRecruitingRepository.kt` |
| MEGA-RECR-155 | P2 | GAP | AI influence / commits headline | `AiRecruitingWeekRunner` processes programs; News commit headlines from AI not traced in audit paths. | core `ai` package |
| MEGA-RECR-156 | P2 | GAP | AI portal slice | Runner HS/JUCO Recommended only — portal entrants excluded (RECR-067). | `AiRecruitingWeekRunner` |
| MEGA-RECR-157 | P2 | PASS | Auto-maintain flag | Respects `automation.recruitingAutoMaintain` when hub open — PASS. | `AiRecruitingRepository` |
| MEGA-RECR-158 | P2 | RISK | AI + 365 programs lag | `AiLeagueProgramGenerator.demoLeague` + week batch in same advance as user sim — perf budget unrelated to recruiting pool size until real pool wired. | `LeagueWeekBatchPerformanceBudget` |
| MEGA-RECR-159 | P1 | GAP | Same interest pipeline | AI should use `RecruitInterestEngine` weights; runner uses simplified Recommended sort — parity gap. | `AiRecruitingWeekRunner` |
| MEGA-RECR-160 | P2 | GAP | Sim-week confirm line copy | Advance confirmation should mention recruiting auto-maintain spend estimate — partial via sim-week input only. | `SaveSlotRepository` |

---

## V. Lag & performance risks (product)

| ID | Pri | Status | Spec / topic | Finding | Evidence |
|----|-----|--------|--------------|---------|----------|
| MEGA-RECR-161 | P0 | RISK | Preseason seed on UI thread | Same root as RECR-131 — blocks checklist until complete; no `withContext(Dispatchers.Default)`. | `IncomingClassSeedPanel` |
| MEGA-RECR-162 | P1 | RISK | Recommended list at scale | When seed wired, building `RecommendedRecruitList` over **3.9k** rows each hub open without pagination. | `RecommendedRecruitList.build` |
| MEGA-RECR-163 | P1 | RISK | LazyColumn only ~50 cap | Engine may still score full pool before cap — CPU each composition if demo pool replaced. | `BalanceRecruitingRecommendedParser` |
| MEGA-RECR-164 | P2 | RISK | AI league week × demo pool | Doubles work on advance: batch sim 365 teams + AI recruiting loop — acceptable today; explodes if pool + persistence added without batching. | `SaveSlotRepository.advanceWeek` |
| MEGA-RECR-165 | P2 | GAP | No recruiting perf test | `LeagueWeekBatchPerformanceTest` guards games; no test for seed encode/decode under **4k** prospects. | `android/core` tests |
| MEGA-RECR-166 | P2 | RISK | DataStore preference size limits | Single-string pool encoding may approach DataStore size guidance on low-end devices. | `encodePool` |

---

## Summary counts

| Status | Count |
|--------|------:|
| FAIL | 41 |
| GAP | 74 |
| PASS | 16 |
| RISK | 18 |
| **DEBT (prior audit)** | **81** |
| **Expanded rows (082–166)** | **+85** |
| **Total MEGA-RECR** | **166** |

### P0 concentration (fix first)

| ID | Theme |
|----|--------|
| **RECR-001, 002, 005, 006** | National pool scale, star percentiles, demo vs persisted seed |
| **RECR-015, 076, 100** | Prestige / NIL bootstrap at dynasty create |
| **RECR-035, 036** | Hub HS/JUCO + portal placeholders |
| **RECR-042, 145** | In-season recruiting dashboard tasks |
| **RECR-053, 116** | Season 1 calendar vs preseason Top 60 |
| **RECR-082, 139** | Interest meter + per-recruit point spend |
| **RECR-131, 161** | Main-thread class seed / ANR risk |

Legacy single-line list: RECR-001, 002, 005, 006, 015, 035, 036, 042, 053 — plus **082, 100, 131, 139, 145, 161** from expansion.

### Suggested verification order

1. Wire **one** `RecruitPoolProvider` (dynasty seed → fallback) through hub, AI, signing, sim-week.  
2. Bind **program prestige** + **NIL cycle** at `signNewGameFromWizard` + `loadOrOpenCycle(loaded, prestige)`.  
3. Fix **star percentiles** (sort hidden rating → band) + **random HS count** 2k–4k.  
4. Move **IncomingClassSeed** generation off main thread; add perf test for encode/load.  
5. Implement **live interest meter** + **prospect-scoped** point actions + `InSeasonOptionalDashboardTasks` recruiting triggers.  
6. Flesh hub tabs / signing soft-commit rules / align BUILD_CHECKLIST claims.

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial MEGA audit (81 × MEGA-RECR-NNN) |
| 2026-11-11 | Expanded +85 rows (082–166): interest UI, NIL, Signing Day, prep brief, rankings UX, phase transitions, generation, points, dashboard, AI, lag |

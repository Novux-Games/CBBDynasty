# League week batch — performance profiling

**Scope:** Regular-season **Sim Week** league work in `:core` after the user’s game is committed: non-user **batch sim**, injury rolls, **NCBA NET** (365), **Media** and **Coaches** polls. Planning: [game-design.md](../planning/game-design.md#media-systems), [time-system.md](../planning/time-system.md), [simulation-engine.md](../planning/simulation-engine.md#playback-modes) (non-user games use **sim-to-end**; league batch currently uses `LeagueFastSim` score stubs until full roster wiring lands).

## Targets

| Surface | Budget | Notes |
|---------|--------|--------|
| **`:core` JVM (CI)** | **≤ 12 s** per RS week at **365** teams | `LeagueWeekBatchPerformanceBudget.MAX_CORE_REGULAR_SEASON_WEEK_MS`; enforced in `LeagueWeekBatchPerformanceTest` |
| **Android UI thread (product)** | **≤ 2.5 s** typical mid-tier device | `SaveSlotRepository.advanceDynastyWeek` / `buildSimWeekAdvanceConfirmation` run on **`Dispatchers.IO`**; UI shows spinners only ([MEGA-QA-014]) |
| **Games per week** | ~180 unique RS matchups | 365 programs × ~32 RS weeks (see schedule generator) |

Non-user batch sim uses **fast score** sim today (`LeagueFastSim` without `SimTuning`). When possession sim is wired for league batch, re-profile and tighten budgets.

## What is included / excluded

| Included | Excluded |
|----------|----------|
| `LeagueWeekBatchSimEngine.runRegularSeasonWeek` | Room / SQLite |
| `LeagueWeekBatchWeeklyRankings.attach` | DataStore persist |
| AI injury roll hooks (lightweight) | Recruiting / portal AI on advance |
| | Early-season bracket sim (repository layer) |
| | Postseason bracket batch |

## Run the benchmark (local / CI)

```bash
cd android
./gradlew :core:test --tests dev.novux.dynasty.cbb.core.league.LeagueWeekBatchPerformanceTest
```

The test builds a **365-team** schedule, warms four prior weeks so NET/polls have résumé data, then times three iterations of week 5 (median iteration must be under the CI budget).

## Android profiling (device)

1. Build debug APK with a dynasty in regular season week ≥ 5.
2. Enable **Android Studio Profiler → CPU**; trigger **Sim Week**.
3. Filter by `dev.novux.dynasty.cbb.core.league` and `dev.novux.dynasty.cbb.core.resume`.
4. Compare wall time to the **2.5 s** product band; file regressions if database or repository work dominates (move heavy I/O off main).

## Code map

| Unit | Role |
|------|------|
| `LeagueWeekBatchSimEngine` | Unique RS matchups for the week, fast sim, standings merge |
| `LeagueWeekBatchWeeklyRankings` | NET + human polls after games |
| `LeagueWeekBatchRepository` (`:app`) | Schedules, brackets, persist, news |
| `LeagueWeekBatchPerformanceBudget` | Shared constants for docs + tests |

## When budgets fail

1. Confirm no accidental **possession** sim in league batch (`LeagueFastSim` + `SimTuning`).
2. Profile `NcbaNetRankingEngine` / `HumanPollRankingEngine` — cost grows with `seasonResults.size` (O(teams × games)).
3. Avoid re-generating full 365 schedules on each advance (one-time per season).
4. Update `MAX_CORE_REGULAR_SEASON_WEEK_MS` only with product sign-off and changelog note.

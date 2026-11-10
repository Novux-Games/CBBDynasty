package dev.novux.dynasty.cbb.core.league

/**
 * Time budgets for full-league **week advance** work in `:core` (batch sim + weekly NET/polls).
 *
 * Product target (mid-tier phone, UI thread): advance should feel instant — see
 * `docs/LEAGUE_WEEK_BATCH_PROFILING.md`.
 */
object LeagueWeekBatchPerformanceBudget {
    const val FULL_LEAGUE_TEAM_COUNT = 365

    /**
     * JVM CI ceiling for one RS week (fast batch scores + NET + Media/Coaches polls).
     * Mobile ships the same `:core` path; device profiling uses the doc workflow.
     */
    const val MAX_CORE_REGULAR_SEASON_WEEK_MS: Long = 12_000L

    const val BENCHMARK_ITERATIONS = 3
}

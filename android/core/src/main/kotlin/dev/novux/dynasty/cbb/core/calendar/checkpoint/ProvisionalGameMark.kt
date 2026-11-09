package dev.novux.dynasty.cbb.core.calendar.checkpoint

/**
 * In-week sim result staged before [WeekCheckpointState.advanceWeek].
 * Full box scores ship with possession sim (Phase 6+).
 */
data class ProvisionalGameMark(
    val gameId: String,
    val homeScore: Int,
    val awayScore: Int,
    val simGeneration: Int = 1,
) {
    init {
        require(gameId.isNotBlank()) { "gameId required" }
        require(homeScore >= 0 && awayScore >= 0) { "scores must be non-negative" }
        require(simGeneration >= 1) { "simGeneration must be >= 1" }
    }
}

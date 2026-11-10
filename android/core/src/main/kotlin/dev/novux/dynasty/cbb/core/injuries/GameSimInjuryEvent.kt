package dev.novux.dynasty.cbb.core.injuries

/** Per-game injury occurrence — [planning/injuries.md#sim-hook]. */
data class GameSimInjuryEvent(
    val playerId: String,
    val injuryId: String,
    val label: String,
    val bodyPart: String,
    val severity: InjurySeverity,
    val weeksRemaining: Int,
    val occurredInGameMinute: Int? = null,
)

data class GameSimInjuryResult(
    val injuries: List<GameSimInjuryEvent> = emptyList(),
)

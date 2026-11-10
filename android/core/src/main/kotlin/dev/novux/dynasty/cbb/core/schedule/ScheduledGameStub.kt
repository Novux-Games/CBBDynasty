package dev.novux.dynasty.cbb.core.schedule

data class ScheduledGameStub(
    val kind: ScheduleGameKind,
    val opponentTeamId: String,
    val opponentDisplayName: String,
    val homeGame: Boolean,
    val rsWeekIndex: Int? = null,
)

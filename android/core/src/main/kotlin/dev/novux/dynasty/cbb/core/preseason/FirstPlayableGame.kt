package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind

/** Calendar focus after preseason — [planning/preseason.md#advance-to-regular-season]. */
data class FirstPlayableGame(
    val gameId: String,
    val opponentTeamId: String,
    val opponentDisplayName: String,
    val homeGame: Boolean,
    val kind: ScheduleGameKind,
    val calendarWeekIndex: Int,
)

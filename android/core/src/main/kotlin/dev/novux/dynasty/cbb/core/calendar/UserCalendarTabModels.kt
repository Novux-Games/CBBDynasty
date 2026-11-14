package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameBoxArchive
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind

enum class UserCalendarRowKind {
    Game,
    Bye,
    OffWeek,
}

data class UserCalendarGameResult(
    val userScore: Int,
    val opponentScore: Int,
    val userTeamIsHome: Boolean,
    val boxArchive: ProvisionalGameBoxArchive? = null,
)

data class UserCalendarGameEntry(
    val opponentTeamId: String,
    val opponentDisplayName: String,
    val homeGame: Boolean,
    val gameKind: ScheduleGameKind,
    val isPastWeek: Boolean,
    /** Month/day display for calendar row ([planning/game-frame.md#calendar-tab]). */
    val gameDateLabel: String,
    val gamePosition: CalendarPosition,
    val gameIndexInWeek: Int = 0,
    val playedResult: UserCalendarGameResult? = null,
) {
    val isTappable: Boolean = playedResult != null || !isPastWeek
}

data class UserCalendarWeekRow(
    val weekLabel: String,
    val phase: CalendarPhase,
    val weekIndex: Int,
    val rowKind: UserCalendarRowKind,
    val games: List<UserCalendarGameEntry>,
    val isCurrentWeek: Boolean,
)

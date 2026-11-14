package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind

enum class UserCalendarRowKind {
    Game,
    Bye,
    OffWeek,
}

data class UserCalendarGameEntry(
    val opponentDisplayName: String,
    val homeGame: Boolean,
    val gameKind: ScheduleGameKind,
    val isPastWeek: Boolean,
    /** Month/day display for calendar row ([planning/game-frame.md#calendar-tab]). */
    val gameDateLabel: String,
)

data class UserCalendarWeekRow(
    val weekLabel: String,
    val phase: CalendarPhase,
    val weekIndex: Int,
    val rowKind: UserCalendarRowKind,
    val games: List<UserCalendarGameEntry>,
    val isCurrentWeek: Boolean,
)

package dev.novux.dynasty.cbb.core.calendar

/**
 * Dynasty clock position — [planning/time-system.md] week index is source of truth.
 */
data class CalendarPosition(
    val seasonIndex: Int,
    val phase: CalendarPhase,
    val weekIndex: Int,
) {
    init {
        require(seasonIndex >= 1) { "seasonIndex must be >= 1" }
        require(weekIndex >= 1) { "weekIndex must be >= 1" }
        require(weekIndex <= PhaseWeekPolicy.weeksInPhase(phase)) {
            "weekIndex $weekIndex out of range for $phase"
        }
    }
}

fun CalendarPosition.compareToPosition(other: CalendarPosition): Int {
    compareValuesBy(
        this,
        other,
        { it.seasonIndex },
        { it.phase.ordinal },
        { it.weekIndex },
    ).let { return it }
}

fun CalendarPosition.isAtOrBefore(committedThrough: CalendarPosition): Boolean =
    compareToPosition(committedThrough) <= 0

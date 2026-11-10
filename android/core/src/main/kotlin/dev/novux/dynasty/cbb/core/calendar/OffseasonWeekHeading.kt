package dev.novux.dynasty.cbb.core.calendar

/**
 * Offseason week labels — [planning/offseason.md#calendar-overview].
 */
object OffseasonWeekHeading {
    val numberedWeekRange: IntRange =
        1..PhaseWeekPolicy.weeksInPhase(CalendarPhase.Offseason)

    fun header(weekIndex: Int): String {
        require(weekIndex in numberedWeekRange) { "offseason week $weekIndex out of range" }
        return CalendarWeekLabel.display(
            CalendarPosition(seasonIndex = 1, phase = CalendarPhase.Offseason, weekIndex = weekIndex),
        )
    }

    fun themeLine(weekIndex: Int): String =
        when (weekIndex) {
            1 -> "Players Leaving"
            2 -> "Recruiting & Portal · Pro draft"
            3, 4, 5 -> "Recruiting & Portal"
            6 -> "Signing Day"
            7 -> "Summer Workouts"
            8 -> "Cuts & Roster Lock"
            9 -> "Fundraising & Upgrades"
            10 -> "Conference Offers"
            else -> error("offseason week $weekIndex out of range")
        }
}

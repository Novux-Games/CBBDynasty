package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear

/**
 * Player-facing week headers — week index is authoritative ([planning/time-system.md]).
 */
object CalendarWeekLabel {
    fun display(position: CalendarPosition): String =
        when (position.phase) {
            CalendarPhase.Offseason -> "Offseason Week ${position.weekIndex}"
            CalendarPhase.Preseason -> "Preseason"
            CalendarPhase.Exhibition -> "Exhibition Week ${position.weekIndex}"
            CalendarPhase.RegularSeason -> "Week ${position.weekIndex}"
            CalendarPhase.ConferenceTournament -> "Conference Tournament Week ${position.weekIndex}"
            CalendarPhase.Postseason -> "Postseason Week ${position.weekIndex}"
            CalendarPhase.SeasonReview -> "End of Season — ${SeasonMembershipYear.label(position.seasonIndex)}"
        }
}

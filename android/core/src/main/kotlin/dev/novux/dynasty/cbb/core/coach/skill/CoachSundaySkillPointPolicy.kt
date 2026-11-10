package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/** Sunday coach skill points ([planning/game-design.md], [planning/dashboard-tasks.md]). */
object CoachSundaySkillPointPolicy {
    const val POINTS_PER_SUNDAY: Int = 1

    private val grantPhases = setOf(
        CalendarPhase.Exhibition,
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
    )

    fun grantsOnWeekEntry(phase: CalendarPhase): Boolean = phase in grantPhases

    fun showDashboardNudge(unspentBank: Int): Boolean = unspentBank > 0
}

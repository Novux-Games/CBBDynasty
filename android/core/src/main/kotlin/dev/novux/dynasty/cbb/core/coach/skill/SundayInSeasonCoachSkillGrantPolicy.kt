package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/** Sunday in-season skill point grants + dashboard nudge ([MEGA-GDD-125]). */
object SundayInSeasonCoachSkillGrantPolicy {
    const val MEGA_ID: String = "MEGA-GDD-125"

    fun grantsInPhase(phase: CalendarPhase): Boolean =
        CoachSundaySkillPointPolicy.grantsOnWeekEntry(phase)

    fun pointsPerSunday(): Int = CoachSundaySkillPointPolicy.POINTS_PER_SUNDAY

    fun notifyUnspentBank(unspent: Int): Boolean =
        CoachSundaySkillPointPolicy.showDashboardNudge(unspent)
}
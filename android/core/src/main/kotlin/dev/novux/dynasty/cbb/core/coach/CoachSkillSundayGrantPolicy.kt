package dev.novux.dynasty.cbb.core.coach

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/** In-season Sunday skill point grants ([MEGA-GDD-125]). */
object CoachSkillSundayGrantPolicy {
    const val MEGA_ID: String = "MEGA-GDD-125"

    fun grantsPoints(phase: CalendarPhase, weekDay: Int): Boolean =
        phase == CalendarPhase.RegularSeason && weekDay == 7
}
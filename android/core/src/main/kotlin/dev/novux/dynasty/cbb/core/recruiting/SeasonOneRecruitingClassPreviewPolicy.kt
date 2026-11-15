package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition

/**
 * Season 1 offseason week 1 explains that the enrolling class was seeded at dynasty sign; Top 60 news
 * lands in preseason ([MEGA-RECR-116]).
 */
object SeasonOneRecruitingClassPreviewPolicy {
    const val MEGA_ID: String = "MEGA-RECR-116"

    fun showOffseasonWeekOneBrief(membershipSeasonIndex: Int, position: CalendarPosition): Boolean =
        SeasonOneRecruitingBootstrap.appliesToMembershipYear(membershipSeasonIndex) &&
            position.phase == CalendarPhase.Offseason &&
            position.weekIndex == 1
}
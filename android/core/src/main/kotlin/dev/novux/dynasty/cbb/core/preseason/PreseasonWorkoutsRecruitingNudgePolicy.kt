package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Preseason workouts should nudge recruiting when new commits are on the roster ([MEGA-RECR-118]). */
object PreseasonWorkoutsRecruitingNudgePolicy {
    const val MEGA_ID: String = "MEGA-RECR-118"

    fun showRecruitingNudge(roster: List<RosterPlayer>): Boolean =
        roster.any { it.classYear == PlayerClassYear.Freshman }
}
package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.recruiting.SigningDayPreseasonRecruitingTransition

/**
 * Enrolling-class pools key on [enrollingSeasonIndex]; next draw uses [nextEnrollingClassSeasonIndex]
 * ([MEGA-RECR-126]).
 */
object IncomingClassEnrollingSeasonPolicy {
    const val MEGA_ID: String = "MEGA-RECR-126"

    fun poolKeySeason(membershipSeasonIndex: Int): Int =
        SigningDayPreseasonRecruitingTransition.enrollingPoolSeasonIndex(membershipSeasonIndex)

    fun nextDrawSeason(membershipSeasonIndex: Int): Int =
        SigningDayPreseasonRecruitingTransition.nextEnrollingClassSeasonIndex(membershipSeasonIndex)
}
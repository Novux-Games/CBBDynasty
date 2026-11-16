package dev.novux.dynasty.cbb.core.recruiting

/**
 * Season review advances recruiting/NIL with [nextSeasonIndex]; preseason advance seeds the next
 * enrolling class at [SigningDayPreseasonRecruitingTransition.nextEnrollingClassSeasonIndex]
 * ([MEGA-RECR-025], [MEGA-RECR-125]).
 */
object RecruitingNextCycleSeasonReviewPolicy {
    const val MEGA_ID_RECAP_POOL: String = "MEGA-RECR-025"
    const val MEGA_ID_TIMING: String = "MEGA-RECR-125"

    fun enrollingPoolSeasonAfterPreseasonAdvance(membershipSeasonIndex: Int): Int =
        SigningDayPreseasonRecruitingTransition.nextEnrollingClassSeasonIndex(membershipSeasonIndex)
}
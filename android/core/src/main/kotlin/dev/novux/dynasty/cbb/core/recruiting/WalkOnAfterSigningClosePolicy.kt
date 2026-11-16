package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.offseason.weekeight.OffseasonRosterLockConstants

/**
 * Walk-on tryouts surface after recruiting hard sign / cycle close ([MEGA-RECR-105], [MEGA-RECR-128]).
 */
object WalkOnAfterSigningClosePolicy {
    const val MEGA_ID: String = "MEGA-RECR-105"
    const val CALENDAR_MEGA_ID: String = "MEGA-RECR-128"

    val walkOnSurfaceWeeks: IntRange =
        OffseasonRosterLockConstants.OFFSEASON_WEEK_INDEX..OffseasonRosterLockConstants.OFFSEASON_WEEK_INDEX

    fun surfaceWalkOnNudge(recruitingCycleClosed: Boolean, offseasonWeekIndex: Int): Boolean =
        recruitingCycleClosed && offseasonWeekIndex in walkOnSurfaceWeeks
}
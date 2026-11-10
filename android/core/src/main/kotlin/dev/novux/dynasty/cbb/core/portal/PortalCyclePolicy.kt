package dev.novux.dynasty.cbb.core.portal

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * One national portal cycle per offseason — [planning/transfer-portal.md#calendar].
 */
object PortalCyclePolicy {
    fun inSeasonPortalEntriesForbidden(phase: CalendarPhase): Boolean =
        phase != CalendarPhase.Offseason

    fun portalRecruitingWindowOpen(phase: CalendarPhase, offseasonWeekIndex: Int?): Boolean =
        phase == CalendarPhase.Offseason &&
            (offseasonWeekIndex ?: 0) in PortalConstants.PORTAL_RECRUITING_WEEKS

    fun shouldLockTop40AtWeekStart(offseasonWeekIndex: Int): Boolean =
        offseasonWeekIndex == 2

    fun portalWindowClosed(offseasonWeekIndex: Int): Boolean =
        offseasonWeekIndex > PortalConstants.PORTAL_RECRUITING_WEEKS.last
}

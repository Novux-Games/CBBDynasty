package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.portal.PortalConstants

/** News hub tab visibility ([planning/news-hub.md#surfaces]). */
object NewsHubPolicy {
    fun portalTabVisible(phase: CalendarPhase, offseasonWeekIndex: Int?): Boolean =
        phase == CalendarPhase.Offseason &&
            (offseasonWeekIndex ?: 0) in 1..PortalConstants.PORTAL_RECRUITING_WEEKS.last
}

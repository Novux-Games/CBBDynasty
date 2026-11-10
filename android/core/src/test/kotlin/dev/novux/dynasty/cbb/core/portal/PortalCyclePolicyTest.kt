package dev.novux.dynasty.cbb.core.portal

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PortalCyclePolicyTest {
    @Test
    fun inSeason_forbidsPortalEntries() {
        assertTrue(PortalCyclePolicy.inSeasonPortalEntriesForbidden(CalendarPhase.RegularSeason))
        assertFalse(PortalCyclePolicy.inSeasonPortalEntriesForbidden(CalendarPhase.Offseason))
    }

    @Test
    fun recruitingWindow_offseasonWeeksTwoThroughFive() {
        assertFalse(PortalCyclePolicy.portalRecruitingWindowOpen(CalendarPhase.Offseason, 1))
        assertTrue(PortalCyclePolicy.portalRecruitingWindowOpen(CalendarPhase.Offseason, 3))
        assertFalse(PortalCyclePolicy.portalRecruitingWindowOpen(CalendarPhase.Offseason, 6))
        assertFalse(PortalCyclePolicy.portalRecruitingWindowOpen(CalendarPhase.RegularSeason, null))
    }
}

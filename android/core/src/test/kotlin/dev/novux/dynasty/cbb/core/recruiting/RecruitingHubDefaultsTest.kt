package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitingHubDefaultsTest {
    @Test
    fun defaultTab_isRecommended() {
        assertEquals(RecruitingHubTab.Recommended, RecruitingHubDefaults.defaultTab)
        assertEquals(RecruitingHubTab.Recommended, RecruitingHubDefaults.tabs.first())
    }

    @Test
    fun hubAccessible_yearLong_inRegularSeason() {
        assertTrue(RecruitingCyclePolicy.hubAccessible(CalendarPhase.RegularSeason))
    }

    @Test
    fun hubAccessible_offseasonFromWeekTwo() {
        assertTrue(RecruitingCyclePolicy.hubAccessible(CalendarPhase.Offseason, offseasonWeekIndex = 2))
        assertEquals(false, RecruitingCyclePolicy.hubAccessible(CalendarPhase.Offseason, offseasonWeekIndex = 1))
    }
}

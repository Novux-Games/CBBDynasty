package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsHubPolicyTest {
    @Test
    fun portalVisibleOffseasonWeekOne() {
        assertTrue(NewsHubPolicy.portalTabVisible(CalendarPhase.Offseason, 1))
    }

    @Test
    fun portalVisibleOffseasonWeekTwo() {
        assertTrue(NewsHubPolicy.portalTabVisible(CalendarPhase.Offseason, 2))
    }

    @Test
    fun portalHiddenInSeason() {
        assertFalse(NewsHubPolicy.portalTabVisible(CalendarPhase.RegularSeason, null))
    }
}

package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/** [MEGA-FRAME-100] */
class DashboardTaskDismissalTest {
    @Test
    fun key_scopesSameTaskIdAcrossWeeks() {
        val w1 = CalendarPosition(seasonIndex = 1, phase = CalendarPhase.RegularSeason, weekIndex = 1)
        val w2 = CalendarPosition(seasonIndex = 1, phase = CalendarPhase.RegularSeason, weekIndex = 2)
        val a = DashboardTaskDismissal.key(w1, DashboardTaskId.ReviewInjury)
        val b = DashboardTaskDismissal.key(w2, DashboardTaskId.ReviewInjury)
        assertNotEquals(a, b)
        assertEquals("RegularSeason:1:ReviewInjury", a)
    }
}
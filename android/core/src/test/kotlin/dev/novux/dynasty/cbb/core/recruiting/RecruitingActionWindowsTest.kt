package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitingActionWindowsTest {
    @Test
    fun scoutOnlyRegularSeason() {
        assertTrue(RecruitingActionWindows.scoutTheirGameOpen(CalendarPhase.RegularSeason))
        assertFalse(RecruitingActionWindows.scoutTheirGameOpen(CalendarPhase.Postseason))
        assertFalse(RecruitingActionWindows.scoutTheirGameOpen(CalendarPhase.Offseason))
    }

    @Test
    fun officialVisit_throughOffseasonWeek5() {
        assertTrue(
            RecruitingActionWindows.officialVisitOpen(CalendarPhase.RegularSeason, offseasonWeekIndex = null),
        )
        assertTrue(RecruitingActionWindows.officialVisitOpen(CalendarPhase.Offseason, offseasonWeekIndex = 5))
        assertFalse(RecruitingActionWindows.officialVisitOpen(CalendarPhase.Offseason, offseasonWeekIndex = 6))
        assertFalse(RecruitingActionWindows.officialVisitOpen(CalendarPhase.Preseason, offseasonWeekIndex = null))
    }
}

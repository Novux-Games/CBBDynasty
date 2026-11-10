package dev.novux.dynasty.cbb.core.calendar

import org.junit.Assert.assertEquals
import org.junit.Test

class OffseasonWeekHeadingTest {
    @Test
    fun numberedWeeks_areOneThroughTen() {
        assertEquals(10, OffseasonWeekHeading.numberedWeekRange.count())
        assertEquals("Offseason Week 1", OffseasonWeekHeading.header(1))
        assertEquals("Offseason Week 10", OffseasonWeekHeading.header(10))
    }

    @Test
    fun week6_isSigningDay() {
        assertEquals("Signing Day", OffseasonWeekHeading.themeLine(6))
    }
}

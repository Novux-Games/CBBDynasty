package dev.novux.dynasty.cbb.core.calendar

import org.junit.Assert.assertEquals
import org.junit.Test

class SeasonMembershipYearTest {
    @Test
    fun seasonOne_is2026_27() {
        assertEquals("2026-27", SeasonMembershipYear.label(1))
    }

    @Test
    fun seasonTwo_is2027_28() {
        assertEquals("2027-28", SeasonMembershipYear.label(2))
    }
}

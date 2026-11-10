package dev.novux.dynasty.cbb.core.offseason.weektwo

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OffseasonScholarshipPolicyTest {
    @Test
    fun offers_closedOffseasonWeek1_openWeek2Plus() {
        assertFalse(OffseasonScholarshipPolicy.scholarshipsOpen(CalendarPhase.Offseason, 1))
        assertTrue(OffseasonScholarshipPolicy.scholarshipsOpen(CalendarPhase.Offseason, 2))
        assertTrue(OffseasonScholarshipPolicy.scholarshipsOpen(CalendarPhase.RegularSeason, null))
    }
}

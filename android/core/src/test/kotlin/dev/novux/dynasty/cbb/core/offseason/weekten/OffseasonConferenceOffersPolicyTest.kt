package dev.novux.dynasty.cbb.core.offseason.weekten

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OffseasonConferenceOffersPolicyTest {
    @Test
    fun seasonOneSkipsWeek() {
        assertTrue(OffseasonConferenceOffersPolicy.skipConferenceOffersWeek(1))
        assertFalse(OffseasonConferenceOffersPolicy.skipConferenceOffersWeek(2))
    }
}

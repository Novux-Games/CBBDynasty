package dev.novux.dynasty.cbb.core.signingday

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NationalRecruitingRankingsTest {
    @Test
    fun top60_cappedAtSixty() {
        val rows = NationalRecruitingRankings.top60(1)
        assertTrue(rows.size <= 60)
        assertEquals(1, rows.first().rank)
    }
}

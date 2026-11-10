package dev.novux.dynasty.cbb.core.coach.security

import org.junit.Assert.assertEquals
import org.junit.Test

class CoachSecurityEvaluatorTest {
    @Test
    fun tierBoundaries() {
        assertEquals(JobSecurityTier.Secure, JobSecurityTier.fromScore(85))
        assertEquals(JobSecurityTier.HotSeat, JobSecurityTier.fromScore(25))
    }

    @Test
    fun adTrustBands() {
        assertEquals(AdTrustBand.Strong, AdTrustBand.fromScore(80))
        assertEquals(AdTrustBand.Low, AdTrustBand.fromScore(30))
    }
}

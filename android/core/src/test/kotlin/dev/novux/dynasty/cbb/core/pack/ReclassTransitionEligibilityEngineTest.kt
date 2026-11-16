package dev.novux.dynasty.cbb.core.pack

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReclassTransitionEligibilityEngineTest {
    @Test
    fun reclassYearOneCertifiesAfterSeasonFive() {
        assertFalse(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 1, 4))
        assertTrue(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 1, 5))
    }

    @Test
    fun reclassYearThreeCertifiesAfterSeasonThree() {
        assertFalse(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 3, 2))
        assertTrue(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 3, 3))
    }
}
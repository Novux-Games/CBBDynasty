package dev.novux.dynasty.cbb.core.nil

import org.junit.Assert.assertEquals
import org.junit.Test

class NilInterestScoringTest {
    @Test
    fun meetsAsk_fullCredit() {
        assertEquals(1.0, NilInterestScoring.factor(2_000_000, 2_000_000, 1.0), 0.001)
    }

    @Test
    fun halfAsk_halfCreditWithLinearExponent() {
        assertEquals(0.5, NilInterestScoring.factor(1_000_000, 2_000_000, 1.0), 0.001)
    }
}

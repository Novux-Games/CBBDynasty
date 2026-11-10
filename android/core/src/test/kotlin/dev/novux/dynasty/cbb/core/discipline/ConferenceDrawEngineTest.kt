package dev.novux.dynasty.cbb.core.discipline

import dev.novux.dynasty.cbb.core.balance.BalanceDisciplineConferenceDraw
import org.junit.Assert.assertNotNull
import org.junit.Test

class ConferenceDrawEngineTest {
    @Test
    fun drawReturnsOutcome() {
        val (outcome, games) = ConferenceDrawEngine.resolve(BalanceDisciplineConferenceDraw(), seed = 42L)
        assertNotNull(outcome)
        if (outcome == ConferenceDrawOutcome.ShortSuspension || outcome == ConferenceDrawOutcome.LongSuspension) {
            assertNotNull(games)
        }
    }
}

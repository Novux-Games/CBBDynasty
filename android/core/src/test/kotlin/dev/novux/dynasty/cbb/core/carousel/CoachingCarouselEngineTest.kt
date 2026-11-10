package dev.novux.dynasty.cbb.core.carousel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachingCarouselEngineTest {
    @Test
    fun season1_skipsCarousel() {
        val state = CoachingCarouselEngine.initialState(1)
        assertEquals(CarouselStatus.NotRequired, state.status)
        assertTrue(state.canEnterOffseasonWeekOne())
        assertFalse(CoachingCarouselGate.blocksOffseasonShell(1, state))
    }

    @Test
    fun season2_requiresBackgroundThenApproval() {
        var state = CoachingCarouselEngine.initialState(2)
        assertTrue(CoachingCarouselGate.blocksOffseasonShell(2, state))
        state = CoachingCarouselEngine.runBackgroundSim(state)
        assertEquals(CarouselStatus.AwaitingUserApprovals, state.status)
        val cardId = state.pendingApprovals.single().id
        state = CoachingCarouselEngine.resolveApproval(state, cardId, accepted = true)
        assertEquals(CarouselStatus.Complete, state.status)
        assertTrue(state.canEnterOffseasonWeekOne())
    }
}

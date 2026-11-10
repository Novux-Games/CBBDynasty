package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LineupSlotPolicyTest {
    @Test
    fun backcourtInterchange() {
        assertTrue(LineupSlotPolicy.slotEligible(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard))
        assertTrue(LineupSlotPolicy.slotEligible(PlayerPosition.ShootingGuard, PlayerPosition.PointGuard))
    }

    @Test
    fun centerCannotPlayForward() {
        assertFalse(LineupSlotPolicy.slotEligible(PlayerPosition.Center, PlayerPosition.PowerForward))
        assertFalse(LineupSlotPolicy.slotEligible(PlayerPosition.SmallForward, PlayerPosition.PointGuard))
    }
}

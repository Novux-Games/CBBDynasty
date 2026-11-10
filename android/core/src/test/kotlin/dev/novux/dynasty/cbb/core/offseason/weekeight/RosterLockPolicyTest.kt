package dev.novux.dynasty.cbb.core.offseason.weekeight

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RosterLockPolicyTest {
    @Test
    fun advanceBlockedWhenOverFifteen() {
        val players = List(16) { idx ->
            RosterPlayer(
                "p$idx",
                "d",
                PlayerPosition.Center,
                PlayerClassYear.Sophomore,
                false,
                idx,
                false,
            )
        }
        assertFalse(RosterLockPolicy.canAdvanceOffseasonWeek8(players))
    }

    @Test
    fun walkOnLimitedToTwo() {
        val players = emptyList<RosterPlayer>()
        assertTrue(RosterLockPolicy.canOfferWalkOn(players, walkOnsSignedThisSeason = 0))
        assertFalse(RosterLockPolicy.canOfferWalkOn(players, walkOnsSignedThisSeason = 2))
    }
}

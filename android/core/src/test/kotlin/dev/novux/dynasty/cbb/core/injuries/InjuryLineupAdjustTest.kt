package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InjuryLineupAdjustTest {
    @Test
    fun injuryPromotesBackupAtSameListedPosition() {
        val starter = player("pg1", PlayerPosition.PointGuard, 88, starter = true, slot = "pg")
        val backup = player("pg2", PlayerPosition.PointGuard, 75, depth = 8)
        val sg = player("sg1", PlayerPosition.ShootingGuard, 70, starter = true, slot = "sg", depth = 2)
        val roster = listOf(starter, backup, sg)
        val injured = InjuryLineupAdjust.applyInjuryLineupAdjust(
            roster.map {
                if (it.playerId == "pg1") {
                    it.copy(
                        injuryActive = true,
                        injuryWeeksRemaining = 3,
                        injuryLabel = "Ankle sprain",
                    )
                } else {
                    it
                }
            },
            injuredPlayerIds = setOf("pg1"),
        )
        val newStarter = injured.first { it.playerId == "pg2" }
        assertTrue(newStarter.isStarter)
        assertEquals("pg", newStarter.starterSlotWire)
        val out = injured.first { it.playerId == "pg1" }
        assertFalse(out.isStarter)
        assertEquals("pg", out.injuryWasStarterSlotWire)
    }

    @Test
    fun returnRestoresStarterSlot() {
        val returning = player("pg1", PlayerPosition.PointGuard, 88, depth = 30)
            .copy(
                injuryWasStarterSlotWire = "pg",
                injuryPriorRotationBand = 1,
            )
        val interim = player("pg2", PlayerPosition.PointGuard, 75, starter = true, slot = "pg", depth = 1)
        val restored = InjuryLineupAdjust.applyInjuryReturnLineupAdjust(listOf(returning, interim), "pg1")
        assertTrue(restored.first { it.playerId == "pg1" }.isStarter)
        assertFalse(restored.first { it.playerId == "pg2" }.isStarter)
    }

    private fun player(
        id: String,
        pos: PlayerPosition,
        ovr: Int,
        starter: Boolean = false,
        slot: String? = null,
        depth: Int = 10,
    ): RosterPlayer = RosterPlayer(
        playerId = id,
        dynastyId = "d",
        position = pos,
        classYear = PlayerClassYear.Sophomore,
        isStarter = starter,
        depthOrder = depth,
        inTransferPortal = false,
        ovr = ovr,
        off = ovr,
        def = ovr,
        starterSlotWire = slot,
    )
}

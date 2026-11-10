package dev.novux.dynasty.cbb.core.offseason.weekseven

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SummerWorkoutSimulatorTest {
    @Test
    fun simulate_returningRosterOnly_deterministic() {
        val players = listOf(
            roster("a", portal = false),
            roster("b", portal = true),
        )
        val first = SummerWorkoutSimulator.simulate(players, dynastySeed = 99L)
        val second = SummerWorkoutSimulator.simulate(players, dynastySeed = 99L)
        assertEquals(first, second)
        assertEquals(1, first.lines.size)
        assertTrue(first.lines.single().gainPoints in 1..12)
    }

    private fun roster(id: String, portal: Boolean) = RosterPlayer(
        playerId = id,
        dynastyId = "d",
        position = PlayerPosition.ShootingGuard,
        classYear = PlayerClassYear.Sophomore,
        isStarter = false,
        depthOrder = 2,
        inTransferPortal = portal,
    )
}

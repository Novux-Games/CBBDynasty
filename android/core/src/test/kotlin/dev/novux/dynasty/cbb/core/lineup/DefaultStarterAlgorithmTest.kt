package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DefaultStarterAlgorithmTest {
    @Test
    fun picksHighestListedOvrPerSlot() {
        val players = listOf(
            roster("pg-low", PlayerPosition.PointGuard, 72),
            roster("pg-high", PlayerPosition.PointGuard, 91),
            roster("sg", PlayerPosition.ShootingGuard, 70),
        )
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        assertEquals("pg-high", starters[PlayerPosition.PointGuard])
        assertEquals("sg", starters[PlayerPosition.ShootingGuard])
    }

    @Test
    fun doesNotCrossListPositions() {
        val players = listOf(
            roster("pg", PlayerPosition.PointGuard, 91),
            roster("sg", PlayerPosition.ShootingGuard, 60),
        )
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        assertEquals("pg", starters[PlayerPosition.PointGuard])
        assertEquals("sg", starters[PlayerPosition.ShootingGuard])
    }

    @Test
    fun injuredPlayerExcludedFromStarters() {
        val players = listOf(
            roster("pg", PlayerPosition.PointGuard, 90).copy(injuryActive = true, injuryWeeksRemaining = 2),
            roster("pg2", PlayerPosition.PointGuard, 70),
        )
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        assertEquals("pg2", starters[PlayerPosition.PointGuard])
        val applied = DefaultStarterAlgorithm.applyToRoster(players, starters)
        assertFalse(applied.first { it.playerId == "pg" }.isStarter)
    }

    private fun roster(id: String, pos: PlayerPosition, ovr: Int): RosterPlayer =
        RosterPlayer(
            playerId = id,
            dynastyId = "d",
            position = pos,
            classYear = PlayerClassYear.Junior,
            isStarter = false,
            depthOrder = 10,
            inTransferPortal = false,
            displayName = id,
            ovr = ovr,
            off = ovr,
            def = ovr,
        )
}

package dev.novux.dynasty.cbb.core.history

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Test
class WinContributionEngineTest {
    @Test
    fun sharesSumToTeamWins() {
        val tuning = BalanceWinContributionTuning()
        val players = listOf(
            player("a", starter = true, ovr = 85, gp = 30),
            player("b", starter = false, ovr = 70, gp = 25),
        )
        val shares = WinContributionEngine.allocateTeamWins(players, teamWins = 20, tuning)
        val sum = shares.values.sum()
        assertEquals(20.0, sum, 0.05)
        assertTrue(shares.getValue("a") > shares.getValue("b"))
    }

    private fun player(id: String, starter: Boolean, ovr: Int, gp: Int) = RosterPlayer(
        playerId = id,
        dynastyId = "d",
        position = PlayerPosition.ShootingGuard,
        classYear = PlayerClassYear.Junior,
        isStarter = starter,
        depthOrder = if (starter) 1 else 8,
        inTransferPortal = false,
        ovr = ovr,
        rsGamesPlayedSeason = gp,
    )

    private fun assertTrue(value: Boolean) = org.junit.Assert.assertTrue(value)
}

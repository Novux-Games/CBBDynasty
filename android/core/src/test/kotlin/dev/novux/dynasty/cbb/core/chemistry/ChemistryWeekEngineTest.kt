package dev.novux.dynasty.cbb.core.chemistry

import dev.novux.dynasty.cbb.core.balance.BalanceChemistry
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Test

class ChemistryWeekEngineTest {
    @Test
    fun winStreakAddsBonus() {
        val state = ChemistrySeasonState(teamChemistry = 70, winStreak = 2)
        val next = ChemistryWeekEngine.tickAfterWeekAdvance(
            state,
            sampleRoster(),
            ChemistryWeeklyTickInput(userWonGame = true),
            BalanceChemistry(70, 2, 3, 0.015, 0.02),
        )
        assertEquals(3, next.winStreak)
        assertEquals(72, next.teamChemistry)
    }

    private fun sampleRoster(): List<RosterPlayer> =
        PlayerPosition.STARTER_ORDER.map { pos ->
            RosterPlayer(
                playerId = pos.wire,
                dynastyId = "d",
                position = pos,
                classYear = PlayerClassYear.Junior,
                isStarter = true,
                depthOrder = 1,
                inTransferPortal = false,
                starterSlotWire = pos.wire,
            )
        }
}

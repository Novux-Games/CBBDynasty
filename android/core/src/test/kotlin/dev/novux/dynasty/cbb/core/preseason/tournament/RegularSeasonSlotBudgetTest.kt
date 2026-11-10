package dev.novux.dynasty.cbb.core.preseason.tournament

import org.junit.Assert.assertEquals
import org.junit.Test

class RegularSeasonSlotBudgetTest {
    @Test
    fun fourGameEventLeavesTwentyEightBuilderSlots() {
        val budget = RegularSeasonSlotBudget.fromResolution(
            TournamentInviteResolution.Accepted(EarlySeasonEventCatalog.islandClassic),
        )
        assertEquals(4, budget.eventRsGames)
        assertEquals(28, budget.builderSlots)
        assertEquals(32, budget.totalRsCap)
    }

    @Test
    fun noEventKeepsThirtyTwoSlots() {
        val budget = RegularSeasonSlotBudget.fromResolution(TournamentInviteResolution.DeclinedAll)
        assertEquals(0, budget.eventRsGames)
        assertEquals(32, budget.builderSlots)
    }

    @Test
    fun twoGameEventLeavesThirtySlots() {
        val budget = RegularSeasonSlotBudget.fromResolution(
            TournamentInviteResolution.Accepted(EarlySeasonEventCatalog.blueBloodTipOff),
        )
        assertEquals(2, budget.eventRsGames)
        assertEquals(30, budget.builderSlots)
    }
}

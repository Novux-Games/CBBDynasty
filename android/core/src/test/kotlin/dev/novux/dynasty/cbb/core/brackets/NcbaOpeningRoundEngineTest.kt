package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NcbaOpeningRoundEngineTest {
    @Test
    fun simProducesTwelveWinners() {
        val opening = (1..24).map { rank ->
            NcbaSelectionEntry(
                teamId = "t$rank",
                bidType = NcbaBidType.OpeningRound,
                netRank = rank,
                seedIndex = 1.0,
                ncbaSeed = 52 + rank,
            )
        }
        val selection = SelectionSundaySnapshot(
            seasonIndex = 1,
            ncbaField = opening,
            nciField = emptyList(),
            heritageField = emptyList(),
        )
        val plan = NcbaOpeningRoundEngine.planFromSelection(selection)
        val done = NcbaOpeningRoundEngine.simulate(plan, seed = 99L)
        assertTrue(done.completed)
        assertEquals(12, done.gameResults.size)
        assertEquals(12, done.winnerTeamIds.size)
        assertEquals(12, done.advancingTeamIds.size)
    }
}

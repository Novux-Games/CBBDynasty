package dev.novux.dynasty.cbb.core.offseason.weektwo

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NbaDraftResultsEngineTest {
    @Test
    fun highStock_senior_draftedAndRemoved() {
        val player = sample(classYear = PlayerClassYear.Senior, stock = 80)
        val resolution = NbaDraftResultsEngine.resolve(listOf(player))
        assertTrue(player.playerId in resolution.playersToRemove)
        assertEquals(NbaDraftOutcome.Drafted, resolution.resultLines.single().outcome)
    }

    @Test
    fun lowStock_underclass_returnsToRoster() {
        val player = sample(classYear = PlayerClassYear.Junior, stock = 50)
        val resolution = NbaDraftResultsEngine.resolve(listOf(player))
        assertEquals(NbaDraftOutcome.ReturnedToRoster, resolution.resultLines.single().outcome)
        assertTrue(resolution.playersToUpdate.single().declaredForDraft.not())
    }

    private fun sample(classYear: PlayerClassYear, stock: Int) = RosterPlayer(
        playerId = "p1",
        dynastyId = "d1",
        position = PlayerPosition.PointGuard,
        classYear = classYear,
        isStarter = true,
        depthOrder = 1,
        inTransferPortal = false,
        declaredForDraft = true,
        draftStockPoints = stock,
        proDayCompleted = true,
    )
}

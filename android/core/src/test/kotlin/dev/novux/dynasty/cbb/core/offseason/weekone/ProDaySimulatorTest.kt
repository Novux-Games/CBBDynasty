package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ProDaySimulatorTest {
    private val senior = RosterPlayer(
        playerId = "p-sr",
        dynastyId = "d1",
        position = PlayerPosition.ShootingGuard,
        classYear = PlayerClassYear.Senior,
        isStarter = true,
        depthOrder = 1,
        inTransferPortal = false,
    )

    @Test
    fun proDay_increasesStockAndLocksForWeekTwo() {
        val after = ProDaySimulator.run(senior, Random(1))
        assertTrue(after.proDayCompleted)
        assertTrue(after.draftStockPoints > 0)
        val lines = WeekTwoDraftResults.fromRoster(listOf(after))
        assertEquals(1, lines.size)
        assertEquals(after.draftStockPoints, lines.single().stockPoints)
    }
}

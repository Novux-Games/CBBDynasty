package dev.novux.dynasty.cbb.core.fatigue

import dev.novux.dynasty.cbb.core.balance.BalanceFatigue
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FatigueEngineTest {
    @Test
    fun lowStaminaRaisesInjuryWeight() {
        assertTrue(FatigueEngine.fatigueInjuryWeight(20) > 1.0)
        assertEquals(1.0, FatigueEngine.fatigueInjuryWeight(80), 0.001)
    }

    @Test
    fun byeWeekRecovery() {
        val player = roster(stamina = 50)
        val next = FatigueEngine.applyWeeklyRecovery(
            listOf(player),
            CalendarPhase.RegularSeason,
            teamPlayedGame = false,
            BalanceFatigue(0.35, 25, 15, 20, 35),
        ).single()
        assertEquals(75, next.stamina)
    }

    private fun roster(stamina: Int) = RosterPlayer(
        playerId = "p1",
        dynastyId = "d",
        position = PlayerPosition.PointGuard,
        classYear = PlayerClassYear.Sophomore,
        isStarter = true,
        depthOrder = 1,
        inTransferPortal = false,
        stamina = stamina,
    )
}

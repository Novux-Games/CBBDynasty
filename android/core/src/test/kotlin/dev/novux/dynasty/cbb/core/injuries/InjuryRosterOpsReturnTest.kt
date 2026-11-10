package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class InjuryRosterOpsReturnTest {
    @Test
    fun weekAdvanceAtZeroClearsInjuryAndReturnsPlayer() {
        val player = RosterPlayer(
            playerId = "p1",
            dynastyId = "d",
            position = PlayerPosition.PointGuard,
            classYear = PlayerClassYear.Freshman,
            isStarter = true,
            depthOrder = 0,
            inTransferPortal = false,
            injuryActive = true,
            injuryWeeksRemaining = 1,
            injuryLabel = "Ankle",
        )
        val ticked = InjuryRosterOps.tickWeekAdvance(CalendarPhase.RegularSeason, listOf(player))
        assertEquals(listOf("p1"), ticked.returnedPlayerIds)
        val healed = ticked.players.single()
        assertFalse(healed.injuryActive)
        assertEquals(0, healed.injuryWeeksRemaining)
    }
}

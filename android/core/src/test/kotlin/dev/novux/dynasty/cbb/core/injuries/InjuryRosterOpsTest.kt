package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class InjuryRosterOpsTest {
    @Test
    fun weekTickClearsInjuryAndRestoresLineup() {
        val starter = RosterPlayer(
            playerId = "pg1",
            dynastyId = "d",
            position = PlayerPosition.PointGuard,
            classYear = PlayerClassYear.Junior,
            isStarter = false,
            depthOrder = 30,
            inTransferPortal = false,
            injuryActive = true,
            injuryId = "ankle",
            injuryWeeksRemaining = 1,
            injuryLabel = "Ankle",
            injuryWasStarterSlotWire = "pg",
            injuryPriorRotationBand = 1,
        )
        val interim = RosterPlayer(
            playerId = "pg2",
            dynastyId = "d",
            position = PlayerPosition.PointGuard,
            classYear = PlayerClassYear.Sophomore,
            isStarter = true,
            depthOrder = 1,
            inTransferPortal = false,
            starterSlotWire = "pg",
            ovr = 70,
            off = 70,
            def = 70,
        )
        val tick = InjuryRosterOps.tickWeekAdvance(CalendarPhase.RegularSeason, listOf(starter, interim))
        assertEquals(listOf("pg1"), tick.returnedPlayerIds)
        assertFalse(tick.players.first { it.playerId == "pg1" }.injuryActive)
        assert(tick.players.first { it.playerId == "pg1" }.isStarter)
    }
}

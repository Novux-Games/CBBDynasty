package dev.novux.dynasty.cbb.core.player

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DynastyPlayerTest {
    @Test
    fun injuryStripMatchesPlayerCardFields() {
        val roster = RosterPlayer(
            playerId = "p1",
            dynastyId = "d1",
            position = PlayerPosition.ShootingGuard,
            classYear = PlayerClassYear.Junior,
            isStarter = true,
            depthOrder = 2,
            inTransferPortal = false,
            displayName = "Sam Brooks",
            ovr = 82,
            off = 84,
            def = 78,
            injuryActive = true,
            injuryId = "ankle-sprain",
            injuryWeeksRemaining = 3,
            injuryLabel = "Ankle sprain",
        )
        val player = DynastyPlayer.fromRosterPlayer(roster)
        val strip = player.injury.injuryStripOrNull()
        assertNotNull(strip)
        assertEquals("OUT", strip?.chip)
        assertEquals("Ankle sprain · 3w", strip?.rosterRowSuffix())
    }

    @Test
    fun roundTripThroughRosterPlayer() {
        val roster = RosterPlayer(
            playerId = "p2",
            dynastyId = "d1",
            position = PlayerPosition.Center,
            classYear = PlayerClassYear.Senior,
            isStarter = true,
            depthOrder = 5,
            inTransferPortal = false,
            displayName = "Alex Carter",
            hometownCity = "Omaha",
            hometownState = "NE",
            ovr = 88,
            off = 86,
            def = 90,
            eligibilityYearsUsed = 3,
            eligibilityYearsRemaining = 1,
        )
        val back = roster.toDynastyPlayer().toRosterPlayer()
        assertEquals(roster.displayName, back.displayName)
        assertEquals(roster.ovr, back.ovr)
        assertEquals(roster.eligibilityYearsRemaining, back.eligibilityYearsRemaining)
    }
}

package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PortalWeekOnePartitionTest {
    private val base = RosterPlayer(
        playerId = "p1",
        dynastyId = "d1",
        position = PlayerPosition.SmallForward,
        classYear = PlayerClassYear.Junior,
        isStarter = true,
        depthOrder = 1,
        inTransferPortal = false,
    )

    @Test
    fun mutualExclusion_rosterAndTransferNeverSharePlayer() {
        val portal = WeekOneLeavingActions.encourageTransfer(base)
        val roster = listOf(base)
        val after = roster.map { if (it.playerId == portal.playerId) portal else it }
        val rosterTab = PortalWeekOnePartition.onRosterTab(after)
        val transferTab = PortalWeekOnePartition.onTransferTab(after)
        assertEquals(0, rosterTab.intersect(transferTab.toSet()).size)
        assertEquals(1, transferTab.size)
    }

    @Test
    fun convinceStay_restoresRosterTab() {
        val portal = WeekOneLeavingActions.encourageTransfer(base)
        val stayed = WeekOneLeavingActions.convinceStay(portal, stayed = true)
        assertTrue(PortalWeekOnePartition.onRosterTab(listOf(stayed)).contains(stayed))
        assertEquals(0, PortalWeekOnePartition.onTransferTab(listOf(stayed)).size)
    }
}

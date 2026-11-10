package dev.novux.dynasty.cbb.core.recruiting

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class InfluenceDecommitPolicyTest {
    @Test
    fun decommit_preservesInterest_adjustsRank() {
        val board = ProspectInfluenceBoard(
            closeness = 70,
            userProgramId = "user",
            userRank = 1,
            leaders = listOf(
                InfluenceLeaderRow("user", "You", 80, 1),
                InfluenceLeaderRow("rival", "Rival", 75, 2),
            ),
        )
        val (interest, updated) = InfluenceDecommitPolicy.afterUserDecommit(
            board,
            preservedLiveInterest = 72,
            newLeaderProgramId = "rival",
        )
        assertEquals(72, interest)
        assertNotEquals(1, updated.userRank)
    }
}

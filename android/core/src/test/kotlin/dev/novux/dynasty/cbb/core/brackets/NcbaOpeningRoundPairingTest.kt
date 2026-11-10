package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot
import org.junit.Assert.assertEquals
import org.junit.Test

class NcbaOpeningRoundPairingTest {
    @Test
    fun pairsOneVsTwentyFourByNetOrderInBubble() {
        val opening = (1..24).map { rank ->
            NcbaSelectionEntry(
                teamId = "t$rank",
                bidType = NcbaBidType.OpeningRound,
                netRank = rank + 50,
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
        val pairs = NcbaOpeningRoundPairing.pairings(selection)
        assertEquals(12, pairs.size)
        assertEquals("t1" to "t24", pairs.first().let { it.first.teamId to it.second.teamId })
        assertEquals("t12" to "t13", pairs.last().let { it.first.teamId to it.second.teamId })
        val firstMatchup = NcbaOpeningRoundPairing.buildMatchups(selection).first()
        assertEquals(1, firstMatchup.bubbleRankHome)
        assertEquals(24, firstMatchup.bubbleRankAway)
    }
}

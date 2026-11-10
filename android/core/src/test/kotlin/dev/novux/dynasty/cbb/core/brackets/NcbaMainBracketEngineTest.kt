package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NcbaMainBracketEngineTest {
    @Test
    fun fullBracketProducesNationalChampion() {
        val ncbaField = buildList {
            (1..52).forEach { seed ->
                add(
                    NcbaSelectionEntry(
                        teamId = "main$seed",
                        bidType = NcbaBidType.DirectAtLarge,
                        netRank = seed,
                        seedIndex = 100.0 - seed,
                        ncbaSeed = seed,
                    ),
                )
            }
            (1..24).forEach { rank ->
                add(
                    NcbaSelectionEntry(
                        teamId = "open$rank",
                        bidType = NcbaBidType.OpeningRound,
                        netRank = 52 + rank,
                        seedIndex = 1.0,
                        ncbaSeed = 52 + rank,
                    ),
                )
            }
        }
        val selection = SelectionSundaySnapshot(1, ncbaField, emptyList(), emptyList())
        val openingPlan = NcbaOpeningRoundEngine.planFromSelection(selection)
        val openingDone = NcbaOpeningRoundEngine.simulate(openingPlan, 1L)
        val conferences = (1..64).associate { i ->
            val id = if (i <= 52) "main$i" else "open${i - 52}"
            id to "conf-${i % 8}"
        }
        val built = NcbaMainBracketEngine.build(selection, openingDone, conferences)
        assertEquals(64, built.entrants.size)
        built.entrants.groupBy { it.regionId }.forEach { (_, list) ->
            assertEquals(16, list.size)
            assertEquals(
                (1..16).toList(),
                list.sortedBy { it.regionalSeed }.map { it.regionalSeed },
            )
        }
        val done = NcbaMainBracketEngine.simulate(built, 77L)
        assertTrue(done.completed)
        assertNotNull(done.championTeamId)
        assertEquals(4, done.regionalChampions.size)
        assertEquals(63, done.gameResults.size)
    }
}

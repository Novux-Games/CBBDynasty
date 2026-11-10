package dev.novux.dynasty.cbb.core.seasonreview

import dev.novux.dynasty.cbb.core.brackets.NcbaMainBracketSnapshot
import dev.novux.dynasty.cbb.core.brackets.NcbaRegionId
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.brackets.NcbaMainBracketEntrant
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonReviewPostseasonLabelTest {
    @Test
    fun nationalChampionLabel() {
        val user = "duke"
        val selection = selectionWith(user)
        val main = NcbaMainBracketSnapshot(
            seasonIndex = 1,
            entrants = emptyList(),
            championTeamId = user,
            completed = true,
        )
        val label = SeasonReviewPostseasonLabel.forUserTeam(user, selection, null, main, null, null)
        assertEquals("NCBA National Champion", label)
    }

    @Test
    fun nonParticipant() {
        val label = SeasonReviewPostseasonLabel.forUserTeam("x", null, null, null, null, null)
        assertEquals("Season complete", label)
    }

    @Test
    fun mainBracketEliminationUsesRoundLabel() {
        val user = "duke"
        val selection = selectionWith(user)
        val main = NcbaMainBracketSnapshot(
            seasonIndex = 1,
            entrants = listOf(
                NcbaMainBracketEntrant(user, 1, NcbaRegionId.East, 12),
            ),
            gameResults = listOf(
                LeagueGameResult("duke", "unc", 60, 70, rsWeekIndex = 2),
            ),
        )
        val label = SeasonReviewPostseasonLabel.forUserTeam(user, selection, null, main, null, null)
        assertTrue(label.contains("eliminated"))
    }

    private fun selectionWith(teamId: String): SelectionSundaySnapshot =
        SelectionSundaySnapshot(
            seasonIndex = 1,
            ncbaField = listOf(
                NcbaSelectionEntry(teamId, NcbaBidType.DirectAtLarge, 10, 1.0, 5),
            ),
            nciField = emptyList(),
            heritageField = emptyList(),
        )
}

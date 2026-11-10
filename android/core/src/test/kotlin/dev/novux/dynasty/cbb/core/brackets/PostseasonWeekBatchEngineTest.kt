package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PostseasonWeekBatchEngineTest {
    @Test
    fun openingWeekMergesUserGameAndBatchesRest() {
        val opening = (1..24).map { rank ->
            NcbaSelectionEntry(
                teamId = "t$rank",
                bidType = NcbaBidType.OpeningRound,
                netRank = rank,
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
        val standings = LeagueStandingsState(
            seasonIndex = 1,
            selectionSunday = selection,
        )
        val userTeam = "t1"
        val matchup = NcbaOpeningRoundEngine.planFromSelection(selection).matchups
            .first { it.homeTeamId == userTeam || it.awayTeamId == userTeam }
        val userHome = userTeam == matchup.homeTeamId
        val userResult = LeagueGameResult(
            homeTeamId = matchup.homeTeamId,
            awayTeamId = matchup.awayTeamId,
            homeScore = if (userHome) 72 else 60,
            awayScore = if (userHome) 60 else 72,
            rsWeekIndex = PostseasonCalendar.OPENING_ROUND_WEEK,
        )
        val next = PostseasonWeekBatchEngine.runPostseasonWeek(
            standings = standings,
            postseasonWeek = PostseasonCalendar.OPENING_ROUND_WEEK,
            userTeamId = userTeam,
            userResult = userResult,
            conferenceIdByTeam = emptyMap(),
            seed = 42L,
        )
        val openingDone = next.ncbaOpeningRound
        assertNotNull(openingDone)
        assertTrue(openingDone!!.completed)
        assertEquals(NcbaOpeningRoundPairing.OPENING_GAME_COUNT, openingDone.gameResults.size)
        assertEquals(userResult.homeScore, openingDone.gameResults.first { it.homeTeamId == matchup.homeTeamId }.homeScore)
        assertEquals(PostseasonCalendar.OPENING_ROUND_WEEK, next.lastCommittedPostseasonWeek)
    }
}

package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NationalTournamentInvite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SingleEliminationBracketEngineTest {
    @Test
    fun sixteenTeamBracketUsesParallelWeeks() {
        val invites = (1..16).map { rank ->
            NationalTournamentInvite(teamId = "t$rank", tournamentId = "HC", seed = rank)
        }
        val plan = SingleEliminationBracketEngine.plan("HC", seasonIndex = 1, invites)
        val done = SingleEliminationBracketEngine.simulate(
            plan,
            weekForRound = PostseasonParallelCalendar::heritageRoundWeek,
            seed = 3L,
        )
        assertTrue(done.completed)
        assertEquals(15, done.gameResults.size)
        assertEquals(2, done.gameResults.first().rsWeekIndex)
        assertEquals(5, done.gameResults.last().rsWeekIndex)
    }
}

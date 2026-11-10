package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ConferenceTournamentUserCalendarTest {
    @Test
    fun mapsUserGamesByConfTourneyWeek() {
        val result = ConferenceTournamentResult(
            conferenceId = "conf-a",
            templateId = "tpl",
            qualifiedTeamIds = listOf("home", "away"),
            championTeamId = "home",
            runnerUpTeamId = "away",
            finishOrder = listOf("home", "away"),
            ncbaAutoBidTeamId = "home",
            gameResults = listOf(
                LeagueGameResult("home", "away", 70, 65, rsWeekIndex = 2),
            ),
        )
        val byWeek = ConferenceTournamentUserCalendar.userGamesByWeek(
            result,
            userTeamId = "home",
            opponentDisplayName = { it.uppercase() },
        )
        assertEquals(1, byWeek[2]?.size)
        assertEquals("AWAY", byWeek[2]?.single()?.opponentDisplayName)
    }
}

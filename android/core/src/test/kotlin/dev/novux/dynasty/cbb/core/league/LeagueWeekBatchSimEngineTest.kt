package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import org.junit.Assert.assertEquals
import org.junit.Test

class LeagueWeekBatchSimEngineTest {
    @Test
    fun batchSimUpdatesStandingsExcludingUser() {
        val schedule = LeagueScheduleSeason(
            seasonIndex = 1,
            teamsScheduled = 4,
            schedules = listOf(
                LeagueTeamSchedule(
                    "user",
                    "c1",
                    listOf(stub("user", "x", week = 2, home = true)),
                ),
                LeagueTeamSchedule(
                    "a",
                    "c1",
                    listOf(stub("a", "b", week = 2, home = true)),
                ),
                LeagueTeamSchedule(
                    "b",
                    "c1",
                    listOf(stub("b", "a", week = 2, home = false)),
                ),
                LeagueTeamSchedule("x", "c1", listOf(stub("x", "user", week = 2, home = false))),
            ),
        )
        val boot = LeagueWeekBatchSimEngine.bootstrap(1, listOf("user", "a", "b", "x"))
        val result = LeagueWeekBatchSimEngine.runRegularSeasonWeek(
            schedule = schedule,
            standings = boot,
            rsWeekIndex = 2,
            userTeamId = "user",
            userResult = LeagueGameResult("user", "x", 70, 65, 2),
            seed = 11,
        )
        assertEquals(1, result.gamesSimmed)
        val a = result.standings.recordFor("a")
        val b = result.standings.recordFor("b")
        assertEquals(1, a.wins + a.losses)
        assertEquals(1, b.wins + b.losses)
        val user = result.standings.recordFor("user")
        assertEquals(1, user.wins)
        assertEquals(0, user.losses)
    }

    private fun stub(self: String, opp: String, week: Int, home: Boolean) = LeagueGameStub(
        kind = ScheduleGameKind.Conference,
        opponentTeamId = opp,
        homeGame = home,
        rsWeekIndex = week,
    )
}

package dev.novux.dynasty.cbb.core.league

object LeagueMatchupIndex {
    fun uniqueRsMatchups(schedule: LeagueScheduleSeason, rsWeekIndex: Int): List<LeagueMatchup> {
        val seen = mutableSetOf<String>()
        val matchups = mutableListOf<LeagueMatchup>()
        for ((teamId, stub) in schedule.gamesInRsWeek(rsWeekIndex)) {
            val opponentId = stub.opponentTeamId
            if (opponentId.startsWith("event-")) continue
            val key = listOf(teamId, opponentId).sorted().joinToString("|")
            if (!seen.add(key)) continue
            val homeTeam = if (stub.homeGame) teamId else opponentId
            val awayTeam = if (stub.homeGame) opponentId else teamId
            matchups += LeagueMatchup(
                homeTeamId = homeTeam,
                awayTeamId = awayTeam,
                rsWeekIndex = rsWeekIndex,
            )
        }
        return matchups
    }
}

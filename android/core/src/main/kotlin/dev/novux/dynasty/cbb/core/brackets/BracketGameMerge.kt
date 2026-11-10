package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup

internal object BracketGameMerge {
    fun alreadyPlayed(results: List<LeagueGameResult>, home: String, away: String): Boolean =
        results.any {
            (it.homeTeamId == home && it.awayTeamId == away) ||
                (it.homeTeamId == away && it.awayTeamId == home)
        }

    fun resolveGame(
        home: String,
        away: String,
        postseasonWeek: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): LeagueGameResult {
        val involvesUser = userTeamId == home || userTeamId == away
        if (involvesUser && userResult != null) {
            require(
                (userResult.homeTeamId == home && userResult.awayTeamId == away) ||
                    (userResult.homeTeamId == away && userResult.awayTeamId == home),
            )
            return userResult.copy(rsWeekIndex = postseasonWeek)
        }
        return LeagueFastSim.simGame(LeagueMatchup(home, away, postseasonWeek), seed)
    }

    fun winnerOf(result: LeagueGameResult): String =
        if (result.homeScore > result.awayScore) result.homeTeamId else result.awayTeamId
}

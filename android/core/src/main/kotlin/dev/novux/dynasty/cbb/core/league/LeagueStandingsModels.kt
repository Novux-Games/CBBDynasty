package dev.novux.dynasty.cbb.core.league

import kotlinx.serialization.Serializable

@Serializable
data class LeagueTeamRecord(
    val teamId: String,
    val wins: Int = 0,
    val losses: Int = 0,
)

@Serializable
data class LeagueGameResult(
    val homeTeamId: String,
    val awayTeamId: String,
    val homeScore: Int,
    val awayScore: Int,
    val rsWeekIndex: Int,
)

@Serializable
data class LeagueStandingsState(
    val seasonIndex: Int,
    val records: Map<String, LeagueTeamRecord> = emptyMap(),
    val lastCommittedRsWeek: Int = 0,
    val lastWeekResults: List<LeagueGameResult> = emptyList(),
    val aiInjuryRollsLastWeek: Int = 0,
) {
    fun recordFor(teamId: String): LeagueTeamRecord =
        records[teamId] ?: LeagueTeamRecord(teamId = teamId)

    fun withResult(result: LeagueGameResult): LeagueStandingsState {
        val homeWon = result.homeScore > result.awayScore
        val home = recordFor(result.homeTeamId)
        val away = recordFor(result.awayTeamId)
        val nextHome = if (homeWon) home.copy(wins = home.wins + 1) else home.copy(losses = home.losses + 1)
        val nextAway = if (homeWon) away.copy(losses = away.losses + 1) else away.copy(wins = away.wins + 1)
        return copy(
            records = records + (result.homeTeamId to nextHome) + (result.awayTeamId to nextAway),
            lastWeekResults = lastWeekResults + result,
        )
    }
}

data class LeagueMatchup(
    val homeTeamId: String,
    val awayTeamId: String,
    val rsWeekIndex: Int,
)

data class LeagueWeekBatchSimResult(
    val standings: LeagueStandingsState,
    val gamesSimmed: Int,
    val aiInjuryRolls: Int,
)

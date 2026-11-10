package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex

data class ConferenceTournamentHubGameLine(
    val confTourneyWeek: Int,
    val homeTeamId: String,
    val awayTeamId: String,
    val homeScore: Int,
    val awayScore: Int,
)

data class ConferenceTournamentHubSnapshot(
    val conferenceId: String,
    val conferenceDisplayName: String,
    val userTeamId: String,
    val leagueMaxConfWeeks: Int,
    val structuralByeWeeks: List<Int>,
    val qualifiedTeamIds: List<String>,
    val teamDisplayNames: Map<String, String>,
    val bracketSimmed: Boolean,
    val championTeamId: String?,
    val runnerUpTeamId: String?,
    val ncbaAutoBidTeamId: String?,
    val games: List<ConferenceTournamentHubGameLine>,
)

object ConferenceTournamentHubSnapshotFactory {
    fun build(
        index: IdentityPackIndex,
        userTeamId: String,
        leagueMaxConfWeeks: Int,
        structuralByeWeeks: List<Int>,
        templateQualifiedIds: List<String>,
        result: ConferenceTournamentResult?,
    ): ConferenceTournamentHubSnapshot? {
        val team = index.team(userTeamId) ?: return null
        val conf = index.conference(team.conferenceId) ?: return null
        val names = (templateQualifiedIds + listOfNotNull(result?.championTeamId, result?.runnerUpTeamId))
            .distinct()
            .associateWith { id -> index.team(id)?.displayName ?: id }
        val games = result?.gameResults.orEmpty().map { it.toHubLine() }
        return ConferenceTournamentHubSnapshot(
            conferenceId = conf.conferenceId,
            conferenceDisplayName = conf.displayName,
            userTeamId = userTeamId,
            leagueMaxConfWeeks = leagueMaxConfWeeks,
            structuralByeWeeks = structuralByeWeeks,
            qualifiedTeamIds = result?.qualifiedTeamIds ?: templateQualifiedIds,
            teamDisplayNames = names,
            bracketSimmed = result != null,
            championTeamId = result?.championTeamId,
            runnerUpTeamId = result?.runnerUpTeamId,
            ncbaAutoBidTeamId = result?.ncbaAutoBidTeamId,
            games = games,
        )
    }

    private fun LeagueGameResult.toHubLine() = ConferenceTournamentHubGameLine(
        confTourneyWeek = rsWeekIndex,
        homeTeamId = homeTeamId,
        awayTeamId = awayTeamId,
        homeScore = homeScore,
        awayScore = awayScore,
    )
}

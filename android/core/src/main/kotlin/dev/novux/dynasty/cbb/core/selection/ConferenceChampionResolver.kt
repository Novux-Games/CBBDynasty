package dev.novux.dynasty.cbb.core.selection

import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentSeason
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay

/**
 * Placeholder until conference tournaments land — best NET per conference among eligible teams.
 */
object ConferenceChampionResolver {
    fun fromTournamentSeason(season: ConferenceTournamentSeason): Set<String> =
        season.resultsByConference.values.map { it.championTeamId }.toSet()

    fun topNetChampionPerConference(
        teams: List<TeamDisplayOverlay>,
        netRankByTeam: Map<String, Int>,
        ncbaAutoBidEligible: (TeamDisplayOverlay) -> Boolean = { it.postseasonEligible },
    ): Set<String> =
        teams
            .groupBy { it.conferenceId }
            .mapNotNull { (_, confTeams) ->
                confTeams
                    .filter(ncbaAutoBidEligible)
                    .minByOrNull { netRankByTeam[it.teamId] ?: Int.MAX_VALUE }
                    ?.teamId
            }
            .toSet()
}

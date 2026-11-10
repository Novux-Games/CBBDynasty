package dev.novux.dynasty.cbb.data.conference

import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentEngine
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentSeason
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository

class ConferenceTournamentRepository(
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): ConferenceTournamentSeason? =
        leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)?.conferenceTournaments

    suspend fun simulateAndPersist(dynastyId: String, seasonIndex: Int, seed: Long): ConferenceTournamentSeason? {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null
        val index = packRepository.activeSession().index
        val pack = index.source
        val conferences = pack.conferences.mapNotNull { conf ->
            val templateId = conf.tournamentTemplateId ?: return@mapNotNull null
            val template = index.tournamentTemplate(templateId) ?: return@mapNotNull null
            conf.id to template
        }
        val teamsByConf = pack.teams.groupBy({ it.conferenceId }, { it.id })
        val postseasonEligible = pack.teams.associate { it.id to it.postseasonEligible }
        val season = ConferenceTournamentEngine.simulateLeague(
            conferences = conferences,
            records = standings.records,
            teamsByConference = teamsByConf,
            postseasonEligible = postseasonEligible,
            seasonIndex = seasonIndex,
            seed = seed,
        )
        leagueWeekBatchRepository.persistStandings(
            dynastyId,
            standings.copy(conferenceTournaments = season),
        )
        return season
    }
}

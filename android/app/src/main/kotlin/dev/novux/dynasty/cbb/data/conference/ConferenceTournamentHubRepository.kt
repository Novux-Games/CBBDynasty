package dev.novux.dynasty.cbb.data.conference

import dev.novux.dynasty.cbb.core.calendar.ConferenceTournamentCalendar
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentHubSnapshot
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentHubSnapshotFactory
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentQualifier
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class ConferenceTournamentHubRepository(
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val database: DynastyDatabase,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): ConferenceTournamentHubSnapshot? {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        val session = packRepository.activeSession()
        val index = session.index
        val pack = index.source
        val team = index.team(coach.userTeamId) ?: return null
        val confId = team.conferenceId
        val conf = index.conference(confId) ?: return null
        val templateId = conf.tournamentTemplateId ?: return null
        val template = index.tournamentTemplate(templateId) ?: return null
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)
        val records = standings?.records.orEmpty()
        val confTeams = index.teamsInConference(confId).map { it.teamId }
        val confRecords = confTeams.associateWith { records[it] ?: dev.novux.dynasty.cbb.core.league.LeagueTeamRecord(it) }
        val templateQualified = ConferenceTournamentQualifier.qualify(template, confTeams, confRecords)
        val result = standings?.conferenceTournaments?.resultsByConference?.get(confId)
        val maxWeeks = ConferenceTournamentCalendar.leagueMaxConfTourneyWeeks(pack)
        val structuralByes = ConferenceTournamentCalendar.byeWeeksForConference(pack, confId)
        return ConferenceTournamentHubSnapshotFactory.build(
            index = index,
            userTeamId = coach.userTeamId,
            leagueMaxConfWeeks = maxWeeks,
            structuralByeWeeks = structuralByes,
            templateQualifiedIds = templateQualified,
            result = result,
        )
    }
}

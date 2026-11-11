package dev.novux.dynasty.cbb.data.calendar

import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.ConferenceTournamentCalendar
import dev.novux.dynasty.cbb.core.calendar.UserCalendarTabBuilder
import dev.novux.dynasty.cbb.core.calendar.UserCalendarWeekRow
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentQualifier
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentUserCalendar
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class UserCalendarRepository(
    private val scheduleRepository: dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository,
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val database: DynastyDatabase,
) {
    suspend fun loadCalendarRows(
        dynastyId: String,
        seasonIndex: Int,
        activePosition: CalendarPosition,
    ): List<UserCalendarWeekRow> {
        val plan = scheduleRepository.loadPlan(dynastyId, seasonIndex)
        val exhibitionCount = scheduleRepository.loadExhibitionCount(dynastyId, seasonIndex)
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId)
        val session = packRepository.activeSession()
        val index = session.index
        val confByeWeeks = if (coach != null) {
            val team = index.team(coach.userTeamId)
            if (team != null) {
                ConferenceTournamentCalendar.byeWeeksForConference(index.source, team.conferenceId)
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)
        val confContext = if (coach != null) {
            buildConferenceCalendarContext(
                coach.userTeamId,
                standings?.records.orEmpty(),
                standings?.conferenceTournaments,
            )
        } else {
            ConferenceCalendarContext()
        }
        return UserCalendarTabBuilder.build(
            activePosition = activePosition,
            plan = plan,
            exhibitionCount = exhibitionCount,
            conferenceTourneyByeWeeks = confByeWeeks,
            conferenceTourneyGamesByWeek = confContext.gamesByWeek,
            conferenceTourneyQualified = confContext.qualified,
            conferenceTourneySimmed = confContext.bracketSimmed,
        )
    }

    private suspend fun buildConferenceCalendarContext(
        userTeamId: String,
        records: Map<String, LeagueTeamRecord>,
        season: dev.novux.dynasty.cbb.core.conference.ConferenceTournamentSeason?,
    ): ConferenceCalendarContext {
        val session = packRepository.activeSession()
        val index = session.index
        val team = index.team(userTeamId) ?: return ConferenceCalendarContext()
        val confId = team.conferenceId
        val conf = index.conference(confId) ?: return ConferenceCalendarContext()
        val templateId = conf.tournamentTemplateId ?: return ConferenceCalendarContext()
        val template = index.tournamentTemplate(templateId) ?: return ConferenceCalendarContext()
        val confTeams = index.teamsInConference(confId).map { it.teamId }
        val confRecords = confTeams.associateWith { records[it] ?: LeagueTeamRecord(it) }
        val templateQualified = ConferenceTournamentQualifier.qualify(template, confTeams, confRecords)
        val result = season?.resultsByConference?.get(confId)
        val qualified = ConferenceTournamentUserCalendar.userQualified(result, templateQualified, userTeamId)
        val gamesByWeek = if (result != null) {
            ConferenceTournamentUserCalendar.userGamesByWeek(
                result,
                userTeamId,
            ) { opponentId -> index.team(opponentId)?.displayName ?: opponentId }
        } else {
            emptyMap()
        }
        return ConferenceCalendarContext(
            qualified = qualified,
            bracketSimmed = result != null,
            gamesByWeek = gamesByWeek,
        )
    }

    private data class ConferenceCalendarContext(
        val qualified: Boolean = true,
        val bracketSimmed: Boolean = false,
        val gamesByWeek: Map<Int, List<dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub>> = emptyMap(),
    )
}

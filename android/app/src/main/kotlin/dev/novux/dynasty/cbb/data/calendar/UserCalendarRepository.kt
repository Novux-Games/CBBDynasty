package dev.novux.dynasty.cbb.data.calendar

import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.ConferenceTournamentCalendar
import dev.novux.dynasty.cbb.core.calendar.UserCalendarTabBuilder
import dev.novux.dynasty.cbb.core.calendar.UserCalendarWeekRow
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class UserCalendarRepository(
    private val scheduleRepository: PreseasonScheduleRepository,
    private val packRepository: PackRepository,
    private val database: DynastyDatabase,
) {
    suspend fun loadCalendarRows(
        dynastyId: String,
        seasonIndex: Int,
        activePosition: CalendarPosition,
    ): List<UserCalendarWeekRow> {
        val plan = scheduleRepository.loadPlan(dynastyId, seasonIndex)
        val exhibitionCount = scheduleRepository.loadExhibitionCount(dynastyId, seasonIndex)
        val confByeWeeks = conferenceByeWeeks(dynastyId)
        return UserCalendarTabBuilder.build(
            activePosition = activePosition,
            plan = plan,
            exhibitionCount = exhibitionCount,
            conferenceTourneyByeWeeks = confByeWeeks,
        )
    }

    private fun conferenceByeWeeks(dynastyId: String): List<Int> {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return emptyList()
        val session = packRepository.activeSession()
        val team = session.index.team(coach.userTeamId) ?: return emptyList()
        return ConferenceTournamentCalendar.byeWeeksForConference(session.index.source, team.conferenceId)
    }
}

package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import kotlinx.serialization.Serializable

@Serializable
data class LeagueGameStub(
    val kind: ScheduleGameKind,
    val opponentTeamId: String,
    val homeGame: Boolean,
    val rsWeekIndex: Int,
)

@Serializable
data class LeagueTeamSchedule(
    val teamId: String,
    val conferenceId: String,
    val games: List<LeagueGameStub>,
) {
    val rsGameCount: Int get() = games.size
}

@Serializable
data class LeagueScheduleSeason(
    val seasonIndex: Int,
    val teamsScheduled: Int,
    val schedules: List<LeagueTeamSchedule>,
) {
    fun scheduleFor(teamId: String): LeagueTeamSchedule? =
        schedules.firstOrNull { it.teamId == teamId }

    fun gamesInRsWeek(rsWeekIndex: Int): List<Pair<String, LeagueGameStub>> =
        schedules.flatMap { team ->
            team.games.filter { it.rsWeekIndex == rsWeekIndex }.map { team.teamId to it }
        }
}

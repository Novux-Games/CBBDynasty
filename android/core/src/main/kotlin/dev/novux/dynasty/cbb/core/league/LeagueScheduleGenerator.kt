package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.pack.ConferenceDisplayOverlay
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import dev.novux.dynasty.cbb.core.preseason.tournament.RegularSeasonSlotBudget
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInviteResolution
import dev.novux.dynasty.cbb.core.schedule.PreseasonScheduleSolver
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub

/**
 * Builds RS schedules for every D-I program in the active pack ([planning/game-design.md#world-scale--league-simulation]).
 */
object LeagueScheduleGenerator {
    fun generate(
        teams: List<TeamDisplayOverlay>,
        conferences: Map<String, ConferenceDisplayOverlay>,
        seasonIndex: Int,
        dynastySeed: Long,
    ): LeagueScheduleSeason {
        require(teams.isNotEmpty()) { "league teams required" }
        val byConference = teams.groupBy { it.conferenceId }
        val schedules = teams.map { team ->
            val conference = conferences[team.conferenceId]
                ?: error("missing conference ${team.conferenceId} for ${team.teamId}")
            val confOpponents = byConference[team.conferenceId].orEmpty()
                .filter { it.teamId != team.teamId }
            val nonConference = teams.filter { it.conferenceId != team.conferenceId }
            val plan = PreseasonScheduleSolver.solve(
                userTeam = team,
                conference = conference,
                conferenceOpponents = confOpponents,
                nonConferencePool = nonConference,
                budget = RegularSeasonSlotBudget(
                    eventRsGames = 0,
                    builderSlots = RegularSeasonSlotBudget.RS_CAP,
                ),
                tournamentResolution = TournamentInviteResolution.DeclinedAll,
                exhibitionCount = 0,
                seed = dynastySeed xor team.teamId.hashCode().toLong() xor seasonIndex.toLong(),
            )
            LeagueTeamSchedule(
                teamId = team.teamId,
                conferenceId = team.conferenceId,
                games = plan.regularSeason.map { it.toLeagueStub() },
            )
        }
        return LeagueScheduleSeason(
            seasonIndex = seasonIndex,
            teamsScheduled = schedules.size,
            schedules = schedules,
        )
    }

    private fun ScheduledGameStub.toLeagueStub(): LeagueGameStub = LeagueGameStub(
        kind = kind,
        opponentTeamId = opponentTeamId,
        homeGame = homeGame,
        rsWeekIndex = rsWeekIndex ?: 1,
    )
}

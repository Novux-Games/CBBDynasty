package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

object LeagueWeekBatchSimEngine {
    fun bootstrap(seasonIndex: Int, teamIds: Collection<String>): LeagueStandingsState =
        LeagueStandingsState(
            seasonIndex = seasonIndex,
            records = teamIds.associateWith { LeagueTeamRecord(teamId = it) },
        )

    fun runRegularSeasonWeek(
        schedule: LeagueScheduleSeason,
        standings: LeagueStandingsState,
        rsWeekIndex: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): LeagueWeekBatchSimResult {
        var state = standings.copy(lastWeekResults = emptyList(), aiInjuryRollsLastWeek = 0)
        val matchups = LeagueMatchupIndex.uniqueRsMatchups(schedule, rsWeekIndex)
        var simmed = 0
        matchups.forEach { matchup ->
            if (matchup.homeTeamId == userTeamId || matchup.awayTeamId == userTeamId) {
                return@forEach
            }
            val result = LeagueFastSim.simGame(matchup, seed + simmed)
            state = state.withResult(result)
            simmed++
        }
        userResult?.let { state = state.withResult(it) }
        val injuries = AiLeagueWeekOps.rollInjuriesForGames(simmed + (if (userResult != null) 1 else 0), seed)
        AiLeagueWeekOps.lineupAdjustTeamsAffected(injuries)
        return LeagueWeekBatchSimResult(
            standings = state.copy(
                lastCommittedRsWeek = rsWeekIndex,
                aiInjuryRollsLastWeek = injuries,
            ),
            gamesSimmed = simmed,
            aiInjuryRolls = injuries,
        )
    }

    fun phaseSupportsBatch(phase: CalendarPhase): Boolean =
        phase == CalendarPhase.RegularSeason || phase == CalendarPhase.Postseason
}

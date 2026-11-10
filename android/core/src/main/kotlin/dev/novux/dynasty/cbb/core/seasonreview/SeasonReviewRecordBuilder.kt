package dev.novux.dynasty.cbb.core.seasonreview

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord

object SeasonReviewRecordBuilder {
    fun build(
        teamId: String,
        standings: LeagueStandingsState,
        packIndex: IdentityPackIndex,
    ): SchoolSeasonRecord {
        val overall = standings.recordFor(teamId)
        val conferenceId = packIndex.team(teamId)?.conferenceId
        if (conferenceId == null) {
            return SchoolSeasonRecord(overall.wins, overall.losses, 0, 0)
        }
        var confWins = 0
        var confLosses = 0
        standings.seasonResults.forEach { game ->
            val opponent = when (teamId) {
                game.homeTeamId -> game.awayTeamId
                game.awayTeamId -> game.homeTeamId
                else -> return@forEach
            }
            val oppConf = packIndex.team(opponent)?.conferenceId ?: return@forEach
            if (oppConf != conferenceId) return@forEach
            val teamWon = when (teamId) {
                game.homeTeamId -> game.homeScore > game.awayScore
                else -> game.awayScore > game.homeScore
            }
            if (teamWon) confWins++ else confLosses++
        }
        return SchoolSeasonRecord(overall.wins, overall.losses, confWins, confLosses)
    }
}

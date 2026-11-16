package dev.novux.dynasty.cbb.core.coach.card.history

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState

/** Conference RS + tourney title chips on History rows ([MEGA-COACH-130]). */
object CoachSeasonHistoryChampionshipHighlightPolicy {
    const val MEGA_ID = "MEGA-COACH-130"

    fun highlightChips(
        standings: LeagueStandingsState?,
        userTeamId: String,
        conferenceId: String?,
        conferenceTeamIds: Set<String>,
    ): List<String> {
        if (standings == null || conferenceId.isNullOrBlank()) return emptyList()
        return buildList {
            if (conferenceTeamIds.isNotEmpty() && isConfRegularSeasonChampion(standings, userTeamId, conferenceTeamIds)) {
                add("Conf RS champion")
            }
            standings.conferenceTournaments?.resultsByConference?.get(conferenceId)?.let { result ->
                if (result.championTeamId == userTeamId) add("Conf tournament champion")
            }
        }
    }

    private fun isConfRegularSeasonChampion(
        standings: LeagueStandingsState,
        userTeamId: String,
        conferenceTeamIds: Set<String>,
    ): Boolean {
        if (userTeamId !in conferenceTeamIds) return false
        val userRec = conferenceWinPct(standings, userTeamId, conferenceTeamIds) ?: return false
        val leaders = conferenceTeamIds.mapNotNull { conferenceWinPct(standings, it, conferenceTeamIds) }
        if (leaders.isEmpty()) return false
        val top = leaders.max()
        return userRec == top && leaders.count { it == top } == 1
    }

    private fun conferenceWinPct(
        standings: LeagueStandingsState,
        teamId: String,
        conferenceTeamIds: Set<String>,
    ): Double? {
        var wins = 0
        var losses = 0
        standings.seasonResults.forEach { game ->
            if (game.homeTeamId !in conferenceTeamIds || game.awayTeamId !in conferenceTeamIds) return@forEach
            if (game.homeTeamId != teamId && game.awayTeamId != teamId) return@forEach
            val userHome = game.homeTeamId == teamId
            val userWon = (userHome && game.homeScore > game.awayScore) ||
                (!userHome && game.awayScore > game.homeScore)
            if (userWon) wins++ else losses++
        }
        if (wins + losses == 0) return null
        return wins.toDouble() / (wins + losses).toDouble()
    }
}
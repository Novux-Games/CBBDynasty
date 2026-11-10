package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState

object PostseasonUserGameResolver {
    fun findMatchup(
        standings: LeagueStandingsState,
        userTeamId: String,
        postseasonWeek: Int,
    ): Pair<String, String>? {
        standings.ncbaOpeningRound?.takeIf { postseasonWeek == PostseasonCalendar.OPENING_ROUND_WEEK }?.let { opening ->
            opening.matchups.forEach { m ->
                if (userTeamId == m.homeTeamId || userTeamId == m.awayTeamId) {
                    return m.homeTeamId to m.awayTeamId
                }
            }
        }
        standings.ncbaMainBracket?.let { main ->
            main.entrants.firstOrNull { it.teamId == userTeamId } ?: return@let
            when (postseasonWeek) {
                in 2..5 -> {
                    val roundIndex = postseasonWeek - PostseasonCalendar.OPENING_ROUND_WEEK - 1
                    val region = main.entrants.first { it.teamId == userTeamId }.regionId
                    NcbaMainBracketWeekEngine.userRegionalPairings(main, region, roundIndex)
                        .firstOrNull { userTeamId == it.first || userTeamId == it.second }
                        ?.let { return it }
                }
                6 -> {
                    val champs = main.regionalChampions
                    if (champs.size == 4) {
                        listOf(
                            champs.getValue(NcbaRegionId.East) to champs.getValue(NcbaRegionId.West),
                            champs.getValue(NcbaRegionId.South) to champs.getValue(NcbaRegionId.Midwest),
                        ).firstOrNull { userTeamId == it.first || userTeamId == it.second }?.let { return it }
                    }
                }
                7 -> {
                    val semiWeek = 6
                    val winners = main.gameResults.filter { it.rsWeekIndex == semiWeek }
                        .map { if (it.homeScore > it.awayScore) it.homeTeamId else it.awayTeamId }
                    if (winners.size == 2 && userTeamId in winners) {
                        return winners[0] to winners[1]
                    }
                }
            }
        }
        standings.nciBracket?.invites?.any { it.teamId == userTeamId }?.takeIf { it }?.let {
            return invitationalMatchup(standings.nciBracket!!, userTeamId, postseasonWeek, PostseasonParallelCalendar::nciRoundWeek, PostseasonParallelCalendar.NCI_ROUNDS)
        }
        standings.heritageBracket?.invites?.any { it.teamId == userTeamId }?.let { inField ->
            if (inField) {
                return invitationalMatchup(
                    standings.heritageBracket!!,
                    userTeamId,
                    postseasonWeek,
                    PostseasonParallelCalendar::heritageRoundWeek,
                    PostseasonParallelCalendar.HERITAGE_ROUNDS,
                )
            }
        }
        return null
    }

    private fun invitationalMatchup(
        snapshot: NationalBracketSnapshot,
        userTeamId: String,
        week: Int,
        weekForRound: (Int) -> Int,
        maxRounds: Int,
    ): Pair<String, String>? {
        val round = SingleEliminationWeekEngine.roundIndexForWeek(week, weekForRound, maxRounds) ?: return null
        val ordered = snapshot.invites.sortedBy { it.seed }.map { it.teamId }
        var alive = ordered
        var pairingRound = 0
        while (pairingRound < round) {
            val w = weekForRound(pairingRound)
            val roundGames = snapshot.gameResults.filter { it.rsWeekIndex == w }
            if (roundGames.isEmpty()) return null
            alive = roundGames.map { BracketGameMerge.winnerOf(it) }
            pairingRound++
        }
        val pairings = if (round == 0) {
            dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine.openingRoundPairings(alive)
        } else {
            dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine.nextRoundPairings(alive)
        }
        return pairings.firstOrNull { userTeamId == it.first || userTeamId == it.second }
    }
}

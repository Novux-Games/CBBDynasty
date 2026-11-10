package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine

object SingleEliminationWeekEngine {
    fun roundIndexForWeek(
        week: Int,
        weekForRound: (roundIndex: Int) -> Int,
        maxRounds: Int,
    ): Int? =
        (0 until maxRounds).firstOrNull { weekForRound(it) == week }

    fun simulatePostseasonWeek(
        snapshot: NationalBracketSnapshot,
        postseasonWeek: Int,
        weekForRound: (roundIndex: Int) -> Int,
        maxRounds: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NationalBracketSnapshot {
        if (snapshot.completed) return snapshot
        val roundIndex = roundIndexForWeek(postseasonWeek, weekForRound, maxRounds) ?: return snapshot
        if (snapshot.gameResults.any { it.rsWeekIndex == postseasonWeek }) return snapshot
        val ordered = snapshot.invites.sortedBy { it.seed }.map { it.teamId }
        var alive = ordered
        var pairingRound = 0
        while (pairingRound < roundIndex) {
            val week = weekForRound(pairingRound)
            val roundGames = snapshot.gameResults.filter { it.rsWeekIndex == week }
            if (roundGames.isEmpty()) return snapshot
            alive = roundGames.map { BracketGameMerge.winnerOf(it) }
            pairingRound++
        }
        val pairings = if (roundIndex == 0) {
            EarlySeasonBracketEngine.openingRoundPairings(alive)
        } else {
            EarlySeasonBracketEngine.nextRoundPairings(alive)
        }
        val played = snapshot.gameResults.toMutableList()
        pairings.forEachIndexed { index, (home, away) ->
            val game = BracketGameMerge.resolveGame(
                home = home,
                away = away,
                postseasonWeek = postseasonWeek,
                userTeamId = userTeamId,
                userResult = userResult,
                seed = seed + roundIndex * 500L + index,
            )
            played += game
        }
        val winners = played.filter { it.rsWeekIndex == postseasonWeek }.map { BracketGameMerge.winnerOf(it) }
        val completed = roundIndex == maxRounds - 1 && winners.size == 1
        return snapshot.copy(
            gameResults = played,
            championTeamId = if (completed) winners.single() else snapshot.championTeamId,
            completed = completed,
        )
    }
}

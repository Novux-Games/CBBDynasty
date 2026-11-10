package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** Simulates NCBA Opening Round (24→12) — [planning/postseason.md#opening-round]. */
object NcbaOpeningRoundEngine {
    fun planFromSelection(selection: SelectionSundaySnapshot): NcbaOpeningRoundSnapshot =
        NcbaOpeningRoundSnapshot(
            seasonIndex = selection.seasonIndex,
            matchups = NcbaOpeningRoundPairing.buildMatchups(selection),
        )

    fun simulate(
        snapshot: NcbaOpeningRoundSnapshot,
        seed: Long,
    ): NcbaOpeningRoundSnapshot {
        require(snapshot.matchups.size == NcbaOpeningRoundPairing.OPENING_GAME_COUNT) {
            "expected ${NcbaOpeningRoundPairing.OPENING_GAME_COUNT} Opening Round games"
        }
        if (snapshot.completed) return snapshot
        val week = PostseasonCalendar.OPENING_ROUND_WEEK
        val results = mutableListOf<LeagueGameResult>()
        val winners = mutableListOf<String>()
        snapshot.matchups.forEachIndexed { index, matchup ->
            val game = LeagueFastSim.simGame(
                LeagueMatchup(
                    homeTeamId = matchup.homeTeamId,
                    awayTeamId = matchup.awayTeamId,
                    rsWeekIndex = week,
                ),
                seed + index * 41L,
            )
            results += game
            val homeWon = game.homeScore > game.awayScore
            winners += if (homeWon) matchup.homeTeamId else matchup.awayTeamId
        }
        return snapshot.copy(
            gameResults = results,
            winnerTeamIds = winners,
            completed = true,
        )
    }

    fun simulatePostseasonWeek(
        snapshot: NcbaOpeningRoundSnapshot,
        postseasonWeek: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NcbaOpeningRoundSnapshot {
        if (snapshot.completed) return snapshot
        require(postseasonWeek == PostseasonCalendar.OPENING_ROUND_WEEK)
        if (snapshot.gameResults.any { it.rsWeekIndex == postseasonWeek }) return snapshot
        val played = snapshot.gameResults.toMutableList()
        val winners = snapshot.winnerTeamIds.toMutableList()
        snapshot.matchups.forEachIndexed { index, matchup ->
            if (BracketGameMerge.alreadyPlayed(played, matchup.homeTeamId, matchup.awayTeamId)) {
                return@forEachIndexed
            }
            val game = BracketGameMerge.resolveGame(
                home = matchup.homeTeamId,
                away = matchup.awayTeamId,
                postseasonWeek = postseasonWeek,
                userTeamId = userTeamId,
                userResult = userResult,
                seed = seed + index * 41L,
            )
            played += game
            winners += BracketGameMerge.winnerOf(game)
        }
        val completed = winners.size == NcbaOpeningRoundPairing.OPENING_GAME_COUNT
        return snapshot.copy(
            gameResults = played,
            winnerTeamIds = winners,
            completed = completed,
        )
    }
}

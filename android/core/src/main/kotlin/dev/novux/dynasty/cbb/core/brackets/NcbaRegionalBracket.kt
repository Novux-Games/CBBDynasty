package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine

/** Single-region NCAA-style pod pairings (1v16 … 2v15). */
object NcbaRegionalBracket {
    private val FIRST_ROUND_REGIONAL_SEEDS = listOf(
        1 to 16,
        8 to 9,
        5 to 12,
        4 to 13,
        6 to 11,
        3 to 14,
        7 to 10,
        2 to 15,
    )

    fun firstRoundPairings(entrants: List<NcbaMainBracketEntrant>): List<Pair<String, String>> {
        require(entrants.size == 16) { "region must have 16 teams" }
        val slot = entrants.sortedBy { it.regionalSeed }
        require(slot.map { it.regionalSeed } == (1..16).toList()) {
            "region seeds must be 1–16 contiguous"
        }
        fun teamAt(regionalSeed: Int) = slot[regionalSeed - 1].teamId
        return FIRST_ROUND_REGIONAL_SEEDS.map { (high, low) ->
            teamAt(high) to teamAt(low)
        }
    }

    fun simulateRound(
        pairings: List<Pair<String, String>>,
        postseasonWeek: Int,
        seed: Long,
    ): Pair<List<String>, List<LeagueGameResult>> {
        val results = mutableListOf<LeagueGameResult>()
        val winners = mutableListOf<String>()
        pairings.forEachIndexed { index, (home, away) ->
            val game = LeagueFastSim.simGame(
                LeagueMatchup(home, away, postseasonWeek),
                seed + index * 17L,
            )
            results += game
            winners += if (game.homeScore > game.awayScore) home else away
        }
        return winners to results
    }

    fun simulateRegion(
        entrants: List<NcbaMainBracketEntrant>,
        startPostseasonWeek: Int,
        seed: Long,
    ): Pair<List<LeagueGameResult>, String> {
        var pairings = firstRoundPairings(entrants)
        var week = startPostseasonWeek
        val allResults = mutableListOf<LeagueGameResult>()
        var winners = emptyList<String>()
        while (pairings.isNotEmpty()) {
            val (roundWinners, results) = simulateRound(pairings, week, seed + week * 1000L)
            allResults += results
            winners = roundWinners
            week++
            pairings = if (roundWinners.size > 1) {
                EarlySeasonBracketEngine.nextRoundPairings(roundWinners)
            } else {
                emptyList()
            }
        }
        require(winners.size == 1)
        return allResults to winners.single()
    }
}

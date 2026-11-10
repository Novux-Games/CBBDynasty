package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine
import dev.novux.dynasty.cbb.core.selection.NationalTournamentInvite

/** High-low seeded single-elimination bracket — [planning/postseason.md]. */
object SingleEliminationBracketEngine {
    fun plan(
        tournamentId: String,
        seasonIndex: Int,
        invites: List<NationalTournamentInvite>,
    ): NationalBracketSnapshot =
        NationalBracketSnapshot(
            tournamentId = tournamentId,
            seasonIndex = seasonIndex,
            invites = invites.sortedBy { it.seed },
        )

    fun simulate(
        snapshot: NationalBracketSnapshot,
        weekForRound: (roundIndex: Int) -> Int,
        seed: Long,
    ): NationalBracketSnapshot {
        if (snapshot.completed) return snapshot
        val ordered = snapshot.invites.sortedBy { it.seed }.map { it.teamId }
        require(ordered.size >= 2 && ordered.size.isPowerOfTwo()) {
            "bracket size must be a power of two"
        }
        var pairings = EarlySeasonBracketEngine.openingRoundPairings(ordered)
        var roundIndex = 0
        val results = mutableListOf<LeagueGameResult>()
        var winners = emptyList<String>()
        while (pairings.isNotEmpty()) {
            val week = weekForRound(roundIndex)
            val roundWinners = mutableListOf<String>()
            pairings.forEachIndexed { gameIndex, (home, away) ->
                val game = LeagueFastSim.simGame(
                    LeagueMatchup(home, away, week),
                    seed + roundIndex * 1_000L + gameIndex * 13L,
                )
                results += game
                roundWinners += if (game.homeScore > game.awayScore) home else away
            }
            winners = roundWinners
            roundIndex++
            pairings = if (roundWinners.size > 1) {
                EarlySeasonBracketEngine.nextRoundPairings(roundWinners)
            } else {
                emptyList()
            }
        }
        require(winners.size == 1)
        return snapshot.copy(
            gameResults = results,
            championTeamId = winners.single(),
            completed = true,
        )
    }

    private fun Int.isPowerOfTwo(): Boolean = this > 0 && (this and (this - 1)) == 0
}

package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.sim.GameSimInput
import dev.novux.dynasty.cbb.core.sim.GameSimTeamInput
import dev.novux.dynasty.cbb.core.sim.PossessionSimEngine
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning

/** Fast batch sim using possession engine when tuning is supplied ([planning/simulation-engine.md]). */
object LeagueFastSim {
    fun simGame(matchup: LeagueMatchup, seed: Long, tuning: SimTuning? = null): LeagueGameResult {
        if (tuning != null) {
            val sim = PossessionSimEngine.simulate(
                GameSimInput(
                    home = GameSimTeamInput(matchup.homeTeamId, DEFAULT_RATING, DEFAULT_RATING),
                    away = GameSimTeamInput(matchup.awayTeamId, DEFAULT_RATING, DEFAULT_RATING),
                    tuning = tuning,
                    seed = seed xor matchup.homeTeamId.hashCode().toLong() xor matchup.awayTeamId.hashCode().toLong(),
                ),
            )
            var home = sim.homeScore
            var away = sim.awayScore
            if (home == away) home += 1
            return LeagueGameResult(
                homeTeamId = matchup.homeTeamId,
                awayTeamId = matchup.awayTeamId,
                homeScore = home,
                awayScore = away,
                rsWeekIndex = matchup.rsWeekIndex,
            )
        }
        val random = kotlin.random.Random(
            seed xor matchup.homeTeamId.hashCode().toLong() xor matchup.awayTeamId.hashCode().toLong(),
        )
        var home = 58 + random.nextInt(40)
        var away = 58 + random.nextInt(40)
        if (home == away) {
            home += if (random.nextBoolean()) 1 else -1
        }
        return LeagueGameResult(
            homeTeamId = matchup.homeTeamId,
            awayTeamId = matchup.awayTeamId,
            homeScore = home.coerceAtLeast(0),
            awayScore = away.coerceAtLeast(0),
            rsWeekIndex = matchup.rsWeekIndex,
        )
    }

    private const val DEFAULT_RATING = 72
}

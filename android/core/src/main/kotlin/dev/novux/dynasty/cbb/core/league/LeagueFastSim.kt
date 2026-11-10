package dev.novux.dynasty.cbb.core.league

import kotlin.random.Random

/** Placeholder possession-free sim for league batch ([planning/simulation-engine.md]). */
object LeagueFastSim {
    fun simGame(matchup: LeagueMatchup, seed: Long): LeagueGameResult {
        val random = Random(seed xor matchup.homeTeamId.hashCode().toLong() xor matchup.awayTeamId.hashCode().toLong())
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
}

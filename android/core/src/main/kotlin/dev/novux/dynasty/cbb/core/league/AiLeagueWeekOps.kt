package dev.novux.dynasty.cbb.core.league

import kotlin.random.Random

/** AI injury rolls + lineup adjust hooks for league batch week ([planning/injuries.md]). */
object AiLeagueWeekOps {
    fun rollInjuriesForGames(gameCount: Int, seed: Long): Int {
        if (gameCount <= 0) return 0
        val random = Random(seed)
        var injuries = 0
        repeat(gameCount) { index ->
            val roll = random.nextDouble()
            if (roll < 0.06) injuries++
            if (roll < 0.012 && index % 3 == 0) injuries++
        }
        return injuries
    }

    fun lineupAdjustTeamsAffected(injuryRolls: Int): Int =
        injuryRolls // future: demote starters per team roster graph
}

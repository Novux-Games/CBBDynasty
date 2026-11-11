package dev.novux.dynasty.cbb.core.sim

import kotlin.random.Random

/**
 * Shooting foul on FGA → 1–3 free throws ([planning/simulation-engine.md#what-can-happen-on-a-possession], MEGA-SIM-094).
 */
object PossessionShootingFoulPolicy {
    const val QA_ID: String = "MEGA-SIM-094"

    private const val AND_ONE_RATE = 0.045
    private const val MISS_SHOOTING_FOUL_RATE = 0.062
    private const val LATE_CLOCK_FOUL_MULT = 1.35

    fun rollAndOne(lateClock: Boolean, random: Random): Boolean {
        val rate = AND_ONE_RATE * if (lateClock) LATE_CLOCK_FOUL_MULT else 1.0
        return random.nextDouble() < rate
    }

    fun rollMissShootingFoul(lateClock: Boolean, random: Random): Boolean {
        val rate = MISS_SHOOTING_FOUL_RATE * if (lateClock) LATE_CLOCK_FOUL_MULT else 1.0
        return random.nextDouble() < rate
    }

    fun attemptsForMissFoul(threePointAttempt: Boolean): Int = if (threePointAttempt) 3 else 2

    fun chainSuffix(andOne: Boolean, attempts: Int): String =
        when {
            andOne -> "and-1 | FT×$attempts"
            attempts > 0 -> "shooting foul | FT×$attempts"
            else -> ""
        }
}

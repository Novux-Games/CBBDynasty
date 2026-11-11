package dev.novux.dynasty.cbb.core.sim

import kotlin.random.Random

/** Blocked shot attempt on miss path ([planning/simulation-engine.md], MEGA-SIM-093). */
object PossessionBlockPolicy {
    const val QA_ID: String = "MEGA-SIM-093"

    private const val BASE_BLOCK_RATE = 0.08
    private const val FOUL_ON_BLOCK_RATE = 0.15

    data class Outcome(
        val blocked: Boolean,
        val foulOnBlock: Boolean = false,
    ) {
        companion object {
            val None = Outcome(blocked = false)
        }
    }

    fun rollOnMiss(defRating: Int, offRating: Int, random: Random): Outcome {
        val edge = (defRating - offRating).coerceIn(-20, 20)
        val rate = (BASE_BLOCK_RATE + edge * 0.002).coerceIn(0.03, 0.18)
        if (random.nextDouble() >= rate) return Outcome.None
        val foul = random.nextDouble() < FOUL_ON_BLOCK_RATE
        return Outcome(blocked = true, foulOnBlock = foul)
    }
}

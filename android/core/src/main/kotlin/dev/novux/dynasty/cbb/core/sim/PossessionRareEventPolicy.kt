package dev.novux.dynasty.cbb.core.sim

import kotlin.random.Random

/** Rare alternate-possession terminals ([MEGA-SIM-096], [MEGA-SIM-107]). */
object PossessionRareEventPolicy {
    const val MEGA_JUMP_BALL_ID = "MEGA-SIM-096"
    const val MEGA_TECHNICAL_ID = "MEGA-SIM-107"

    data class RareTrip(val logLine: String, val flipPossession: Boolean)

    fun maybeSample(random: Random, possessionIndex: Int): RareTrip? {
        if (possessionIndex < 8) return null
        val roll = random.nextInt(400)
        return when (roll) {
            0 -> RareTrip("held ball | jump ball", flipPossession = true)
            1 -> RareTrip("technical foul | 1 FT", flipPossession = false)
            else -> null
        }
    }
}
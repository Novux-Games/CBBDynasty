package dev.novux.dynasty.cbb.core.sim

/** Completed sims break ties via OT ([MEGA-SIM-085]). */
object SimOvertimeTieBreakPolicy {
    const val MEGA_ID: String = "MEGA-SIM-085"

    fun requiresOvertime(homeScore: Int, awayScore: Int, regulationComplete: Boolean): Boolean =
        regulationComplete && homeScore == awayScore
}
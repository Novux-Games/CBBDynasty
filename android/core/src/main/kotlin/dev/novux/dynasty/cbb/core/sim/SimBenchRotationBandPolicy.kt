package dev.novux.dynasty.cbb.core.sim

/** In-game subs pull from minutes bands ([MEGA-SIM-062]). */
object SimBenchRotationBandPolicy {
    const val MEGA_ID: String = "MEGA-SIM-062"

    fun adjustFloorFromTarget(deltaMinutes: Double): Boolean = kotlin.math.abs(deltaMinutes) >= 2.0
}
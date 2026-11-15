package dev.novux.dynasty.cbb.core.sim

/** Usage weights tweak offense share per possession ([MEGA-SIM-047]). */
object SimUsageOffenseSharePolicy {
    const val MEGA_ID: String = "MEGA-SIM-047"

    fun peakUsageMultiplier(usageWeight: Double): Double = 0.75 + usageWeight.coerceIn(0.0, 1.0) * 0.5
}
package dev.novux.dynasty.cbb.core.lineup

/** Show rotation target minutes on strategy rows ([MEGA-SIM-144]). */
object StrategyTargetMinutesDisplayPolicy {
    const val MEGA_ID = "MEGA-SIM-144"

    fun label(targetMinutes: Int?): String =
        targetMinutes?.let { "Target $it MIN" } ?: ""
}
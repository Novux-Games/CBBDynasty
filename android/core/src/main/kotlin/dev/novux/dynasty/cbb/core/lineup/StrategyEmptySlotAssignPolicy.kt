package dev.novux.dynasty.cbb.core.lineup

/** Empty starter slots prompt assign flow ([MEGA-SIM-060]). */
object StrategyEmptySlotAssignPolicy {
    const val MEGA_ID: String = "MEGA-SIM-060"

    fun needsAssignPrompt(playerId: String?): Boolean = playerId == null
}
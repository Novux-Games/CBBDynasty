package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Re-insert starters from prep at 2nd-half tip ([MEGA-SIM-110]). */
object HalftimeOpeningFivePolicy {
    const val MEGA_ID = "MEGA-SIM-110"

    fun logLine(starters: Map<PlayerPosition, String>): String? {
        if (starters.isEmpty()) return null
        val ids = PlayerPosition.STARTER_ORDER.mapNotNull { starters[it] }
        if (ids.isEmpty()) return null
        return "2H opening five | ${ids.joinToString(", ")}"
    }

    fun logLineFromRoster(roster: GameSimRosterInput?): String? {
        val ids = roster?.participants
            ?.sortedByDescending { it.targetMinutes }
            ?.take(5)
            ?.map { it.logDisplayName.ifBlank { "#${it.jerseyNumber}" } }
            ?: return null
        if (ids.isEmpty()) return null
        return "2H opening five | ${ids.joinToString(", ")}"
    }

    fun shouldApplyAtSecondHalfStart(gameClockSeconds: Int, inOvertime: Boolean): Boolean =
        !inOvertime && gameClockSeconds == GameSimClock.HALF_SECONDS
}
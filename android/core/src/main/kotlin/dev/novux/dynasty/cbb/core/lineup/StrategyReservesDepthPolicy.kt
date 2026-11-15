package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Strategy bench sorted by depth order ([MEGA-SIM-063]). */
object StrategyReservesDepthPolicy {
    const val MEGA_ID: String = "MEGA-SIM-063"

    fun sortReserves(players: List<RosterPlayer>, starterIds: Set<String>): List<RosterPlayer> =
        players.filter { it.playerId !in starterIds }.sortedBy { it.depthOrder }
}
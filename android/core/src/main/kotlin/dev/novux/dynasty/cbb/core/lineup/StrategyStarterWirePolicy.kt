package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Strategy UI passes user starter map into sim prep ([MEGA-SIM-058], [MEGA-SIM-064]). */
object StrategyStarterWirePolicy {
    const val MEGA_WIRE_058 = "MEGA-SIM-058"
    const val MEGA_WIRE_064 = "MEGA-SIM-064"

    fun wireForSim(starters: Map<PlayerPosition, String>): Map<PlayerPosition, String> = starters
}
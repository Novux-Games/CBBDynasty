package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Reset starters invokes listed-position default algorithm ([MEGA-SIM-142]). */
object StrategyResetStartersPolicy {
    const val MEGA_ID = "MEGA-SIM-142"

    fun reset(
        players: List<RosterPlayer>,
        staminaThreshold: Int,
    ): Map<PlayerPosition, String> = DefaultStarterAlgorithm.pickStarters(players, staminaThreshold)
}
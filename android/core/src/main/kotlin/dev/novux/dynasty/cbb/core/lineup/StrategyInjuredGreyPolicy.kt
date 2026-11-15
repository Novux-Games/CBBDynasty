package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Injured / ineligible greyed on Strategy cards ([MEGA-SIM-075]). */
object StrategyInjuredGreyPolicy {
    const val MEGA_ID: String = "MEGA-SIM-075"

    fun dimCard(player: RosterPlayer, ineligible: Boolean): Boolean =
        player.injuryActive || ineligible
}
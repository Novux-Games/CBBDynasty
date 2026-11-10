package dev.novux.dynasty.cbb.core.chemistry

import dev.novux.dynasty.cbb.core.balance.BalanceChemistry

/** Clutch win-probability delta — [planning/in-season-systems.md#chemistry]. */
object ChemistryClutchModifier {
    fun winProbabilityDelta(teamChemistry: Int, tuning: BalanceChemistry): Double =
        when {
            teamChemistry >= 80 -> tuning.clutchBonusAbove80
            teamChemistry < 50 -> -tuning.clutchPenaltyBelow50
            else -> 0.0
        }
}

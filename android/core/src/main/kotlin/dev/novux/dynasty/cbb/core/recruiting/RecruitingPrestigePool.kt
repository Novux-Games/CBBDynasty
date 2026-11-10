package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingPrestigeTier
import kotlin.math.roundToInt

/**
 * Weekly pool from program prestige ([planning/recruiting-engine.md#prestige-tier-weekly-pool-design-lock]).
 */
object RecruitingPrestigePool {
    fun tierForPrestige(economy: BalanceRecruitingEconomy, programPrestige: Int): BalanceRecruitingPrestigeTier {
        val prestige = programPrestige.coerceIn(0, 99)
        return economy.prestigeTiers.first { prestige in it.prestigeMin..it.prestigeMax }
    }

    fun weeklyPoolPoints(economy: BalanceRecruitingEconomy, programPrestige: Int): Int {
        val tier = tierForPrestige(economy, programPrestige)
        return linearMapPrestigeToPool(tier, programPrestige)
    }

    private fun linearMapPrestigeToPool(tier: BalanceRecruitingPrestigeTier, prestige: Int): Int {
        val clamped = prestige.coerceIn(tier.prestigeMin, tier.prestigeMax)
        val span = (tier.prestigeMax - tier.prestigeMin).coerceAtLeast(1)
        val ratio = (clamped - tier.prestigeMin).toDouble() / span
        val poolSpan = tier.weeklyPointsMax - tier.weeklyPointsMin
        return (tier.weeklyPointsMin + ratio * poolSpan).roundToInt()
    }
}

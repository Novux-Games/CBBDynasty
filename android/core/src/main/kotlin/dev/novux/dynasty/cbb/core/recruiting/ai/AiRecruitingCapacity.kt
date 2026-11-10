package dev.novux.dynasty.cbb.core.recruiting.ai

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingPrestigeTier
import dev.novux.dynasty.cbb.core.recruiting.RecruitingPrestigePool

/** Prestige-tier realistic board size — [planning/recruiting-engine.md#ai-recruiting]. */
object AiRecruitingCapacity {
    fun coreTargetCap(economy: BalanceRecruitingEconomy, programPrestige: Int): Int {
        val tier = RecruitingPrestigePool.tierForPrestige(economy, programPrestige)
        return parseCapacityUpperBound(tier)
    }

    private fun parseCapacityUpperBound(tier: BalanceRecruitingPrestigeTier): Int {
        val digits = Regex("""(\d+)""").findAll(tier.realisticCapacity).map { it.value.toInt() }.toList()
        if (tier.realisticCapacity.contains("full national", ignoreCase = true)) return 15
        if (digits.isEmpty()) return 4
        return digits.max()
    }
}

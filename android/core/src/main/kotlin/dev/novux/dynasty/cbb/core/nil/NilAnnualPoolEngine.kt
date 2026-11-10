package dev.novux.dynasty.cbb.core.nil

import dev.novux.dynasty.cbb.core.balance.BalanceNilAnnualPoolTables
import dev.novux.dynasty.cbb.core.balance.BalanceNilConferenceTier
import kotlin.math.roundToLong

/**
 * Conference tier × program prestige → frozen annual collective pool
 * ([planning/game-design.md#annual-collective-pool-design-lock]).
 */
object NilAnnualPoolEngine {
    const val PRESTIGE_SCALE_MAX: Int = 99

    fun resolve(
        conferenceId: String,
        programPrestige: Int,
        tables: BalanceNilAnnualPoolTables,
    ): Long {
        val prestige = programPrestige.coerceIn(0, PRESTIGE_SCALE_MAX)
        val tier = tables.tiers.firstOrNull { conferenceId in it.conferenceIds }
            ?: tables.tiers.lastOrNull { it.tierId == "low" }
            ?: tables.tiers.first()
        if (prestige >= tables.blueBlood.prestigeMin && tier.blueBloodEligible) {
            return lerpBlueBlood(prestige, tables)
        }
        if (prestige >= tables.blueBlood.prestigeMin) {
            return tier.annualPoolMaxUsd
        }
        return lerpTier(prestige, tier)
    }

    private fun lerpTier(prestige: Int, tier: BalanceNilConferenceTier): Long {
        val t = prestige.toDouble() / PRESTIGE_SCALE_MAX.toDouble()
        val span = tier.annualPoolMaxUsd - tier.annualPoolMinUsd
        return tier.annualPoolMinUsd + (span * t).roundToLong()
    }

    private fun lerpBlueBlood(prestige: Int, tables: BalanceNilAnnualPoolTables): Long {
        val band = tables.blueBlood
        val denom = (band.prestigeMax - band.prestigeMin).coerceAtLeast(1)
        val t = (prestige - band.prestigeMin).coerceAtLeast(0).toDouble() / denom.toDouble()
        val span = band.annualPoolMaxUsd - band.annualPoolMinUsd
        return band.annualPoolMinUsd + (span * t).roundToLong()
    }
}

package dev.novux.dynasty.cbb.core.recruiting

/**
 * Proposed defaults until `recruiting.interest.*` lands in balance JSON ([planning/recruiting-engine.md]).
 */
object RecruitInterestConfig {
    const val PRIORITY_MULTIPLIER: Double = 2.0

    val baseWeights: Map<RecruitingInterestFactor, Double> = mapOf(
        RecruitingInterestFactor.Nil to 0.25,
        RecruitingInterestFactor.PlayingTimeFit to 0.20,
        RecruitingInterestFactor.PrestigeWinning to 0.20,
        RecruitingInterestFactor.CoachRelationship to 0.15,
        RecruitingInterestFactor.Proximity to 0.10,
        RecruitingInterestFactor.CampusLife to 0.10,
    )
}

object RecruitInterestWeights {
    fun effectiveWeights(
        priorities: List<RecruitPriority>,
        stars: Int,
    ): Map<RecruitingInterestFactor, Double> {
        val weights = RecruitInterestConfig.baseWeights.toMutableMap()
        for (priority in priorities) {
            val current = weights.getValue(priority.factor)
            weights[priority.factor] = current * RecruitInterestConfig.PRIORITY_MULTIPLIER
        }
        applyStarTierShift(weights, stars)
        return renormalize(weights)
    }

    private fun applyStarTierShift(weights: MutableMap<RecruitingInterestFactor, Double>, stars: Int) {
        when (stars) {
            5 -> {
                bump(weights, RecruitingInterestFactor.Nil, 0.08)
                bump(weights, RecruitingInterestFactor.PrestigeWinning, 0.04)
                bump(weights, RecruitingInterestFactor.PlayingTimeFit, -0.06)
                bump(weights, RecruitingInterestFactor.Proximity, -0.06)
            }
            4 -> bump(weights, RecruitingInterestFactor.Nil, 0.04)
            2 -> {
                bump(weights, RecruitingInterestFactor.PlayingTimeFit, 0.06)
                bump(weights, RecruitingInterestFactor.Proximity, 0.04)
                bump(weights, RecruitingInterestFactor.Nil, -0.05)
                bump(weights, RecruitingInterestFactor.PrestigeWinning, -0.05)
            }
            else -> Unit
        }
        renormalizeInPlace(weights)
    }

    private fun bump(weights: MutableMap<RecruitingInterestFactor, Double>, factor: RecruitingInterestFactor, delta: Double) {
        weights[factor] = (weights[factor] ?: 0.0) + delta
    }

    private fun renormalize(weights: Map<RecruitingInterestFactor, Double>): Map<RecruitingInterestFactor, Double> {
        val copy = weights.toMutableMap()
        renormalizeInPlace(copy)
        return copy
    }

    private fun renormalizeInPlace(weights: MutableMap<RecruitingInterestFactor, Double>) {
        val sum = weights.values.sum().coerceAtLeast(0.0001)
        for (key in weights.keys.toList()) {
            weights[key] = (weights[key]!! / sum).coerceAtLeast(0.0)
        }
    }
}

package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceNilRules
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import dev.novux.dynasty.cbb.core.nil.NilInterestScoring
import kotlin.math.roundToInt

data class RecruitInterestSnapshot(
    val factorScores: Map<RecruitingInterestFactor, Double>,
    val effectiveWeights: Map<RecruitingInterestFactor, Double>,
    val baselineInterest: Int,
    /** Live meter — baseline at zero spend; spend adds via [liveInterest]. */
    val liveInterest: Int,
)

object RecruitInterestEngine {
    fun baselineSnapshot(
        prospect: RecruitProspect,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
        spendDelta: Int = 0,
        promiseDelta: Int = 0,
        nilOfferUsd: Int? = null,
        nilRules: BalanceNilRules? = null,
    ): RecruitInterestSnapshot {
        val scores = factorScores(prospect, school, recommended, nilOfferUsd, nilRules)
        val weights = RecruitInterestWeights.effectiveWeights(prospect.topPriorities, prospect.stars)
        val weighted = weights.entries.sumOf { (factor, weight) -> weight * scores.getValue(factor) }
        val baseline = (weighted * 100.0).roundToInt().coerceIn(0, 100)
        val live = (baseline + spendDelta + promiseDelta).coerceIn(0, 100)
        return RecruitInterestSnapshot(
            factorScores = scores,
            effectiveWeights = weights,
            baselineInterest = baseline,
            liveInterest = live,
        )
    }

    private fun factorScores(
        prospect: RecruitProspect,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
        nilOfferUsd: Int?,
        nilRules: BalanceNilRules?,
    ): Map<RecruitingInterestFactor, Double> = mapOf(
        RecruitingInterestFactor.Nil to nilFactor(
            prospect.nilAskUsd,
            nilOfferUsd,
            school.nilPoolAnnualUsd,
            nilRules,
        ),
        RecruitingInterestFactor.PlayingTimeFit to
            if (prospect.position in school.positionsNeed) 0.85 else 0.48,
        RecruitingInterestFactor.PrestigeWinning to prestigeWinning(prospect.stars, school.programPrestige),
        RecruitingInterestFactor.CoachRelationship to 0.5,
        RecruitingInterestFactor.Proximity to proximity(prospect.homeState, school, recommended),
        RecruitingInterestFactor.CampusLife to 0.55,
    )

    private fun prestigeWinning(stars: Int, programPrestige: Int): Double {
        val floor = when (stars) {
            5 -> 75
            4 -> 52
            3 -> 30
            else -> 12
        }
        val gap = (floor - programPrestige).coerceAtLeast(0)
        return (1.0 - gap / 40.0).coerceIn(0.1, 1.0)
    }

    private fun proximity(
        homeState: String,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): Double = when {
        homeState.equals(school.schoolHomeState, ignoreCase = true) -> recommended.inStateProximityScore
        homeState.uppercase() in school.pipelineStates.map { it.uppercase() } -> recommended.pipelineProximityScore
        else -> recommended.outOfRegionProximityScore
    }

    private fun nilFactor(
        nilAskUsd: Int,
        nilOfferUsd: Int?,
        poolUsd: Int,
        nilRules: BalanceNilRules?,
    ): Double {
        if (nilOfferUsd != null && nilOfferUsd > 0 && nilRules != null) {
            return NilInterestScoring.factor(nilOfferUsd, nilAskUsd, nilRules.interestNilRatioExponent)
        }
        return nilCapacity(nilAskUsd, poolUsd)
    }

    private fun nilCapacity(nilAskUsd: Int, poolUsd: Int): Double {
        val divisor = 4.0
        return (poolUsd.toDouble() / (nilAskUsd.coerceAtLeast(1) * divisor)).coerceIn(0.0, 1.0)
    }
}

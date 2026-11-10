package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import kotlin.math.roundToInt

/** Zero-spend interest for Recommended — [planning/recruiting-engine.md#feed-into-signlikelihood]. */
object BaselineInterestCalculator {
    fun compute(
        prospect: RecruitProspect,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): Int {
        val prestigeWinning = prestigeWinningScore(prospect.stars, school.programPrestige)
        val proximity = proximityScore(prospect.homeState, school, recommended)
        val playingTimeFit = if (prospect.position in school.positionsNeed) 0.85 else 0.48
        val nilCapacity = nilBaselineCapacity(prospect.nilAskUsd, school.nilPoolAnnualUsd)
        val coachRelationship = 0.5
        val campusLife = 0.55
        val factors = listOf(prestigeWinning, proximity, playingTimeFit, nilCapacity, coachRelationship, campusLife)
        val weighted = factors.average()
        return (weighted * 100.0).roundToInt().coerceIn(0, 100)
    }

    private fun prestigeWinningScore(stars: Int, programPrestige: Int): Double {
        val floor = when (stars) {
            5 -> 75
            4 -> 52
            3 -> 30
            else -> 12
        }
        val gap = (floor - programPrestige).coerceAtLeast(0)
        return (1.0 - gap / 40.0).coerceIn(0.1, 1.0)
    }

    private fun proximityScore(
        homeState: String,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): Double = when {
        homeState.equals(school.schoolHomeState, ignoreCase = true) -> recommended.inStateProximityScore
        homeState.uppercase() in school.pipelineStates.map { it.uppercase() } -> recommended.pipelineProximityScore
        else -> recommended.outOfRegionProximityScore
    }

    private fun nilBaselineCapacity(nilAskUsd: Int, poolUsd: Int): Double {
        val divisor = 4.0
        return (poolUsd.toDouble() / (nilAskUsd.coerceAtLeast(1) * divisor)).coerceIn(0.0, 1.0)
    }
}

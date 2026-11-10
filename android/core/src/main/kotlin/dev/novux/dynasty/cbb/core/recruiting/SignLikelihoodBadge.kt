package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import dev.novux.dynasty.cbb.core.balance.BalanceSignLikelihoodBands

enum class SignLikelihoodBadge {
    Likely,
    Possible,
    Reach,
    LongShot,
}

data class RecommendedProspectRow(
    val prospect: RecruitProspect,
    val baselineInterest: Int,
    val adjustedScore: Double,
    val badge: SignLikelihoodBadge,
    val inState: Boolean,
    val positionNeed: Boolean,
)

object SignLikelihoodEngine {
    fun evaluate(
        prospect: RecruitProspect,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): RecommendedProspectRow {
        val baseline = BaselineInterestCalculator.compute(prospect, school, recommended)
        var adjusted = baseline.toDouble()
        adjusted -= prestigeGapPenalty(prospect.stars, school.programPrestige, recommended)
        if (prospect.position in school.positionsNeed) {
            adjusted += recommended.rosterNeedBonus
        }
        val inState = prospect.homeState.equals(school.schoolHomeState, ignoreCase = true)
        val positionNeed = prospect.position in school.positionsNeed
        val badge = badgeForScore(adjusted, recommended.bands)
        return RecommendedProspectRow(
            prospect = prospect,
            baselineInterest = baseline,
            adjustedScore = adjusted,
            badge = badge,
            inState = inState,
            positionNeed = positionNeed,
        )
    }

    fun hiddenOnRecommendedTab(
        row: RecommendedProspectRow,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): Boolean {
        val rank = row.prospect.nationalHsRank
        if (rank != null && rank <= recommended.eliteNationalRankHideThrough) {
            val pipelineSave = row.inState ||
                row.prospect.homeState.uppercase() in school.pipelineStates.map { it.uppercase() }
            if (row.adjustedScore < recommended.eliteShowMinAdjustedScore && !pipelineSave) {
                return true
            }
        }
        if (school.programPrestige < 40 && row.prospect.stars >= 5 && row.badge == SignLikelihoodBadge.LongShot) {
            return true
        }
        return false
    }

    private fun prestigeGapPenalty(
        stars: Int,
        programPrestige: Int,
        recommended: BalanceRecruitingRecommended,
    ): Double {
        val floor = recommended.prestigeFloorByStars[stars.toString()] ?: 0
        val gap = (floor - programPrestige).coerceAtLeast(0)
        return gap * recommended.prestigeGapPenaltyPerPoint
    }

    private fun badgeForScore(score: Double, bands: BalanceSignLikelihoodBands): SignLikelihoodBadge =
        when {
            score >= bands.likelyMin -> SignLikelihoodBadge.Likely
            score >= bands.possibleMin -> SignLikelihoodBadge.Possible
            score >= bands.reachMin -> SignLikelihoodBadge.Reach
            else -> SignLikelihoodBadge.LongShot
        }
}

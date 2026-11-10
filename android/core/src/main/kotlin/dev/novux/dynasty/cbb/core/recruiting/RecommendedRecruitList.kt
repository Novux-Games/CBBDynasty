package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceNilRules
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended

/** Recommended tab ordering — [planning/recruiting-engine.md#recommended-tab-default]. */
object RecommendedRecruitList {
    fun build(
        pool: List<RecruitProspect>,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
        nilOffersByProspect: Map<String, Int> = emptyMap(),
        nilRules: BalanceNilRules? = null,
    ): List<RecommendedProspectRow> {
        return pool
            .map { prospect ->
                SignLikelihoodEngine.evaluate(
                    prospect,
                    school,
                    recommended,
                    nilOfferUsd = nilOffersByProspect[prospect.prospectId],
                    nilRules = nilRules,
                )
            }
            .filterNot { SignLikelihoodEngine.hiddenOnRecommendedTab(it, school, recommended) }
            .sortedWith(
                compareByDescending<RecommendedProspectRow> { it.adjustedScore }
                    .thenByDescending { it.positionNeed }
                    .thenByDescending { it.inState }
                    .thenByDescending { it.prospect.stars },
            )
            .take(recommended.listCap)
    }
}

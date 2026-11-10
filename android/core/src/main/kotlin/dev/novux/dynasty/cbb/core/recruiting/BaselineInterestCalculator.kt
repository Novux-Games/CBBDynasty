package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended

/** Zero-spend interest for Recommended — [planning/recruiting-engine.md#feed-into-signlikelihood]. */
object BaselineInterestCalculator {
    fun compute(
        prospect: RecruitProspect,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
    ): Int = RecruitInterestEngine.baselineSnapshot(prospect, school, recommended).baselineInterest
}

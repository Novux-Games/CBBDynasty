package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow
import dev.novux.dynasty.cbb.core.recruiting.RecommendedRecruitList
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.SchoolRecruitingContext

object InSeasonRecruitingNudgeResolver {
    private const val WATCHLIST_INTEREST_THRESHOLD = 55

    fun watchlistNudge(
        pool: List<RecruitProspect>,
        school: SchoolRecruitingContext,
        recommended: BalanceRecruitingRecommended,
        spendDeltaByProspect: Map<String, Int>,
        top60: List<NationalRankRow> = emptyList(),
    ): RecruitingWatchlistNudge? {
        if (pool.isEmpty() || spendDeltaByProspect.isEmpty()) return null
        val rankById = top60.associate { it.prospect.prospectId to it.rank }
        val rows = RecommendedRecruitList.build(
            pool,
            school,
            recommended,
            spendDeltaByProspect = spendDeltaByProspect,
        )
        val candidate = rows.firstOrNull { row ->
            val spent = spendDeltaByProspect[row.prospect.prospectId] ?: 0
            spent > 0 && row.liveInterest >= WATCHLIST_INTEREST_THRESHOLD
        } ?: rows.firstOrNull { row ->
            (spendDeltaByProspect[row.prospect.prospectId] ?: 0) > 0
        } ?: return null
        val spent = spendDeltaByProspect[candidate.prospect.prospectId] ?: 0
        if (spent <= 0) return null
        return RecruitingWatchlistNudge(
            prospectName = candidate.prospect.displayName,
            nationalRank = rankById[candidate.prospect.prospectId],
            reEngage = candidate.liveInterest < WATCHLIST_INTEREST_THRESHOLD,
        )
    }
}

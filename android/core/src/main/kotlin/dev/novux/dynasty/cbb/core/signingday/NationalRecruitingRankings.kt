package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.RecruitingClassSeed

data class NationalRankRow(
    val rank: Int,
    val prospect: RecruitProspect,
)

object NationalRecruitingRankings {
    fun top60FromPool(pool: List<RecruitProspect>): List<NationalRankRow> =
        pool
            .filter { it.nationalHsRank != null }
            .sortedBy { it.nationalHsRank!! }
            .map { NationalRankRow(it.nationalHsRank!!, it) }

    /** Demo-sized pool for signing-day previews until a dynasty seed is loaded. */
    fun top60(membershipSeasonIndex: Int): List<NationalRankRow> =
        top60FromPool(RecruitingClassSeed.demoPool(membershipSeasonIndex))
}

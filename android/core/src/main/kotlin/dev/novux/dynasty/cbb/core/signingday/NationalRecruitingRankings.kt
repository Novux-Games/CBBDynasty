package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.RecruitingClassSeed

data class NationalRankRow(
    val rank: Int,
    val prospect: RecruitProspect,
)

object NationalRecruitingRankings {
    fun top60(membershipSeasonIndex: Int): List<NationalRankRow> =
        RecruitingClassSeed.demoPool(membershipSeasonIndex)
            .filter { it.nationalHsRank != null }
            .sortedBy { it.nationalHsRank!! }
            .map { NationalRankRow(it.nationalHsRank!!, it) }
}

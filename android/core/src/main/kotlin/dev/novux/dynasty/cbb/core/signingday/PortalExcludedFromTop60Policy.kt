package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.recruiting.RecruitPoolKind
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect

/**
 * Portal entrants never appear in national HS Top 60 ([MEGA-RECR-009]).
 */
object PortalExcludedFromTop60Policy {
    const val MEGA_ID: String = "MEGA-RECR-009"

    fun top60Eligible(pool: List<RecruitProspect>): List<RecruitProspect> =
        pool.filter { it.poolKind != RecruitPoolKind.Portal }

    fun top60FromEligiblePool(pool: List<RecruitProspect>) =
        NationalRecruitingRankings.top60FromPool(top60Eligible(pool))
}
package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.academics.AcademicsRecruitingBudgetEngine

/** Traceability anchors for recruiting-queue batch 35. */
object AcademicsRecruitingPoolDriftPolicy {
    const val MEGA_ID = "MEGA-RECR-016"
    val ENGINE: String = AcademicsRecruitingBudgetEngine::class.java.simpleName
}

object RecruitingCyclePoolLockPolicy {
    const val MEGA_ID = "MEGA-RECR-019"
}

object PreseasonTeamRankingsScopePolicy {
    const val MEGA_ID = "MEGA-RECR-055"
}

object RecruitingBuildChecklistEconomyPolicy {
    const val MEGA_ID = "MEGA-RECR-079"
}

object RecruitingInSeasonTriggerChecklistPolicy {
    const val MEGA_ID = "MEGA-RECR-080"
}

object SigningDayRecruitRankingsCopyPolicy {
    const val MEGA_ID = "MEGA-RECR-119"
}

object PreseasonPollReviewCopyPolicy {
    const val MEGA_ID = "MEGA-RECR-120"
}

object IncomingClassSeedPerformancePolicy {
    const val MEGA_ID = "MEGA-RECR-132"
    const val PANEL_MEGA_ID = IncomingClassSeedIoPolicy.PANEL_QA_ID
}

object RecruitingPoolEncodeSizePolicy {
    const val MEGA_ID = "MEGA-RECR-134"
}

object ReservationInterestLagPolicy {
    const val MEGA_ID = "MEGA-RECR-142"
}

object RecommendedListScalePolicy {
    const val MEGA_ID = "MEGA-RECR-162"
}

object RecommendedListCapPolicy {
    const val MEGA_ID = "MEGA-RECR-163"
}
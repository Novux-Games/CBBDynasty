package dev.novux.dynasty.cbb.core.dashboard

enum class DashboardTaskKind {
    Required,
    Optional,
}

enum class DashboardDeepLink {
    RecruitingRecommended,
    RecruitingPortal,
    RecruitingHsJuco,
}

enum class DashboardTaskId {
    SpendRecruitingPoints,
    PortalWindow,
    PortalTop40Published,
    SigningCrunch,
    OpenScholarships,
}

data class DashboardTask(
    val id: DashboardTaskId,
    val kind: DashboardTaskKind,
    val deepLink: DashboardDeepLink,
    /** Optional format args for UI copy (e.g. points count, week index). */
    val formatArgs: List<Int> = emptyList(),
)

object DashboardAdvanceGate {
    fun blocksWeekAdvance(tasks: List<DashboardTask>): Boolean =
        tasks.any { it.kind == DashboardTaskKind.Required }
}

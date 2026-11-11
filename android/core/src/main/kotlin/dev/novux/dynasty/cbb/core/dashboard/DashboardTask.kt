package dev.novux.dynasty.cbb.core.dashboard

enum class DashboardTaskKind {
    Required,
    Optional,
}

enum class DashboardDeepLink {
    RecruitingRecommended,
    RecruitingPortal,
    RecruitingHsJuco,
    SigningDayRankings,
    SigningDayPositions,
    Strategy,
    DisciplineSheet,
    PreseasonChecklist,
    PlayWeekGame,
    CoachCard,
}

enum class DashboardTaskId {
    SpendRecruitingPoints,
    PortalWindow,
    PortalTop40Published,
    SigningCrunch,
    OpenScholarships,
    SigningDayViewRankings,
    SigningDayEditPositions,
    SigningDayHardSign,
    ReviewInjury,
    MedicalRedshirtCandidate,
    TeamDisciplinePending,
    PreseasonRequired,
    PlayScheduledGame,
    CoachSundaySkillPoints,
    RecruitingWatchlistNudge,
    PromiseAtRisk,
}

data class DashboardTask(
    val id: DashboardTaskId,
    val kind: DashboardTaskKind,
    val deepLink: DashboardDeepLink,
    /** Optional format args for UI copy (e.g. points count, week index). */
    val formatArgs: List<Int> = emptyList(),
    val stringFormatArgs: List<String> = emptyList(),
)

object DashboardAdvanceGate {
    fun blocksWeekAdvance(tasks: List<DashboardTask>): Boolean =
        tasks.any { it.kind == DashboardTaskKind.Required }
}

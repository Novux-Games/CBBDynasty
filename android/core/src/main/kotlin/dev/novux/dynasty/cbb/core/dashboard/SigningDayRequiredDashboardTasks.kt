package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.signingday.SigningDayProgress

object SigningDayRequiredDashboardTasks {
    fun build(progress: SigningDayProgress): List<DashboardTask> {
        if (progress.hardSignComplete) return emptyList()
        val tasks = mutableListOf<DashboardTask>()
        if (!progress.rankingsViewed) {
            tasks.add(
                DashboardTask(
                    id = DashboardTaskId.SigningDayViewRankings,
                    kind = DashboardTaskKind.Required,
                    deepLink = DashboardDeepLink.SigningDayRankings,
                ),
            )
        }
        if (!progress.positionsConfirmed) {
            tasks.add(
                DashboardTask(
                    id = DashboardTaskId.SigningDayEditPositions,
                    kind = DashboardTaskKind.Required,
                    deepLink = DashboardDeepLink.SigningDayPositions,
                ),
            )
        }
        if (progress.requiredTasksComplete() && !progress.hardSignComplete) {
            tasks.add(
                DashboardTask(
                    id = DashboardTaskId.SigningDayHardSign,
                    kind = DashboardTaskKind.Required,
                    deepLink = DashboardDeepLink.SigningDayPositions,
                ),
            )
        }
        return tasks
    }
}

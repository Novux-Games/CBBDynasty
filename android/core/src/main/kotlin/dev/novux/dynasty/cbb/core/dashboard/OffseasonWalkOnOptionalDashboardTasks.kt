package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.recruiting.WalkOnAfterSigningClosePolicy

object OffseasonWalkOnOptionalDashboardTasks {
    fun build(recruitingCycleClosed: Boolean, offseasonWeekIndex: Int): List<DashboardTask> {
        if (!WalkOnAfterSigningClosePolicy.surfaceWalkOnNudge(recruitingCycleClosed, offseasonWeekIndex)) {
            return emptyList()
        }
        return listOf(
            DashboardTask(
                id = DashboardTaskId.WalkOnTryouts,
                kind = DashboardTaskKind.Optional,
                deepLink = DashboardDeepLink.OffseasonRosterCuts,
            ),
        )
    }
}
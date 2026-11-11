package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.recruiting.RecruitingCyclePolicy

/** Year-long recruiting optional nudges on the in-season shell dashboard. */
object InSeasonRecruitingOptionalDashboardTasks {
    fun build(context: InSeasonRecruitingDashboardContext?): List<DashboardTask> {
        if (context == null) return emptyList()
        if (!RecruitingCyclePolicy.hubNavigable(
                context.phase,
                context.offseasonWeekIndex,
                context.cycleClosed,
            )
        ) {
            return emptyList()
        }
        return buildList {
            if (context.pointsAvailable > 0) {
                add(
                    DashboardTask(
                        id = DashboardTaskId.SpendRecruitingPoints,
                        kind = DashboardTaskKind.Optional,
                        deepLink = DashboardDeepLink.RecruitingRecommended,
                        formatArgs = listOf(context.pointsAvailable),
                    ),
                )
            }
            context.watchlistNudge?.let { nudge ->
                add(
                    DashboardTask(
                        id = DashboardTaskId.RecruitingWatchlistNudge,
                        kind = DashboardTaskKind.Optional,
                        deepLink = DashboardDeepLink.RecruitingRecommended,
                        formatArgs = listOf(
                            nudge.nationalRank ?: 0,
                            if (nudge.reEngage) 1 else 0,
                        ),
                        stringFormatArgs = listOf(nudge.prospectName),
                    ),
                )
            }
        }
    }
}

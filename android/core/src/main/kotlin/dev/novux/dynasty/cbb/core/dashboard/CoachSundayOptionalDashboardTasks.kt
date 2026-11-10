package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.coach.skill.CoachSundaySkillPointPolicy

object CoachSundayOptionalDashboardTasks {
    fun build(unspentBank: Int): List<DashboardTask> {
        if (!CoachSundaySkillPointPolicy.showDashboardNudge(unspentBank)) return emptyList()
        return listOf(
            DashboardTask(
                id = DashboardTaskId.CoachSundaySkillPoints,
                kind = DashboardTaskKind.Optional,
                deepLink = DashboardDeepLink.CoachCard,
                formatArgs = listOf(unspentBank),
            ),
        )
    }
}

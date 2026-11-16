package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.coach.skill.CoachSundaySkillPointPolicy
import dev.novux.dynasty.cbb.core.coach.skill.SundayInSeasonCoachSkillGrantPolicy

object CoachSundayOptionalDashboardTasks {
    fun build(unspentBank: Int): List<DashboardTask> {
        if (!SundayInSeasonCoachSkillGrantPolicy.notifyUnspentBank(unspentBank)) return emptyList()
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

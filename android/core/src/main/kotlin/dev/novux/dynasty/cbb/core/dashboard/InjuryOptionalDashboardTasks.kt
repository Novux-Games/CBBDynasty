package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.balance.BalanceMedicalRedshirtRules
import dev.novux.dynasty.cbb.core.injuries.MedicalRedshirtPolicy
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** In-season optional injury tasks — [planning/dashboard-tasks.md]. */
object InjuryOptionalDashboardTasks {
    fun build(
        players: List<RosterPlayer>,
        rsWeekIndex: Int,
        rules: BalanceMedicalRedshirtRules = BalanceMedicalRedshirtRules(),
    ): List<DashboardTask> {
        val tasks = mutableListOf<DashboardTask>()
        players.filter { it.injuryActive && it.injuryWeeksRemaining > 0 }.forEach { player ->
            val name = player.displayLabel()
            tasks += DashboardTask(
                id = DashboardTaskId.ReviewInjury,
                kind = DashboardTaskKind.Optional,
                deepLink = DashboardDeepLink.Strategy,
                formatArgs = listOf(player.injuryWeeksRemaining),
                stringFormatArgs = listOf(name),
            )
            if (MedicalRedshirtPolicy.evaluate(player, rsWeekIndex, rules).eligible) {
                tasks += DashboardTask(
                    id = DashboardTaskId.MedicalRedshirtCandidate,
                    kind = DashboardTaskKind.Optional,
                    deepLink = DashboardDeepLink.Strategy,
                    stringFormatArgs = listOf(name),
                )
            }
        }
        return tasks
    }
}

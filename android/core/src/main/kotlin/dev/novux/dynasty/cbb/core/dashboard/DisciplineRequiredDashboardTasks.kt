package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.discipline.PendingDisciplineIncident

object DisciplineRequiredDashboardTasks {
    fun build(pending: PendingDisciplineIncident?): List<DashboardTask> {
        val incident = pending ?: return emptyList()
        return listOf(
            DashboardTask(
                id = DashboardTaskId.TeamDisciplinePending,
                kind = DashboardTaskKind.Required,
                deepLink = DashboardDeepLink.DisciplineSheet,
                stringFormatArgs = listOf(incident.headline),
            ),
        )
    }
}

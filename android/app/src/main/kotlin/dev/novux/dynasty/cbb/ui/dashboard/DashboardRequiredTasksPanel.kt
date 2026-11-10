package dev.novux.dynasty.cbb.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import dev.novux.dynasty.cbb.core.dashboard.DashboardTask
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId

@Composable
fun DashboardRequiredTasksPanel(
    tasks: List<DashboardTask>,
    onOpenSigningDay: (DashboardDeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tasks.isEmpty()) return
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.dashboard_required_heading),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        tasks.forEach { task ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = requiredTaskMessage(task), style = MaterialTheme.typography.bodyLarge)
                    OutlinedButton(
                        onClick = { onOpenSigningDay(task.deepLink) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.dashboard_task_open))
                    }
                }
            }
        }
    }
}

@Composable
private fun requiredTaskMessage(task: DashboardTask): String = when (task.id) {
    DashboardTaskId.SigningDayViewRankings -> stringResource(R.string.dashboard_task_signing_rankings)
    DashboardTaskId.SigningDayEditPositions -> stringResource(R.string.dashboard_task_signing_positions)
    DashboardTaskId.SigningDayHardSign -> stringResource(R.string.dashboard_task_signing_hard_sign)
    else -> task.id.name
}

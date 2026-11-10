package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import kotlinx.coroutines.launch

@Composable
fun RegularSeasonSchedulePanel(
    dynastyId: String,
    seasonIndex: Int,
    scheduleRepository: PreseasonScheduleRepository,
    progressRepository: PreseasonProgressRepository,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var plan by remember { mutableStateOf<PreseasonSchedulePlan?>(null) }
    var solving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId, seasonIndex) {
        plan = scheduleRepository.loadPlan(dynastyId, seasonIndex)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.preseason_rs_schedule_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        plan?.let { current ->
            Text(
                text = stringResource(
                    R.string.preseason_rs_schedule_summary,
                    current.totalRsGames,
                    current.conferenceGameCount,
                    current.nonConferenceGameCount,
                    current.earlyEventGameCount,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                current.regularSeason
                    .filter { it.kind == ScheduleGameKind.Conference }
                    .take(8)
                    .forEach { game ->
                        Text(
                            stringResource(
                                R.string.preseason_rs_line_conf,
                                game.opponentDisplayName,
                                if (game.homeGame) "H" else "A",
                            ),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                if (current.regularSeason.count { it.kind == ScheduleGameKind.Conference } > 8) {
                    Text(stringResource(R.string.preseason_rs_more_conf), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    solving = true
                    plan = scheduleRepository.solveAndSaveRegularSeason(dynastyId, seasonIndex)
                    if (plan != null) {
                        progressRepository.completeTask(
                            dynastyId,
                            seasonIndex,
                            PreseasonTaskId.ScheduleRegularSeason,
                        )
                        onSaved()
                    }
                    solving = false
                }
            },
            enabled = !solving,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (solving) {
                    stringResource(R.string.preseason_rs_solving)
                } else if (plan != null) {
                    stringResource(R.string.preseason_rs_locked)
                } else {
                    stringResource(R.string.preseason_rs_run_solver)
                },
            )
        }
    }
}

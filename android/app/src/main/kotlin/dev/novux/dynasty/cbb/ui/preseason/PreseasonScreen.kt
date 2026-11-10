package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.preseason.PreseasonStartGate
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskProgress
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import kotlinx.coroutines.launch

@Composable
fun PreseasonScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    progressRepository: PreseasonProgressRepository,
    onExitHome: () -> Unit,
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    var progress by remember { mutableStateOf<PreseasonTaskProgress?>(null) }
    val scope = rememberCoroutineScope()
    val position = loaded.weekCheckpoint.activePosition

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex) {
        progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
    }

    val taskProgress = progress ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.preseason_shell_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = loaded.summary.schoolDisplayName.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = SeasonMembershipYear.label(loaded.metadata.seasonIndex),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = CalendarWeekLabel.display(position),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.preseason_checklist_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(PreseasonTaskId.entries) { taskId ->
                val done = taskProgress.isComplete(taskId)
                val enabled = taskProgress.canComplete(taskId) && !done
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (done) {
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        CardDefaults.cardColors()
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preseasonTaskTitle(taskId),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            if (!taskProgress.schedulingUnlocked() &&
                                (taskId == PreseasonTaskId.ScheduleExhibitions ||
                                    taskId == PreseasonTaskId.ScheduleRegularSeason)
                            ) {
                                Text(
                                    text = stringResource(R.string.preseason_task_locked_invites),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                        if (done) {
                            Text(
                                text = stringResource(R.string.preseason_task_done),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        progress = progressRepository.completeTask(
                                            loaded.dynastyId,
                                            loaded.metadata.seasonIndex,
                                            taskId,
                                        )
                                    }
                                },
                                enabled = enabled,
                            ) {
                                Text(stringResource(R.string.preseason_task_complete))
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                        is DynastyLoadResult.Success -> {
                            loaded = result.loaded
                            ActiveDynastySession.set(result.loaded)
                        }
                        else -> Unit
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = PreseasonStartGate.canStartSeason(taskProgress),
        ) {
            Text(stringResource(R.string.preseason_start_season))
        }
        OutlinedButton(
            onClick = onExitHome,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

@Composable
private fun preseasonTaskTitle(taskId: PreseasonTaskId): String = when (taskId) {
    PreseasonTaskId.IncomingClassSeed -> stringResource(R.string.preseason_task_incoming_class)
    PreseasonTaskId.PreseasonWorkouts -> stringResource(R.string.preseason_task_workouts)
    PreseasonTaskId.CoachUpgrades -> stringResource(R.string.preseason_task_coach_upgrades)
    PreseasonTaskId.RankingsReview -> stringResource(R.string.preseason_task_rankings)
    PreseasonTaskId.PreseasonReport -> stringResource(R.string.preseason_task_report)
    PreseasonTaskId.TournamentInvites -> stringResource(R.string.preseason_task_tournament_invites)
    PreseasonTaskId.ScheduleExhibitions -> stringResource(R.string.preseason_task_exhibitions)
    PreseasonTaskId.ScheduleRegularSeason -> stringResource(R.string.preseason_task_regular_schedule)
}

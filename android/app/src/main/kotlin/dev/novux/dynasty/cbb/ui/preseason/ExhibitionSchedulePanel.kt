package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.schedule.PreseasonScheduleConstants
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import kotlinx.coroutines.launch

@Composable
fun ExhibitionSchedulePanel(
    dynastyId: String,
    seasonIndex: Int,
    scheduleRepository: PreseasonScheduleRepository,
    progressRepository: PreseasonProgressRepository,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var count by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId, seasonIndex) {
        count = scheduleRepository.loadExhibitionCount(dynastyId, seasonIndex)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.preseason_exhibitions_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(R.string.preseason_exhibitions_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (0..PreseasonScheduleConstants.MAX_EXHIBITION_GAMES).forEach { option ->
                FilterChip(
                    selected = count == option,
                    onClick = { count = option },
                    label = { Text(option.toString()) },
                )
            }
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    scheduleRepository.saveExhibitions(dynastyId, seasonIndex, count)
                    progressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.ScheduleExhibitions)
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (count == 0) {
                    stringResource(R.string.preseason_exhibitions_skip)
                } else {
                    stringResource(R.string.preseason_exhibitions_confirm, count)
                },
            )
        }
    }
}

package dev.novux.dynasty.cbb.ui.offseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.offseason.weekseven.SummerWorkoutReport
import dev.novux.dynasty.cbb.data.offseason.OffseasonSummerWorkoutRepository

@Composable
fun SummerWorkoutReportPanel(
    dynastyId: String,
    seasonIndex: Int,
    summerWorkoutRepository: OffseasonSummerWorkoutRepository,
    modifier: Modifier = Modifier,
) {
    var report by remember { mutableStateOf<SummerWorkoutReport?>(null) }

    LaunchedEffect(dynastyId, seasonIndex) {
        report = summerWorkoutRepository.ensureSimulated(dynastyId, seasonIndex)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.summer_workouts_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(R.string.summer_workouts_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        report?.let { data ->
            Text(
                text = stringResource(
                    R.string.summer_workouts_summary,
                    data.coachTrainingGrade,
                    data.teamAverageGain,
                    data.lines.size,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(data.lines, key = { it.playerId }) { line ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(line.displayLabel, fontWeight = FontWeight.Medium)
                            Text(
                                stringResource(
                                    R.string.summer_workouts_line,
                                    line.gainPoints,
                                    line.summary,
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

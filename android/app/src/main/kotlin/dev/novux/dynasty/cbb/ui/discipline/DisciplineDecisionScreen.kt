package dev.novux.dynasty.cbb.ui.discipline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.discipline.DisciplineIncidentKind
import dev.novux.dynasty.cbb.core.discipline.DisciplineResolutionChoice
import dev.novux.dynasty.cbb.core.discipline.PendingDisciplineIncident
import dev.novux.dynasty.cbb.data.discipline.DisciplineRepository
import kotlinx.coroutines.launch

@Composable
fun DisciplineDecisionScreen(
    dynastyId: String,
    seasonIndex: Int,
    incident: PendingDisciplineIncident,
    disciplineRepository: DisciplineRepository,
    onResolved: () -> Unit,
    onBack: () -> Unit,
) {
    var suspendGames by remember { mutableFloatStateOf(3f) }
    var working by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun resolve(choice: DisciplineResolutionChoice, games: Int? = null) {
        if (working) return
        working = true
        scope.launch {
            disciplineRepository.resolve(dynastyId, seasonIndex, choice, games)
            working = false
            onResolved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = incident.headline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = incident.flavorBody,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = incident.playerDisplayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (incident.kind == DisciplineIncidentKind.Conduct) {
            OutlinedButton(
                onClick = { resolve(DisciplineResolutionChoice.Dismiss) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_dismiss_player)) }
            Text(stringResource(R.string.discipline_suspend_games_label))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Slider(
                    value = suspendGames,
                    onValueChange = { suspendGames = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.weight(1f),
                )
                Text("${suspendGames.toInt()}")
            }
            Button(
                onClick = { resolve(DisciplineResolutionChoice.SuspendGames, suspendGames.toInt()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_suspend_games)) }
            OutlinedButton(
                onClick = { resolve(DisciplineResolutionChoice.ConferenceDraw) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_conference_draw)) }
        } else {
            OutlinedButton(
                onClick = { resolve(DisciplineResolutionChoice.AcademicSweep) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_academic_sweep)) }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Slider(
                    value = suspendGames,
                    onValueChange = { suspendGames = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.weight(1f),
                )
                Text("${suspendGames.toInt()}")
            }
            Button(
                onClick = { resolve(DisciplineResolutionChoice.AcademicSuspendGames, suspendGames.toInt()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_academic_suspend)) }
            OutlinedButton(
                onClick = { resolve(DisciplineResolutionChoice.AcademicHoldSupport) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !working,
            ) { Text(stringResource(R.string.discipline_academic_support)) }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

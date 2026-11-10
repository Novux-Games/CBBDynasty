package dev.novux.dynasty.cbb.ui.offseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.calendar.OffseasonWeekHeading
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.ui.offseason.leaving.PlayersLeavingPanel
import kotlinx.coroutines.launch

/** Offseason shell — week header and advance ([planning/offseason.md]). */
@Composable
fun OffseasonScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    rosterRepository: DynastyRosterRepository,
    onExitHome: () -> Unit,
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    val scope = rememberCoroutineScope()
    val position = loaded.weekCheckpoint.activePosition
    val inOffseason = position.phase == CalendarPhase.Offseason
    val weekHeader = CalendarWeekLabel.display(position)
    val themeLine = if (inOffseason) {
        OffseasonWeekHeading.themeLine(position.weekIndex)
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.offseason_shell_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = loaded.summary.schoolDisplayName.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(
                R.string.offseason_shell_season,
                SeasonMembershipYear.label(loaded.metadata.seasonIndex),
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = weekHeader,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp),
        )
        themeLine?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (inOffseason && position.weekIndex == 1) {
            PlayersLeavingPanel(
                dynastyId = loaded.dynastyId,
                rosterRepository = rosterRepository,
                modifier = Modifier.weight(1f),
            )
        } else {
            Text(
                text = stringResource(R.string.offseason_shell_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
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
            enabled = inOffseason,
        ) {
            Text(
                if (inOffseason && position.weekIndex >= 10) {
                    stringResource(R.string.offseason_advance_to_preseason)
                } else {
                    stringResource(R.string.offseason_advance_week)
                },
            )
        }

        OutlinedButton(
            onClick = onExitHome,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import dev.novux.dynasty.cbb.core.save.SaveSlotSummary
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import kotlinx.coroutines.launch

/** Save slot picker — [planning/game-start.md#continue--load]. */
@Composable
fun LoadGameScreen(
    saveSlotRepository: SaveSlotRepository,
    onBack: () -> Unit,
    onDynastyLoaded: () -> Unit,
) {
    val slots by saveSlotRepository.observeSlots().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var pendingDelete by remember { mutableStateOf<SaveSlotSummary?>(null) }

    LaunchedEffect(Unit) {
        saveSlotRepository.ensureInitialized()
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(
            text = stringResource(R.string.menu_load),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.load_slots_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(slots, key = { it.slotIndex }) { slot ->
                SaveSlotRow(
                    slot = slot,
                    onOpen = {
                        scope.launch {
                            val loaded = saveSlotRepository.loadSlot(slot.slotIndex) ?: return@launch
                            ActiveDynastySession.set(loaded)
                            onDynastyLoaded()
                        }
                    },
                    onDelete = { pendingDelete = slot },
                )
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        ) {
            Text(stringResource(R.string.settings_back))
        }
    }

    pendingDelete?.let { slot ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(stringResource(R.string.load_delete_title)) },
            text = { Text(stringResource(R.string.load_delete_message, slot.slotIndex + 1)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            saveSlotRepository.deleteSlot(slot.slotIndex)
                            if (ActiveDynastySession.current?.slotIndex == slot.slotIndex) {
                                ActiveDynastySession.clear()
                            }
                            pendingDelete = null
                        }
                    },
                ) {
                    Text(stringResource(R.string.load_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(R.string.load_delete_cancel))
                }
            },
        )
    }
}

@Composable
private fun SaveSlotRow(
    slot: SaveSlotSummary,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
) {
    val label = if (slot.isEmpty) {
        stringResource(R.string.load_slot_empty, slot.slotIndex + 1)
    } else {
        stringResource(
            R.string.load_slot_occupied,
            slot.slotIndex + 1,
            slot.schoolDisplayName.orEmpty(),
            slot.seasonLabel.orEmpty(),
            slot.wins,
            slot.losses,
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !slot.isEmpty, onClick = onOpen)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            if (!slot.isEmpty && slot.coachDisplayName != null) {
                Text(
                    text = slot.coachDisplayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (!slot.isEmpty) {
            IconButton(onClick = onDelete) {
                Text(stringResource(R.string.load_delete_short))
            }
        }
    }
}

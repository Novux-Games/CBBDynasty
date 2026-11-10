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
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.save.SaveSlotSummary
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import kotlinx.coroutines.launch

/** Save slot picker — [planning/game-start.md#continue--load]. */
@Composable
fun LoadGameScreen(
    saveSlotRepository: SaveSlotRepository,
    onBack: () -> Unit,
    onDynastyLoaded: (LoadedDynasty) -> Unit,
) {
    val slots by saveSlotRepository.observeSlots().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var pendingDelete by remember { mutableStateOf<SaveSlotSummary?>(null) }
    var unavailableSlot by remember { mutableStateOf<SaveSlotSummary?>(null) }
    var loadableSlots by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }

    LaunchedEffect(Unit) {
        saveSlotRepository.ensureInitialized()
    }

    LaunchedEffect(slots) {
        loadableSlots = slots.associate { slot ->
            slot.slotIndex to if (slot.isEmpty) {
                true
            } else {
                saveSlotRepository.isSlotLikelyLoadable(slot.slotIndex)
            }
        }
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
                    likelyLoadable = loadableSlots[slot.slotIndex] != false,
                    onOpen = {
                        scope.launch {
                            when (val result = saveSlotRepository.loadSlot(slot.slotIndex)) {
                                is DynastyLoadResult.Success -> onDynastyLoaded(result.loaded)
                                is DynastyLoadResult.Unreadable -> unavailableSlot = slot
                                DynastyLoadResult.SlotEmpty -> Unit
                            }
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

    unavailableSlot?.let { slot ->
        SaveLoadUnavailableDialog(
            slotDisplayNumber = slot.slotIndex + 1,
            onDelete = {
                scope.launch {
                    saveSlotRepository.deleteSlot(slot.slotIndex)
                    if (ActiveDynastySession.current?.slotIndex == slot.slotIndex) {
                        ActiveDynastySession.clear()
                    }
                    unavailableSlot = null
                }
            },
            onDismiss = { unavailableSlot = null },
        )
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
    likelyLoadable: Boolean,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
) {
    val slotNumber = slot.slotIndex + 1
    val header = stringResource(R.string.load_slot_header, slotNumber)
    val detail = when {
        slot.isEmpty -> stringResource(R.string.load_slot_empty)
        !likelyLoadable -> stringResource(R.string.load_slot_cant_open)
        else -> stringResource(
            R.string.load_slot_occupied,
            slot.schoolDisplayName.orEmpty(),
            slot.seasonLabel.orEmpty(),
            slot.wins,
            slot.losses,
        )
    }
    val label = "$header — $detail"

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

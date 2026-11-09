package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.novux.dynasty.cbb.R

/** Fallback when an occupied slot fails validation — [planning/game-start.md]. */
@Composable
fun SaveLoadUnavailableDialog(
    slotDisplayNumber: Int,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.load_unavailable_title)) },
        text = {
            Text(
                stringResource(R.string.load_unavailable_message, slotDisplayNumber),
            )
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(stringResource(R.string.load_unavailable_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.load_unavailable_dismiss))
            }
        },
    )
}

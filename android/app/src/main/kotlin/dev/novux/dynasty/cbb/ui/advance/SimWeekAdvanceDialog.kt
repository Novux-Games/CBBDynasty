package dev.novux.dynasty.cbb.ui.advance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.advance.SimWeekConfirmation
import dev.novux.dynasty.cbb.core.advance.SimWeekConfirmationLineKind

@Composable
fun SimWeekAdvanceDialog(
    confirmation: SimWeekConfirmation,
    isAdvancing: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { if (!isAdvancing) onDismiss() },
        title = { Text(stringResource(R.string.advance_confirm_title)) },
        text = {
            Text(
                buildString {
                    confirmation.lines.forEach { line ->
                        appendLine(lineText(confirmation, line.kind, line.formatArgs))
                    }
                }.trim(),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isAdvancing) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isAdvancing) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    }
                    Text(
                        if (isAdvancing) {
                            stringResource(R.string.advance_confirm_advancing)
                        } else {
                            stringResource(R.string.advance_confirm_confirm)
                        },
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isAdvancing) {
                Text(stringResource(R.string.advance_confirm_cancel))
            }
        },
    )
}

@Composable
private fun lineText(
    confirmation: SimWeekConfirmation,
    kind: SimWeekConfirmationLineKind,
    args: List<Int>,
): String = when (kind) {
    SimWeekConfirmationLineKind.UnplayedGameFastSim ->
        stringResource(R.string.advance_confirm_line_fast_sim)
    SimWeekConfirmationLineKind.UserRecruitingAutoMaintain ->
        stringResource(R.string.advance_confirm_line_recruiting, args.firstOrNull() ?: 0)
    SimWeekConfirmationLineKind.LeagueRecruitingBatch ->
        stringResource(R.string.advance_confirm_line_league_recruiting)
    SimWeekConfirmationLineKind.PortalRecruitingWeek ->
        stringResource(R.string.advance_confirm_line_portal)
    SimWeekConfirmationLineKind.InjuryWeeksTick ->
        stringResource(R.string.advance_confirm_line_injury_tick, args.firstOrNull() ?: 0)
    SimWeekConfirmationLineKind.SimpleAdvance ->
        stringResource(R.string.advance_confirm_line_simple, confirmation.nextWeekLabel)
}

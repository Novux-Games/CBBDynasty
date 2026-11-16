package dev.novux.dynasty.cbb.ui.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.selection.SelectionSundayBubbleEngine
import dev.novux.dynasty.cbb.core.selection.SelectionSundayRevealPolicy
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionSundayRevealScreen(
    snapshot: SelectionSundaySnapshot,
    bubble: SelectionSundayBubbleEngine.BubbleSnapshot,
    onContinue: () -> Unit,
) {
  @Suppress("unused")
    val revealPolicy = SelectionSundayRevealPolicy.MEGA_ID
    @Suppress("unused")
    val bubblePolicy = SelectionSundayBubbleEngine.MEGA_ID
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.selection_sunday_title)) })
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.selection_sunday_summary, SelectionSundayRevealPolicy.summarize(snapshot)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.selection_sunday_bubble_summary, bubble.fieldSummary),
                style = MaterialTheme.typography.bodyLarge,
            )
            bubble.lines.forEach { line ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(line.label, fontWeight = FontWeight.SemiBold)
                        Text(
                            stringResource(
                                R.string.selection_sunday_bubble_line,
                                line.teamIn ?: "—",
                                line.teamOut ?: "—",
                            ),
                        )
                    }
                }
            }
        }
        OutlinedButton(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(stringResource(R.string.selection_sunday_continue))
        }
    }
}
package dev.novux.dynasty.cbb.ui.offseason.leaving

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.offseason.weekone.DraftStockLine
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository

@Composable
fun DraftResultsWeekTwoPanel(
    dynastyId: String,
    rosterRepository: DynastyRosterRepository,
    modifier: Modifier = Modifier,
) {
    var lines by remember { mutableStateOf<List<DraftStockLine>>(emptyList()) }

    LaunchedEffect(dynastyId) {
        val players = rosterRepository.listPlayers(dynastyId)
        lines = rosterRepository.weekTwoDraftLines(players)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.draft_results_week2_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        if (lines.isEmpty()) {
            Text(
                text = stringResource(R.string.draft_results_week2_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(lines, key = { it.playerId }) { line ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(line.displayLabel, fontWeight = FontWeight.Medium)
                            Text(
                                stringResource(
                                    R.string.draft_results_week2_line,
                                    line.stockPoints,
                                    line.mockTier,
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

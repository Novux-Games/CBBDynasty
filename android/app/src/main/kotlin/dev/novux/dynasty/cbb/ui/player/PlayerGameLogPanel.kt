package dev.novux.dynasty.cbb.ui.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.games.PlayerGameLogEntry

@Composable
fun PlayerGameLogPanel(
    entries: List<PlayerGameLogEntry>,
    onEntryClick: (PlayerGameLogEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        entries.forEach { entry ->
            PlayerGameLogRow(entry = entry, onClick = { onEntryClick(entry) })
        }
    }
}

@Composable
private fun PlayerGameLogRow(
    entry: PlayerGameLogEntry,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(
                        R.string.player_game_log_opponent_week,
                        entry.opponentLabel,
                        entry.rsWeekIndex,
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    stringResource(
                        R.string.player_game_log_result,
                        entry.resultLabel,
                        entry.scoreLabel,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (entry.won) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
            }
            Text(
                stringResource(
                    R.string.player_game_log_line,
                    entry.points,
                    entry.assists,
                    entry.rebounds,
                    entry.steals,
                    entry.blocks,
                    entry.turnovers,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            val venue = if (entry.isHome) {
                stringResource(R.string.player_game_log_home)
            } else {
                stringResource(R.string.player_game_log_away)
            }
            Text(
                venue,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

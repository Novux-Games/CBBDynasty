package dev.novux.dynasty.cbb.ui.offseason.leaving

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.offseason.weekone.DraftStockTiers
import dev.novux.dynasty.cbb.core.offseason.weekone.ProDayEligibility
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository

@Composable
fun ProDaysPanel(
    players: List<RosterPlayer>,
    rosterRepository: DynastyRosterRepository,
    dynastyId: String,
    onPlayersChanged: (List<RosterPlayer>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val eligible = players.filter { ProDayEligibility.eligible(it) }
    if (eligible.isEmpty()) return

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.pro_days_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(R.string.pro_days_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(eligible, key = { it.playerId }) { player ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(player.displayLabel(), fontWeight = FontWeight.Medium)
                        val stockLabel = if (player.proDayCompleted) {
                            stringResource(
                                R.string.pro_days_stock,
                                player.draftStockPoints,
                                DraftStockTiers.mockTier(player.draftStockPoints),
                            )
                        } else {
                            stringResource(R.string.pro_days_not_run)
                        }
                        Text(stockLabel, style = MaterialTheme.typography.bodyMedium)
                        if (!player.proDayCompleted) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        onPlayersChanged(
                                            rosterRepository.runProDay(dynastyId, player.playerId),
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.pro_days_run_player))
                            }
                        }
                    }
                }
            }
        }
        val pending = eligible.any { !it.proDayCompleted }
        if (pending) {
            Button(
                onClick = {
                    scope.launch {
                        onPlayersChanged(rosterRepository.simAllProDays(dynastyId))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.pro_days_sim_all))
            }
        }
    }
}

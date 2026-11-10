package dev.novux.dynasty.cbb.ui.offseason.leaving

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import kotlinx.coroutines.launch

@Composable
fun PlayersLeavingPanel(
    dynastyId: String,
    rosterRepository: DynastyRosterRepository,
    modifier: Modifier = Modifier,
) {
    var players by remember { mutableStateOf<List<RosterPlayer>>(emptyList()) }
    var tabIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId) {
        players = rosterRepository.prepareWeekOneLeaving(dynastyId)
    }

  val rosterTab = rosterRepository.rosterTab(players)
    val transferTab = rosterRepository.transferTab(players)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.leaving_week_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                text = { Text(stringResource(R.string.leaving_tab_roster)) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = { Text(stringResource(R.string.leaving_tab_transfer)) },
            )
        }
        val visible = if (tabIndex == 0) rosterTab else transferTab
        ProDaysPanel(
            players = players,
            rosterRepository = rosterRepository,
            dynastyId = dynastyId,
            onPlayersChanged = { players = it },
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(visible, key = { it.playerId }) { player ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(player.displayLabel(), fontWeight = FontWeight.Medium)
                        if (tabIndex == 0) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        players = rosterRepository.encourageTransfer(dynastyId, player.playerId)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.leaving_encourage_transfer))
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        players = rosterRepository.convinceStay(
                                            dynastyId,
                                            player.playerId,
                                            stayed = true,
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.leaving_convince_stay))
                            }
                        }
                    }
                }
            }
        }
    }
}

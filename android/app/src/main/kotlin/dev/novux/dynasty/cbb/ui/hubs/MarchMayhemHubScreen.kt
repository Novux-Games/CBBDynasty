package dev.novux.dynasty.cbb.ui.hubs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.march.MarchMayhemGameRow
import dev.novux.dynasty.cbb.core.march.MarchMayhemHubSnapshot
import dev.novux.dynasty.cbb.core.march.MarchMayhemInvitationalPanel
import dev.novux.dynasty.cbb.core.march.MarchMayhemNcbaPanel
import dev.novux.dynasty.cbb.core.march.MarchMayhemTeamRow

private enum class MarchMayhemTab { Ncba, Nci, Heritage }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarchMayhemHubScreen(
    snapshot: MarchMayhemHubSnapshot,
    onBack: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = MarchMayhemTab.entries

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.march_mayhem_hub_title)) })
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            when (tab) {
                                MarchMayhemTab.Ncba -> stringResource(R.string.march_mayhem_tab_ncba)
                                MarchMayhemTab.Nci -> stringResource(R.string.march_mayhem_tab_nci)
                                MarchMayhemTab.Heritage -> stringResource(R.string.march_mayhem_tab_heritage)
                            },
                        )
                    },
                )
            }
        }
        if (!snapshot.selectionPublished) {
            Text(
                text = stringResource(R.string.march_mayhem_hub_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
            )
        } else {
            when (tabs[tabIndex]) {
                MarchMayhemTab.Ncba -> NcbaTab(snapshot.ncba)
                MarchMayhemTab.Nci -> InvitationalTab(snapshot.nci)
                MarchMayhemTab.Heritage -> InvitationalTab(snapshot.heritage)
            }
        }
    }
}

@Composable
private fun NcbaTab(panel: MarchMayhemNcbaPanel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.march_mayhem_ncba_field, panel.field.size),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (panel.openingSimmed) {
            item {
                Text(
                    text = stringResource(R.string.march_mayhem_opening_advancers, panel.openingAdvancers.size),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        panel.champion?.let { champion ->
            item {
                Text(
                    text = stringResource(R.string.march_mayhem_champion_line, champion.seedLabel, champion.displayName),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        items(panel.field, key = { it.teamId }) { row ->
            TeamLine(row)
        }
        if (panel.games.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.march_mayhem_results_header),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            items(panel.games, key = { "${it.postseasonWeek}:${it.homeTeamId}:${it.awayTeamId}" }) { game ->
                GameLine(game)
            }
        }
    }
}

@Composable
private fun InvitationalTab(panel: MarchMayhemInvitationalPanel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.march_mayhem_invitational_field, panel.tournamentShortLabel, panel.field.size),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        panel.champion?.let { champion ->
            item {
                Text(
                    text = stringResource(R.string.march_mayhem_champion_line, champion.seedLabel, champion.displayName),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        items(panel.field, key = { it.teamId }) { row ->
            TeamLine(row)
        }
        if (panel.games.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.march_mayhem_results_header),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            items(panel.games, key = { "${it.postseasonWeek}:${it.homeTeamId}:${it.awayTeamId}" }) { game ->
                GameLine(game)
            }
        }
    }
}

@Composable
private fun TeamLine(row: MarchMayhemTeamRow) {
    val suffix = buildString {
        if (row.isUserTeam) append(" •")
        row.regionLabel?.let { append(" · $it") }
    }
    Text(
        text = stringResource(
            R.string.march_mayhem_team_line,
            row.seedLabel,
            row.displayName,
            suffix,
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun GameLine(game: MarchMayhemGameRow) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AssistChip(
            onClick = {},
            label = { Text(stringResource(R.string.march_mayhem_game_week, game.postseasonWeek)) },
        )
        Text(
            text = stringResource(
                R.string.march_mayhem_score_line,
                game.homeSeedLabel ?: "",
                game.homeDisplayName,
                game.homeScore,
                game.awaySeedLabel ?: "",
                game.awayDisplayName,
                game.awayScore,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

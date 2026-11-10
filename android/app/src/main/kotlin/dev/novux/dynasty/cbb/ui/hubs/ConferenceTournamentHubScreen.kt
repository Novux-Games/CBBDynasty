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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentHubGameLine
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentHubSnapshot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConferenceTournamentHubScreen(
    snapshot: ConferenceTournamentHubSnapshot,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.conf_tourney_hub_title)) })
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = snapshot.conferenceDisplayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(
                        R.string.conf_tourney_hub_weeks,
                        snapshot.leagueMaxConfWeeks,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (snapshot.structuralByeWeeks.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.conf_tourney_hub_structural_byes,
                            snapshot.structuralByeWeeks.joinToString(),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (!snapshot.bracketSimmed) {
                item {
                    Text(
                        text = stringResource(R.string.conf_tourney_hub_pending_sim),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                item {
                    ChampionBlock(snapshot)
                }
                items(snapshot.games, key = { "${it.confTourneyWeek}:${it.homeTeamId}:${it.awayTeamId}" }) { line ->
                    GameLine(snapshot, line)
                }
            }
            item {
                QualifiersBlock(snapshot)
            }
        }
    }
}

@Composable
private fun ChampionBlock(snapshot: ConferenceTournamentHubSnapshot) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        snapshot.championTeamId?.let { id ->
            Text(
                text = stringResource(
                    R.string.conf_tourney_hub_champion,
                    snapshot.teamDisplayNames[id] ?: id,
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        snapshot.runnerUpTeamId?.let { id ->
            Text(
                text = stringResource(
                    R.string.conf_tourney_hub_runner_up,
                    snapshot.teamDisplayNames[id] ?: id,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        snapshot.ncbaAutoBidTeamId?.let { id ->
            Text(
                text = stringResource(
                    R.string.conf_tourney_hub_auto_bid,
                    snapshot.teamDisplayNames[id] ?: id,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun GameLine(
    snapshot: ConferenceTournamentHubSnapshot,
    line: ConferenceTournamentHubGameLine,
) {
    val home = snapshot.teamDisplayNames[line.homeTeamId] ?: line.homeTeamId
    val away = snapshot.teamDisplayNames[line.awayTeamId] ?: line.awayTeamId
    Column(modifier = Modifier.fillMaxWidth()) {
        AssistChip(
            onClick = {},
            label = {
                Text(
                    stringResource(R.string.conf_tourney_hub_game_week, line.confTourneyWeek),
                )
            },
        )
        Text(
            text = stringResource(
                R.string.conf_tourney_hub_score_line,
                home,
                line.homeScore,
                away,
                line.awayScore,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun QualifiersBlock(snapshot: ConferenceTournamentHubSnapshot) {
    Text(
        text = stringResource(R.string.conf_tourney_hub_qualifiers),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
    )
    snapshot.qualifiedTeamIds.forEachIndexed { index, teamId ->
        val label = snapshot.teamDisplayNames[teamId] ?: teamId
        val marker = if (teamId == snapshot.userTeamId) " •" else ""
        Text(
            text = "${index + 1}. $label$marker",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

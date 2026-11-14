package dev.novux.dynasty.cbb.ui.dynasty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.news.NewsHeadline
import dev.novux.dynasty.cbb.core.news.NewsHeadlineKind
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.roster.ShellRosterSection
import dev.novux.dynasty.cbb.core.roster.ShellRosterSectionPartition
import dev.novux.dynasty.cbb.ui.player.PlayerInjuryRosterRow
import dev.novux.dynasty.cbb.ui.player.PlayerLoyaltyChips

@Composable
fun DynastyShellRosterTab(
    players: List<DynastyPlayer>,
    contentPadding: PaddingValues,
    onPlayerClick: (DynastyPlayer) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.shell_roster_tab_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        if (players.isEmpty()) {
            Text(
                text = stringResource(R.string.shell_roster_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            val rosterPlayers = players.map { it.toRosterPlayer() }
            ShellRosterSectionPartition.group(rosterPlayers).forEach { section ->
                Text(
                    text = shellRosterSectionTitle(section.section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp),
                )
                val dynastyById = players.associateBy { it.playerId }
                section.players.forEach { rosterPlayer ->
                    val player = dynastyById.getValue(rosterPlayer.playerId)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayerClick(player) },
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            PlayerInjuryRosterRow(player = rosterPlayer)
                            PlayerLoyaltyChips(player)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun shellRosterSectionTitle(section: ShellRosterSection): String = when (section) {
    ShellRosterSection.Starters -> stringResource(R.string.shell_roster_section_starters)
    ShellRosterSection.Reserves -> stringResource(R.string.shell_roster_section_reserves)
    ShellRosterSection.BenchWarmers -> stringResource(R.string.shell_roster_section_bench)
}

@Composable
fun DynastyShellCalendarTabPlaceholder(contentPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.shell_tab_calendar),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(R.string.shell_calendar_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun DynastyShellNewsTab(
    headlines: List<NewsHeadline>,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.shell_tab_news),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        if (headlines.isEmpty()) {
            Text(
                text = stringResource(R.string.shell_news_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            headlines.forEach { headline ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = newsKindLabel(headline.kind),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(text = headline.title, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun newsKindLabel(kind: NewsHeadlineKind): String = when (kind) {
    NewsHeadlineKind.UserInjury -> stringResource(R.string.shell_news_kind_user_injury)
    NewsHeadlineKind.LeagueInjury -> stringResource(R.string.shell_news_kind_league_injury)
    NewsHeadlineKind.LeagueResult -> stringResource(R.string.shell_news_kind_league_result)
    NewsHeadlineKind.RecruitingSpotlight -> stringResource(R.string.shell_news_kind_recruiting)
    NewsHeadlineKind.General -> stringResource(R.string.shell_news_kind_general)
    NewsHeadlineKind.Awards -> stringResource(R.string.shell_news_kind_awards)
    NewsHeadlineKind.Bracketology -> stringResource(R.string.shell_news_kind_bracketology)
}

@Composable
fun DynastyShellNewsTabPlaceholder(contentPadding: PaddingValues) {
    DynastyShellNewsTab(headlines = emptyList(), contentPadding = contentPadding)
}

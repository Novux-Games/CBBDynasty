package dev.novux.dynasty.cbb.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.PlayerInjuryState
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

@Composable
fun PlayerInjuryRosterRow(
    player: RosterPlayer,
    modifier: Modifier = Modifier,
    nameWeight: Float = 1f,
) {
    val strip = player.toDynastyPlayer().injury.injuryStripOrNull()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = player.displayLabel(),
            modifier = Modifier.weight(nameWeight),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (strip != null) {
            AssistChip(
                onClick = {},
                label = { Text(stringResource(R.string.injury_roster_out_chip)) },
            )
            Text(
                text = stringResource(
                    R.string.injury_roster_weeks_suffix,
                    strip.label,
                    strip.weeksRemaining,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun PlayerInjuryOverviewBlock(injury: PlayerInjuryState, modifier: Modifier = Modifier) {
    val strip = injury.injuryStripOrNull() ?: return
    val severity = injury.severityWire?.replaceFirstChar { it.uppercase() } ?: ""
    Text(
        text = stringResource(
            R.string.player_card_injury_detail,
            strip.label,
            severity.ifBlank { "—" },
            strip.weeksRemaining,
        ),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier,
    )
}

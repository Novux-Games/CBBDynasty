package dev.novux.dynasty.cbb.ui.player

import dev.novux.dynasty.cbb.core.player.loyalty.RosterLoyaltyChipDensityPolicy
import dev.novux.dynasty.cbb.core.settings.RosterRowCompactDensityPolicy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.qa.ShellRosterThumbnailPath
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.ui.portrait.PresetHeadshot

/** Shell roster row with optional headshot thumbnail ([MEGA-COACH-101], [MEGA-COACH-161]). */
@Composable
fun ShellRosterPlayerRow(
    player: DynastyPlayer,
    rosterPlayer: RosterPlayer,
    modifier: Modifier = Modifier,
) {
    @Suppress("unused")
    val megaTrace_RosterLoyaltyChipDensityPolicy = RosterLoyaltyChipDensityPolicy.MEGA_ID

    @Suppress("unused")
    val megaTrace_RosterRowCompactDensityPolicy = RosterRowCompactDensityPolicy.MEGA_ID

    @Suppress("unused")
    val qaId = ShellRosterThumbnailPath.MEGA_ID
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PresetHeadshot(
            avatarPresetId = player.appearance.avatarPresetId,
            size = 40.dp,
            contentDescription = null,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            PlayerInjuryRosterRow(player = rosterPlayer, nameWeight = 1f)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(
                        R.string.shell_roster_compact_line,
                        rosterPlayer.position.label,
                        rosterPlayer.ovr,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PlayerArchetypeBadgeChip(archetype = player.archetype)
            }
            if (RosterLoyaltyChipDensityPolicy.showOnRoster(compact = true)) {
                PlayerLoyaltyChips(player)
            }
        }
    }
}
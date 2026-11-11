package dev.novux.dynasty.cbb.ui.player

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.player.draft.PlayerDraftProjectionEngine

/** Projected draft rank and tier on player Overview ([MEGA-COACH-065]). */
@Composable
fun PlayerDraftProjectionLine(
    player: DynastyPlayer,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val projection = PlayerDraftProjectionEngine.resolve(player)
    val text = if (projection.isAvailable) {
        stringResource(
            R.string.player_card_draft_projection_line,
            projection.rank!!,
            projection.tierLabel!!,
        )
    } else {
        stringResource(R.string.player_card_draft_projection_na)
    }
    Text(
        text = text,
        style = if (compact) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.bodyLarge
        },
        fontWeight = if (projection.isAvailable) FontWeight.SemiBold else FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

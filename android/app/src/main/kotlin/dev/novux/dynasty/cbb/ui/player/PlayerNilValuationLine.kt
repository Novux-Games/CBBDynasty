package dev.novux.dynasty.cbb.ui.player

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.DynastyPlayer

/** Annual program NIL valuation on player Overview ([MEGA-COACH-064]). */
@Composable
fun PlayerNilValuationLine(
    player: DynastyPlayer,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Text(
        text = stringResource(R.string.player_card_nil_line, player.identity.nilUsdPerYear),
        style = if (compact) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.bodyLarge
        },
        fontWeight = if (player.identity.nilUsdPerYear > 0) FontWeight.SemiBold else FontWeight.Normal,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}

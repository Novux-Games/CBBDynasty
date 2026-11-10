package dev.novux.dynasty.cbb.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.player.loyalty.PromiseStatus

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerLoyaltyChips(player: DynastyPlayer, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AssistChip(
            onClick = {},
            label = {
                Text(
                    stringResource(R.string.player_loyalty_chip, player.loyalty.loyalty.value),
                )
            },
        )
        player.loyalty.promise?.let { promise ->
            val statusRes = when (promise.status) {
                PromiseStatus.Kept -> R.string.player_promise_status_kept
                PromiseStatus.Broken -> R.string.player_promise_status_broken
                PromiseStatus.AtRisk -> R.string.player_promise_status_at_risk
                PromiseStatus.Deferred -> R.string.player_promise_status_deferred
                PromiseStatus.Pending -> R.string.player_promise_status_pending
            }
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        stringResource(
                            R.string.player_promise_chip,
                            promise.displayLabel(),
                            stringResource(statusRes),
                        ),
                    )
                },
            )
        }
    }
}

@Composable
fun PlayerLoyaltyRatingsRow(player: DynastyPlayer, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.player_ratings_loyalty_row, player.loyalty.loyalty.value),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(vertical = 4.dp),
    )
}

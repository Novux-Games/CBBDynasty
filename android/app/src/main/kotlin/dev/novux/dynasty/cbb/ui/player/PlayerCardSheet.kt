package dev.novux.dynasty.cbb.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.player.scout.ScoutDepth
import dev.novux.dynasty.cbb.ui.portrait.PlayerPortraitSlot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCardPreviewSheet(
    player: DynastyPlayer?,
    onDismiss: () -> Unit,
    onViewMore: (DynastyPlayer) -> Unit,
) {
    if (player == null) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Text(
                stringResource(R.string.player_card_preview_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PlayerPortraitSlot(
                displayName = player.identity.displayName,
                avatarPresetId = player.appearance.avatarPresetId,
                playerId = player.playerId,
            )
            PlayerCardOverviewContent(
                player = player,
                modifier = Modifier.padding(bottom = 8.dp),
                compact = true,
            )
            Button(
                onClick = {
                    onDismiss()
                    onViewMore(player)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            ) {
                Text(stringResource(R.string.coach_preview_view_more))
            }
        }
    }
}

/** @deprecated Use [PlayerCardPreviewSheet]; kept as alias for callers during migration. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCardSheet(
    player: DynastyPlayer?,
    onDismiss: () -> Unit,
) = PlayerCardPreviewSheet(player = player, onDismiss = onDismiss, onViewMore = { })

@Composable
fun PlayerCardOverviewContent(
    player: DynastyPlayer,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val report = player.scoutReport(ScoutDepth.Light)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = player.identity.displayName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(
                R.string.player_card_identity,
                player.position.label,
                player.classYear.label,
                player.archetype.displayName,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PlayerInjuryOverviewBlock(player.injury)
        PlayerLoyaltyChips(player)
        HorizontalDivider()
        Text(
            text = stringResource(R.string.player_card_overview),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(
                R.string.player_card_hometown,
                player.identity.hometownCity,
                player.identity.hometownState,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(
                R.string.player_card_measurements,
                player.identity.heightInches / 12,
                player.identity.heightInches % 12,
                player.identity.weightLbs,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        PlayerNilValuationLine(player = player, compact = compact)
        PlayerDraftProjectionLine(player = player, compact = compact)
        if (!compact) {
            HorizontalDivider()
            Text(
                text = stringResource(R.string.player_card_ratings_tab),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (compact) {
            Text(
                text = stringResource(
                    R.string.player_card_headline_ratings,
                    player.ratings.ovr,
                    player.ratings.off,
                    player.ratings.def,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            PlayerRatingAttributesPanel(ratings = player.ratings)
            PlayerLoyaltyRatingsRow(player)
        }
        Text(
            text = stringResource(R.string.player_card_scout_ovr, report.overall),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

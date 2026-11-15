package dev.novux.dynasty.cbb.ui.signingday

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.signingday.SigningDayPositionFlexPolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigningDayPositionPickerSheet(
    displayName: String,
    scoutedPosition: PlayerPosition,
    currentPosition: PlayerPosition,
    onDismiss: () -> Unit,
    onSelect: (PlayerPosition) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val options = SigningDayPositionFlexPolicy.allowedListedPositions(scoutedPosition)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.signing_day_position_picker_title, displayName),
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(
                    R.string.signing_day_position_picker_scouted,
                    scoutedPosition.label,
                ),
                modifier = Modifier.padding(vertical = 8.dp),
            )
            options.forEach { pos ->
                val selected = pos == currentPosition
                Text(
                    text = stringResource(
                        if (selected) {
                            R.string.signing_day_position_picker_option_selected
                        } else {
                            R.string.signing_day_position_picker_option
                        },
                        pos.label,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(pos)
                            onDismiss()
                        }
                        .padding(vertical = 10.dp),
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}
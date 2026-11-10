package dev.novux.dynasty.cbb.ui.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.employment.JobOfferCard

@Composable
fun JobCenterScreen(
    offers: List<JobOfferCard>,
    onAccept: (JobOfferCard) -> Unit,
    onWait: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            stringResource(R.string.job_center_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            stringResource(R.string.job_center_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(offers, key = { it.teamId }) { offer ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(offer.displayName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        stringResource(
                            R.string.job_center_offer_meta,
                            offer.programPrestige,
                            offer.conferenceId,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Button(
                        onClick = { onAccept(offer) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.job_center_accept))
                    }
                }
            }
        }
        OutlinedButton(onClick = onWait, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.job_center_decline_wait))
        }
    }
}

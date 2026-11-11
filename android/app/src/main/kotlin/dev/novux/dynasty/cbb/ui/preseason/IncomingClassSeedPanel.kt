package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.preseason.Top60NewsHeadline
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow
import dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun IncomingClassSeedPanel(
    dynastyId: String,
    seasonIndex: Int,
    seedRepository: IncomingClassSeedRepository,
    progressRepository: PreseasonProgressRepository,
    onSeeded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var headline by remember { mutableStateOf<Top60NewsHeadline?>(null) }
    var seeding by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId, seasonIndex) {
        if (withContext(Dispatchers.IO) { seedRepository.isSeeded(dynastyId, seasonIndex) }) {
            headline = withContext(Dispatchers.IO) { seedRepository.loadHeadline(dynastyId, seasonIndex) }
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.incoming_seed_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        headline?.let { news ->
            Text(
                text = news.headline,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.incoming_seed_top60_heading),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                news.top60.forEach { row ->
                    Top60Row(row)
                }
            }
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    seeding = true
                    try {
                        if (headline == null) {
                            headline = withContext(Dispatchers.IO) {
                                seedRepository.runSeed(dynastyId, seasonIndex)
                            }
                        }
                        withContext(Dispatchers.IO) {
                            progressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.IncomingClassSeed)
                        }
                        onSeeded()
                    } finally {
                        seeding = false
                    }
                }
            },
            enabled = !seeding,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (seeding) {
                    stringResource(R.string.incoming_seed_running)
                } else if (headline != null) {
                    stringResource(R.string.incoming_seed_confirm)
                } else {
                    stringResource(R.string.incoming_seed_run)
                },
            )
        }
    }
}

@Composable
private fun Top60Row(row: NationalRankRow) {
    Text(
        text = stringResource(
            R.string.incoming_seed_top60_line,
            row.rank,
            row.prospect.displayName,
            row.prospect.stars,
            row.prospect.position.label,
            row.prospect.homeState,
        ),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp),
    )
}

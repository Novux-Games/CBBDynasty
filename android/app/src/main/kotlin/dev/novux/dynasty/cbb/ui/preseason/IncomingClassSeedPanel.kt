package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.IncomingClassSeedCancelPolicy
import dev.novux.dynasty.cbb.core.preseason.IncomingClassSeedReseedGuardPolicy
import dev.novux.dynasty.cbb.core.preseason.IncomingClassSeedRunPhase
import dev.novux.dynasty.cbb.core.preseason.IncomingClassSeedRunProgress
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
    var poolSeeded by remember { mutableStateOf(false) }
    var seeding by remember { mutableStateOf(false) }
    var seedCancelled by remember { mutableStateOf(false) }
    var runPhase by remember { mutableStateOf<IncomingClassSeedRunPhase?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId, seasonIndex) {
        poolSeeded = withContext(Dispatchers.IO) { seedRepository.isSeeded(dynastyId, seasonIndex) }
        if (poolSeeded) {
            headline = withContext(Dispatchers.IO) { seedRepository.loadHeadline(dynastyId, seasonIndex) }
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.incoming_seed_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (seeding) {
            val phase = runPhase ?: IncomingClassSeedRunPhase.RollingHighSchoolPool
            LinearProgressIndicator(
                progress = { IncomingClassSeedRunProgress.fraction(phase) },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Text(
                    text = stringResource(incomingSeedPhaseLabelRes(phase)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (IncomingClassSeedCancelPolicy.allowCancel(seeding)) {
                OutlinedButton(
                    onClick = {
                        seedCancelled = true
                        seeding = false
                        runPhase = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.incoming_seed_cancel))
                }
            }
        }
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
                    seedCancelled = false
                    seeding = true
                    runPhase = IncomingClassSeedRunPhase.RollingHighSchoolPool
                    try {
                        if (seedCancelled) return@launch
                        if (
                            headline == null &&
                            IncomingClassSeedReseedGuardPolicy.mayRunInitialSeed(poolSeeded)
                        ) {
                            headline = withContext(Dispatchers.IO) {
                                seedRepository.runSeed(dynastyId, seasonIndex) { phase ->
                                    scope.launch(Dispatchers.Main.immediate) {
                                        runPhase = phase
                                    }
                                }
                            }
                            poolSeeded = true
                        }
                        if (seedCancelled) return@launch
                        withContext(Dispatchers.IO) {
                            progressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.IncomingClassSeed)
                        }
                        onSeeded()
                    } finally {
                        seeding = false
                        runPhase = null
                    }
                }
            },
            enabled = when {
                seeding -> false
                headline != null -> IncomingClassSeedReseedGuardPolicy.mayConfirmExistingHeadline(
                    headlinePresent = true,
                    seedingInFlight = false,
                )
                else -> IncomingClassSeedReseedGuardPolicy.mayRunInitialSeed(poolSeeded)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (seeding) {
                    stringResource(R.string.incoming_seed_running)
                } else if (headline != null) {
                    stringResource(R.string.incoming_seed_confirm)
                } else if (!IncomingClassSeedReseedGuardPolicy.mayRunInitialSeed(poolSeeded)) {
                    stringResource(R.string.incoming_seed_already_seeded)
                } else {
                    stringResource(R.string.incoming_seed_run)
                },
            )
        }
    }
}

@Composable
private fun incomingSeedPhaseLabelRes(phase: IncomingClassSeedRunPhase): Int = when (phase) {
    IncomingClassSeedRunPhase.RollingHighSchoolPool -> R.string.incoming_seed_progress_hs
    IncomingClassSeedRunPhase.RollingJucoPool -> R.string.incoming_seed_progress_juco
    IncomingClassSeedRunPhase.RankingNationalRecruits -> R.string.incoming_seed_progress_rank
    IncomingClassSeedRunPhase.PublishingTop60Headline -> R.string.incoming_seed_progress_headline
    IncomingClassSeedRunPhase.SavingClassPool -> R.string.incoming_seed_progress_save
    IncomingClassSeedRunPhase.Done -> R.string.incoming_seed_done
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

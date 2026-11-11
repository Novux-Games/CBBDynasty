package dev.novux.dynasty.cbb.ui.signingday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.signingday.IncomingSignee
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow
import dev.novux.dynasty.cbb.core.signingday.NationalRecruitingRankings
import dev.novux.dynasty.cbb.core.signingday.SigningDayProgress
import dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository
import dev.novux.dynasty.cbb.data.nil.NilAnnualPoolRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekNineRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import dev.novux.dynasty.cbb.data.signingday.SigningDayRepository
import kotlinx.coroutines.launch

enum class SigningDayInitialTab {
    Rankings,
    Positions,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigningDayScreen(
    loaded: LoadedDynasty,
    signingDayRepository: SigningDayRepository,
    recruitingEconomyRepository: RecruitingEconomyRepository,
    nilAnnualPoolRepository: NilAnnualPoolRepository,
    weekNineRepository: OffseasonWeekNineRepository? = null,
    incomingClassSeedRepository: IncomingClassSeedRepository,
    initialTab: SigningDayInitialTab = SigningDayInitialTab.Rankings,
    onBack: () -> Unit,
) {
    var tabIndex by remember(initialTab) {
        mutableIntStateOf(if (initialTab == SigningDayInitialTab.Positions) 1 else 0)
    }
    var progress by remember { mutableStateOf<SigningDayProgress?>(null) }
    var incoming by remember { mutableStateOf<List<IncomingSignee>>(emptyList()) }
    var rankings by remember { mutableStateOf<List<NationalRankRow>>(emptyList()) }
    val scope = rememberCoroutineScope()

    suspend fun refresh() {
        progress = signingDayRepository.loadProgress(loaded.dynastyId, loaded.metadata.seasonIndex)
        incoming = signingDayRepository.loadIncomingClass(loaded.dynastyId, loaded.metadata.seasonIndex)
    }

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex) {
        val pool = incomingClassSeedRepository.loadPool(loaded.dynastyId, loaded.metadata.seasonIndex)
        rankings = if (pool != null) {
            NationalRecruitingRankings.top60FromPool(pool)
        } else {
            NationalRecruitingRankings.top60(loaded.metadata.seasonIndex)
        }
        refresh()
        if (tabIndex == 0) {
            signingDayRepository.markRankingsViewed(loaded.dynastyId, loaded.metadata.seasonIndex)
            progress = signingDayRepository.loadProgress(loaded.dynastyId, loaded.metadata.seasonIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.signing_day_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.signing_day_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = {
                    tabIndex = 0
                    scope.launch {
                        signingDayRepository.markRankingsViewed(loaded.dynastyId, loaded.metadata.seasonIndex)
                        refresh()
                    }
                },
                text = { Text(stringResource(R.string.signing_day_tab_rankings)) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = { Text(stringResource(R.string.signing_day_tab_positions)) },
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            when (tabIndex) {
                0 -> RankingsBody(rankings)
                else -> PositionsBody(
                signees = incoming,
                progress = progress,
                onCyclePosition = { signee ->
                    scope.launch {
                        val next = PlayerPosition.entries[
                            (PlayerPosition.entries.indexOf(signee.position) + 1) % PlayerPosition.entries.size
                        ]
                        signingDayRepository.savePositionOverride(loaded.dynastyId, signee.prospectId, next)
                        refresh()
                    }
                },
                onConfirmPositions = {
                    scope.launch {
                        signingDayRepository.confirmPositions(loaded.dynastyId, loaded.metadata.seasonIndex)
                        refresh()
                    }
                },
                onHardSign = {
                    scope.launch {
                        val boost = weekNineRepository?.nilPoolBoostUsd(
                            loaded.dynastyId,
                            loaded.metadata.seasonIndex,
                        ) ?: 0
                        val pool = nilAnnualPoolRepository.effectivePoolUsd(loaded, boost)
                        signingDayRepository.hardSign(loaded.dynastyId, loaded.metadata.seasonIndex, pool)
                        recruitingEconomyRepository.closeCycleAfterHardSign(loaded.dynastyId)
                        refresh()
                    }
                },
                )
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

@Composable
private fun RankingsBody(rows: List<NationalRankRow>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(rows, key = { it.prospect.prospectId }) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(
                        R.string.signing_day_rank_line,
                        row.rank,
                        row.prospect.displayName,
                        row.prospect.stars,
                        row.prospect.position.label,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun PositionsBody(
    signees: List<IncomingSignee>,
    progress: SigningDayProgress?,
    onCyclePosition: (IncomingSignee) -> Unit,
    onConfirmPositions: () -> Unit,
    onHardSign: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (signees.isEmpty()) {
            Text(
                text = stringResource(R.string.signing_day_no_commits),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(signees, key = { it.prospectId }) { signee ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(signee.displayName, fontWeight = FontWeight.SemiBold)
                            Text(
                                stringResource(
                                    R.string.signing_day_signee_meta,
                                    signee.stars,
                                    signee.position.label,
                                ),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        OutlinedButton(onClick = { onCyclePosition(signee) }) {
                            Text(stringResource(R.string.signing_day_cycle_position))
                        }
                    }
                }
            }
        }
        val canHardSign = progress?.requiredTasksComplete() == true && progress.hardSignComplete.not()
        Button(
            onClick = onConfirmPositions,
            modifier = Modifier.fillMaxWidth(),
            enabled = progress?.positionsConfirmed != true,
        ) {
            Text(stringResource(R.string.signing_day_confirm_positions))
        }
        Button(
            onClick = onHardSign,
            modifier = Modifier.fillMaxWidth(),
            enabled = canHardSign,
        ) {
            Text(
                if (progress?.hardSignComplete == true) {
                    stringResource(R.string.signing_day_hard_sign_done)
                } else {
                    stringResource(R.string.signing_day_hard_sign)
                },
            )
        }
    }
}

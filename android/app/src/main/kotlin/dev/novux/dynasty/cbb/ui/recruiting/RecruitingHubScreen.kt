package dev.novux.dynasty.cbb.ui.recruiting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.novux.dynasty.cbb.core.recruiting.RecruitingActionBlockReason
import dev.novux.dynasty.cbb.core.recruiting.RecruitingProspectActionGates
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommendedParser
import dev.novux.dynasty.cbb.core.recruiting.RecruitingClassSeed
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubDefaults
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubTab
import dev.novux.dynasty.cbb.core.recruiting.RecommendedProspectRow
import dev.novux.dynasty.cbb.core.recruiting.RecommendedRecruitList
import dev.novux.dynasty.cbb.core.recruiting.SchoolRecruitingContext
import dev.novux.dynasty.cbb.core.recruiting.SignLikelihoodBadge
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekNineRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun RecruitingHubScreen(
    loaded: LoadedDynasty,
    recruitingEconomyRepository: RecruitingEconomyRepository,
    weekNineRepository: OffseasonWeekNineRepository? = null,
    initialTab: RecruitingHubTab = RecruitingHubDefaults.defaultTab,
    onBack: () -> Unit,
) {
    var tabIndex by remember(initialTab) {
        mutableIntStateOf(RecruitingHubDefaults.tabs.indexOf(initialTab))
    }
    var ledger by remember { mutableStateOf<RecruitingCycleLedger?>(null) }
    var nilBoostUsd by remember { mutableIntStateOf(0) }
    var gateRefresh by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    LaunchedEffect(loaded.dynastyId, gateRefresh) {
        ledger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
        nilBoostUsd = weekNineRepository?.nilPoolBoostUsd(loaded.dynastyId, loaded.metadata.seasonIndex) ?: 0
    }


    val recommendedConfig = remember(loaded.balanceRuleset) {
        BalanceRecruitingRecommendedParser.parse(loaded.balanceRuleset.recruiting)
    }
    val schoolContext = remember(loaded, nilBoostUsd) {
        SchoolRecruitingContext(
            programPrestige = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
            schoolHomeState = "OH",
            pipelineStates = setOf("KY", "IN", "MI", "PA", "WV"),
            nilPoolAnnualUsd = 900_000 + nilBoostUsd,
        )
    }
    val recommendedRows = remember(loaded.metadata.seasonIndex, schoolContext, recommendedConfig) {
        RecommendedRecruitList.build(
            RecruitingClassSeed.demoPool(loaded.metadata.seasonIndex),
            schoolContext,
            recommendedConfig,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.recruiting_hub_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.recruiting_hub_cycle_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        ledger?.let { row ->
            Text(
                text = stringResource(
                    R.string.recruiting_points_pool,
                    row.pointsAvailable,
                    row.weeklyPoolBase,
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (nilBoostUsd > 0) {
            Text(
                text = stringResource(R.string.recruiting_nil_boost_line, currency.format(nilBoostUsd)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                text = { Text(stringResource(R.string.recruiting_tab_recommended)) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = { Text(stringResource(R.string.recruiting_tab_hs_juco)) },
            )
            Tab(
                selected = tabIndex == 2,
                onClick = { tabIndex = 2 },
                text = { Text(stringResource(R.string.recruiting_tab_portal)) },
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            when (RecruitingHubDefaults.tabs[tabIndex]) {
                RecruitingHubTab.Recommended -> RecommendedTabBody(
                    loaded = loaded,
                    rows = recommendedRows,
                    schoolContext = schoolContext,
                    recruitingEconomyRepository = recruitingEconomyRepository,
                    gateRefresh = gateRefresh,
                    onSpend = {
                        scope.launch {
                            recruitingEconomyRepository.spendRefundableAction(
                                loaded,
                                prospectId = it.prospectId,
                                offer = false,
                                scout = it.scout,
                                officialVisit = it.officialVisit,
                                visitDistance = recruitingEconomyRepository.visitDistanceFor(
                                    it.prospectHomeState,
                                    schoolContext,
                                ),
                            )
                            ledger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
                            gateRefresh++
                        }
                    },
                )
                RecruitingHubTab.HsJuco -> PlaceholderBody(stringResource(R.string.recruiting_placeholder_hs_juco))
                RecruitingHubTab.TransferPortal -> PlaceholderBody(stringResource(R.string.recruiting_placeholder_portal))
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

@Composable
private fun PlaceholderBody(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
    )
}

private data class ProspectSpendRequest(
    val prospectId: String,
    val prospectHomeState: String,
    val scout: Boolean,
    val officialVisit: Boolean,
)

@Composable
private fun RecommendedTabBody(
    loaded: LoadedDynasty,
    rows: List<RecommendedProspectRow>,
    schoolContext: SchoolRecruitingContext,
    recruitingEconomyRepository: RecruitingEconomyRepository,
    gateRefresh: Int,
    onSpend: (ProspectSpendRequest) -> Unit,
) {
    val likelyCount = rows.count { it.badge == SignLikelihoodBadge.Likely || it.badge == SignLikelihoodBadge.Possible }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.recruiting_recommended_summary, likelyCount, rows.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(rows, key = { it.prospect.prospectId }) { row ->
            var gates by remember(row.prospect.prospectId, gateRefresh) {
                mutableStateOf<RecruitingProspectActionGates?>(null)
            }
            LaunchedEffect(row.prospect.prospectId, gateRefresh, loaded.dynastyId) {
                gates = recruitingEconomyRepository.prospectActionGates(
                    loaded,
                    row.prospect.prospectId,
                    row.prospect.homeState,
                    schoolContext,
                )
            }
            RecommendedProspectRow(
                row = row,
                gates = gates,
                onScout = {
                    onSpend(
                        ProspectSpendRequest(
                            row.prospect.prospectId,
                            row.prospect.homeState,
                            scout = true,
                            officialVisit = false,
                        ),
                    )
                },
                onOfficialVisit = {
                    onSpend(
                        ProspectSpendRequest(
                            row.prospect.prospectId,
                            row.prospect.homeState,
                            scout = false,
                            officialVisit = true,
                        ),
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecommendedProspectRow(
    row: RecommendedProspectRow,
    gates: RecruitingProspectActionGates?,
    onScout: () -> Unit,
    onOfficialVisit: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = row.prospect.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            Text(
                text = stringResource(
                    R.string.recruiting_row_meta,
                    row.prospect.stars,
                    row.prospect.position.label,
                    row.prospect.homeState,
                    row.prospect.scoutLetterOverall,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (row.prospect.topPriorities.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.prospect.topPriorities.forEach { priority ->
                        AssistChip(
                            onClick = {},
                            label = { Text(priority.chipLabel) },
                        )
                    }
                }
            }
            row.influenceBoard.userRank?.let { rank ->
                Text(
                    text = stringResource(
                        R.string.recruiting_influence_row,
                        rank,
                        row.influenceBoard.closeness,
                        row.baselineInterest,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        SignLikelihoodChip(row.badge)
        }
        gates?.let { g ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onScout,
                    enabled = g.scoutTheirGame.allowed,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.recruiting_action_scout_game, g.scoutTheirGame.pointCost))
                }
                OutlinedButton(
                    onClick = onOfficialVisit,
                    enabled = g.officialVisit.allowed,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        stringResource(
                            R.string.recruiting_action_official_visit,
                            g.officialVisit.pointCost,
                        ),
                    )
                }
            }
            val hint = blockReasonHint(g)
            if (hint != null) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun blockReasonHint(gates: RecruitingProspectActionGates): String? {
    val scoutReason = gates.scoutTheirGame.blockReason
    val visitReason = gates.officialVisit.blockReason
    val reason = scoutReason ?: visitReason ?: return null
    return when (reason) {
        RecruitingActionBlockReason.WindowClosed -> stringResource(R.string.recruiting_window_closed)
        RecruitingActionBlockReason.AlreadyUsedThisSeason -> stringResource(R.string.recruiting_used_this_season)
        RecruitingActionBlockReason.ReservationActiveThisWeek ->
            stringResource(R.string.recruiting_reservation_active)
        RecruitingActionBlockReason.InsufficientPoints -> null
    }
}

@Composable
private fun SignLikelihoodChip(badge: SignLikelihoodBadge) {
    val label = when (badge) {
        SignLikelihoodBadge.Likely -> stringResource(R.string.recruiting_badge_likely)
        SignLikelihoodBadge.Possible -> stringResource(R.string.recruiting_badge_possible)
        SignLikelihoodBadge.Reach -> stringResource(R.string.recruiting_badge_reach)
        SignLikelihoodBadge.LongShot -> stringResource(R.string.recruiting_badge_long_shot)
    }
    val colors = when (badge) {
        SignLikelihoodBadge.Likely -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
        SignLikelihoodBadge.Possible -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
        SignLikelihoodBadge.Reach -> AssistChipDefaults.assistChipColors()
        SignLikelihoodBadge.LongShot -> AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
    AssistChip(onClick = {}, label = { Text(label) }, colors = colors)
}

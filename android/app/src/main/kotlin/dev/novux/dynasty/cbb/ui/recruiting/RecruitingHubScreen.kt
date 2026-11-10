package dev.novux.dynasty.cbb.ui.recruiting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository

@Composable
fun RecruitingHubScreen(
    loaded: LoadedDynasty,
    recruitingEconomyRepository: RecruitingEconomyRepository,
    onBack: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(RecruitingHubDefaults.tabs.indexOf(RecruitingHubDefaults.defaultTab)) }
    var ledger by remember { mutableStateOf<RecruitingCycleLedger?>(null) }

    LaunchedEffect(loaded.dynastyId) {
        ledger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
    }

    val recommendedConfig = remember(loaded.balanceRuleset) {
        BalanceRecruitingRecommendedParser.parse(loaded.balanceRuleset.recruiting)
    }
    val schoolContext = remember(loaded) {
        SchoolRecruitingContext(
            programPrestige = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
            schoolHomeState = "OH",
            pipelineStates = setOf("KY", "IN", "MI", "PA", "WV"),
            nilPoolAnnualUsd = 900_000,
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
                RecruitingHubTab.Recommended -> RecommendedTabBody(recommendedRows)
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

@Composable
private fun RecommendedTabBody(rows: List<RecommendedProspectRow>) {
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
            RecommendedProspectRow(row)
        }
    }
}

@Composable
private fun RecommendedProspectRow(row: RecommendedProspectRow) {
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
        }
        SignLikelihoodChip(row.badge)
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

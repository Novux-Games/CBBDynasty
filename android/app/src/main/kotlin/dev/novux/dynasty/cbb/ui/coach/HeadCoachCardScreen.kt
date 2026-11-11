package dev.novux.dynasty.cbb.ui.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachRatings
import dev.novux.dynasty.cbb.core.coach.contract.HeadCoachContractEngine
import dev.novux.dynasty.cbb.core.coach.security.CoachSecuritySnapshot
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillBranch
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointState
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillTreeEngine
import dev.novux.dynasty.cbb.ui.portrait.CoachPortraitSlot
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadCoachCardScreen(
    card: HeadCoachCard,
    userCoachSecurity: CoachSecuritySnapshot? = null,
    skillPointBank: Int? = null,
    skillState: CoachSkillPointState? = null,
    displayRatings: HeadCoachRatings = card.ratings,
    programPrestige: Int = 50,
    seasonIndex: Int = 1,
    onSpendSkill: (CoachSkillBranch) -> Unit = {},
    onAutoSpendChanged: (Boolean) -> Unit = {},
    onOpenStaffTab: () -> Unit = {},
    selectedTab: CoachCardTab = CoachCardTab.Overview,
    onSelectedTabChange: (CoachCardTab) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val tabs = CoachCardTab.HEAD_COACH_ORDER
    val contractDetail = remember(card, userCoachSecurity, programPrestige) {
        HeadCoachContractEngine.derive(card, userCoachSecurity, programPrestige)
    }
    var showResignConfirm by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        CoachCardIdentityHeader(card = card)
        PrimaryTabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
            tabs.forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { onSelectedTabChange(tab) },
                    text = { Text(coachTabLabel(tab)) },
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (selectedTab) {
                CoachCardTab.Overview -> HeadCoachOverviewTab(
                    card = card,
                    userCoachSecurity = userCoachSecurity,
                    skillPointBank = skillPointBank,
                    skillState = skillState,
                    displayRatings = displayRatings,
                    onSpendSkill = onSpendSkill,
                    onAutoSpendChanged = onAutoSpendChanged,
                )
                CoachCardTab.Contract -> HeadCoachContractTab(
                    card = card,
                    contractDetail = contractDetail,
                    userCoachSecurity = userCoachSecurity,
                    onViewAdTrustOnStaff = onOpenStaffTab,
                    onResignTap = { showResignConfirm = true },
                )
                CoachCardTab.History -> HeadCoachHistoryTab(card = card, seasonIndex = seasonIndex)
                CoachCardTab.HallOfFame -> CoachCardPlaceholderTab(
                    title = stringResource(R.string.coach_tab_hof),
                    body = stringResource(R.string.coach_tab_hof_body),
                )
                CoachCardTab.Draft -> CoachCardPlaceholderTab(
                    title = stringResource(R.string.coach_tab_draft),
                    body = stringResource(R.string.coach_tab_draft_body),
                )
            }
        }
    }

    if (showResignConfirm) {
        AlertDialog(
            onDismissRequest = { showResignConfirm = false },
            title = { Text(stringResource(R.string.coach_contract_resign_title)) },
            text = { Text(stringResource(R.string.coach_contract_resign_body)) },
            confirmButton = {
                TextButton(onClick = { showResignConfirm = false }) {
                    Text(stringResource(R.string.coach_contract_resign_ack))
                }
            },
        )
    }
}

@Composable
private fun CoachCardIdentityHeader(card: HeadCoachCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoachPortraitSlot(
            displayName = card.displayName,
            avatarPresetId = card.avatarPresetId,
        )
        Column {
            Text(card.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(card.schoolDisplayName, style = MaterialTheme.typography.titleMedium)
            Text(
                stringResource(R.string.coach_card_record, card.seasonRecordLabel),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun coachTabLabel(tab: CoachCardTab): String = when (tab) {
    CoachCardTab.Overview -> stringResource(R.string.coach_tab_overview)
    CoachCardTab.Contract -> stringResource(R.string.coach_tab_contract)
    CoachCardTab.History -> stringResource(R.string.coach_tab_history)
    CoachCardTab.HallOfFame -> stringResource(R.string.coach_tab_hof)
    CoachCardTab.Draft -> stringResource(R.string.coach_tab_draft)
}

@Composable
private fun HeadCoachOverviewTab(
    card: HeadCoachCard,
    userCoachSecurity: CoachSecuritySnapshot?,
    skillPointBank: Int?,
    skillState: CoachSkillPointState?,
    displayRatings: HeadCoachRatings,
    onSpendSkill: (CoachSkillBranch) -> Unit,
    onAutoSpendChanged: (Boolean) -> Unit,
) {
    Text(
        stringResource(R.string.coach_card_role_head),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
    )
    userCoachSecurity?.let { JobSecurityMeter(it) }
    skillPointBank?.takeIf { it > 0 }?.let { bank ->
        Text(
            stringResource(R.string.coach_skill_point_bank, bank),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
    RatingCard(
        title = stringResource(R.string.coach_card_ratings_headline),
        lines = listOf(
            stringResource(R.string.coach_rating_offensive, displayRatings.offensive),
            stringResource(R.string.coach_rating_defensive, displayRatings.defensive),
            stringResource(R.string.coach_rating_training, displayRatings.training),
            stringResource(R.string.coach_rating_recruiting, displayRatings.recruiting),
        ),
    )
    skillState?.let { skills ->
        SkillTreePanel(
            state = skills,
            bank = skillPointBank ?: skills.unspentBank,
            onSpendSkill = onSpendSkill,
            onAutoSpendChanged = onAutoSpendChanged,
        )
    }
    RatingCard(
        title = stringResource(R.string.coach_card_strategy),
        lines = listOf(
            stringResource(R.string.coach_scheme_offense, card.offensiveSchemeLabel),
            stringResource(R.string.coach_scheme_defense, card.defensiveSchemeLabel),
        ),
    )
    Text(
        stringResource(
            R.string.coach_contract_summary,
            card.contractLengthYears,
            card.contractYearsLeft,
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
    card.almaMaterLabel?.let {
        Text(stringResource(R.string.coach_alma_mater, it), style = MaterialTheme.typography.bodyMedium)
    }
    Text(
        stringResource(R.string.coach_card_age, card.age),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun HeadCoachContractTab(
    card: HeadCoachCard,
    contractDetail: dev.novux.dynasty.cbb.core.coach.contract.HeadCoachContractDetail,
    userCoachSecurity: CoachSecuritySnapshot?,
    onViewAdTrustOnStaff: () -> Unit,
    onResignTap: () -> Unit,
) {
    val money = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    userCoachSecurity?.let { JobSecurityMeter(it) }
    contractDetail.firingRiskCopy?.let {
        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
    }
    if (contractDetail.extensionLikely) {
        Text(
            stringResource(R.string.coach_contract_extension_likely),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource(R.string.coach_contract_terms_title), fontWeight = FontWeight.SemiBold)
            Text(stringResource(R.string.coach_contract_salary, money.format(contractDetail.annualSalaryUsd)))
            Text(stringResource(R.string.coach_contract_buyout, money.format(contractDetail.buyoutUsd)))
            Text(
                stringResource(
                    R.string.coach_contract_length_detail,
                    card.contractLengthYears,
                    card.contractYearsLeft,
                ),
            )
        }
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.coach_contract_goals_title), fontWeight = FontWeight.SemiBold)
            contractDetail.performanceGoals.forEach { goal ->
                Text("• $goal", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    userCoachSecurity?.let {
        AdTrustMeter(it)
        OutlinedButton(onClick = onViewAdTrustOnStaff, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.coach_contract_view_staff_ad_trust))
        }
    }
    OutlinedButton(onClick = onResignTap, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.coach_contract_resign_cta))
    }
}

@Composable
private fun HeadCoachHistoryTab(card: HeadCoachCard, seasonIndex: Int) {
    Text(stringResource(R.string.coach_tab_history), style = MaterialTheme.typography.titleMedium)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.coach_history_season_row, seasonIndex, card.seasonRecordLabel),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
    Text(
        stringResource(R.string.coach_history_accumulates),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun CoachCardPlaceholderTab(title: String, body: String) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    Text(body, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun SkillTreePanel(
    state: CoachSkillPointState,
    bank: Int,
    onSpendSkill: (CoachSkillBranch) -> Unit,
    onAutoSpendChanged: (Boolean) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                stringResource(R.string.coach_skill_tree_title),
                fontWeight = FontWeight.SemiBold,
            )
            CoachSkillBranch.entries.forEach { branch ->
                val level = CoachSkillTreeEngine.level(state, branch)
                val canSpend = bank >= CoachSkillTreeEngine.POINTS_PER_LEVEL &&
                    level < CoachSkillTreeEngine.MAX_LEVEL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        stringResource(
                            R.string.coach_skill_branch_level,
                            branch.label,
                            level,
                            CoachSkillTreeEngine.MAX_LEVEL,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    OutlinedButton(onClick = { onSpendSkill(branch) }, enabled = canSpend) {
                        Text(stringResource(R.string.coach_skill_spend_point))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(R.string.coach_skill_auto_spend), style = MaterialTheme.typography.bodyMedium)
                Switch(checked = state.autoSpendEnabled, onCheckedChange = onAutoSpendChanged)
            }
        }
    }
}

@Composable
private fun RatingCard(title: String, lines: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            lines.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

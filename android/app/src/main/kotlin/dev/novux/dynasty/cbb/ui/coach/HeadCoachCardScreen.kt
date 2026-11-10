package dev.novux.dynasty.cbb.ui.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachRatings
import dev.novux.dynasty.cbb.core.coach.security.CoachSecuritySnapshot
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillBranch
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointState
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillTreeEngine

@Composable
fun HeadCoachCardScreen(
    card: HeadCoachCard,
    userCoachSecurity: CoachSecuritySnapshot? = null,
    skillPointBank: Int? = null,
    skillState: CoachSkillPointState? = null,
    displayRatings: HeadCoachRatings = card.ratings,
    onSpendSkill: (CoachSkillBranch) -> Unit = {},
    onAutoSpendChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(card.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            stringResource(R.string.coach_card_role_head),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(card.schoolDisplayName, style = MaterialTheme.typography.titleLarge)
        Text(
            stringResource(R.string.coach_card_record, card.seasonRecordLabel),
            style = MaterialTheme.typography.bodyLarge,
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
}

@Composable
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
                        stringResource(R.string.coach_skill_branch_level, branch.label, level, CoachSkillTreeEngine.MAX_LEVEL),
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

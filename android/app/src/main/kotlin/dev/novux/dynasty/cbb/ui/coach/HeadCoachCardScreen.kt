package dev.novux.dynasty.cbb.ui.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard

@Composable
fun HeadCoachCardScreen(
    card: HeadCoachCard,
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
        RatingCard(
            title = stringResource(R.string.coach_card_ratings_headline),
            lines = listOf(
                stringResource(R.string.coach_rating_offensive, card.ratings.offensive),
                stringResource(R.string.coach_rating_defensive, card.ratings.defensive),
                stringResource(R.string.coach_rating_training, card.ratings.training),
                stringResource(R.string.coach_rating_recruiting, card.ratings.recruiting),
            ),
        )
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
private fun RatingCard(title: String, lines: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            lines.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

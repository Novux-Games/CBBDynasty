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
import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachCard

@Composable
fun AssistantCoachCardScreen(
    card: AssistantCoachCard,
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
        Text(card.role.label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(card.schoolDisplayName, style = MaterialTheme.typography.titleLarge)
        Text(
            stringResource(R.string.coach_card_record, card.seasonRecordLabel),
            style = MaterialTheme.typography.bodyLarge,
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.coach_card_ratings_assistant), fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.coach_rating_recruiting, card.ratings.recruiting))
                Text(stringResource(R.string.coach_rating_development, card.ratings.development))
                Text(stringResource(R.string.coach_rating_scouting, card.ratings.scouting))
            }
        }
        Text(
            stringResource(
                R.string.coach_assistant_tenure,
                card.yearsInRole,
                card.yearsAtSchool,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            stringResource(
                R.string.coach_contract_summary,
                card.contractLengthYears,
                card.contractYearsLeft,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

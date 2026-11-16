package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.save.NewGameContractNilPreviewPolicy
import dev.novux.dynasty.cbb.core.save.NewGameDraft
import dev.novux.dynasty.cbb.core.save.SignContractRecapPolicy
import dev.novux.dynasty.cbb.core.save.NewGameSchoolSummaryPreview
import dev.novux.dynasty.cbb.core.save.NewGameSignRecapPolicy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import java.text.NumberFormat
import java.util.Locale

/** Sign-contract step including structured recap ([MEGA-COACH-027]). */
@Composable
fun SignContractStepContent(
    draft: NewGameDraft,
    schoolDisplayName: String,
    contractNilPreview: NewGameSchoolSummaryPreview?,
    signError: SignContractError?,
    signingInProgress: Boolean,
    onSign: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        @Suppress("unused")
        val recapMega = NewGameSignRecapPolicy.MEGA_ID
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.new_sign_recap_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                NewGameSignRecapPolicy.recapLines(
                    archetype = draft.archetype,
                    offensive = draft.offensiveScheme,
                    defensive = draft.defensiveScheme,
                    schoolDisplayName = schoolDisplayName,
                ).forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.new_sign_school_label, schoolDisplayName),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(R.string.new_sign_coach_label, draft.coach.displayName()),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
        )
        @Suppress("unused")
        val qaRecap = SignContractRecapPolicy.MEGA_ID
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.new_sign_recap_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.new_sign_recap_archetype, draft.archetype.label),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(
                    text = stringResource(
                        R.string.new_sign_recap_schemes,
                        draft.offensiveScheme.label,
                        draft.defensiveScheme.label,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(R.string.new_sign_recap_school, schoolDisplayName),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        contractNilPreview?.let { preview ->
            val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }
            val nilUsd = NewGameContractNilPreviewPolicy.annualNilUsd(preview)
            Text(
                text = stringResource(R.string.new_sign_nil_preview, currency.format(nilUsd)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
        Text(
            text = draft.coach.displayName(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 32.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        )
        signError?.let {
            Text(
                text = signErrorMessage(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        Button(
            onClick = onSign,
            enabled = !signingInProgress,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.new_sign_cta))
        }
    }
}

enum class SignContractError {
    IncompleteWizard,
    SlotUnavailable,
}

@Composable
private fun signErrorMessage(error: SignContractError): String = when (error) {
    SignContractError.IncompleteWizard -> stringResource(R.string.new_sign_err_incomplete)
    SignContractError.SlotUnavailable -> stringResource(R.string.new_no_empty_slot)
}

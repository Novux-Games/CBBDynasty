package dev.novux.dynasty.cbb.ui.recruiting

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.balance.BalanceNilRules
import dev.novux.dynasty.cbb.core.nil.NilAssignCapLinePolicy
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RecruitNilAssignCapLine(
    nilAskUsd: Int,
    nilRules: BalanceNilRules,
    modifier: Modifier = Modifier,
) {
    @Suppress("unused")
    val qaId = NilAssignCapLinePolicy.MEGA_ID
    val currency = NumberFormat.getCurrencyInstance(Locale.US)
    val maxOffer = NilAssignCapLinePolicy.maxOfferUsd(nilAskUsd, nilRules)
    Text(
        text = stringResource(
            R.string.recruiting_nil_max_offer_line,
            currency.format(maxOffer),
            nilRules.maxOfferMultiplierOfAsk,
        ),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}
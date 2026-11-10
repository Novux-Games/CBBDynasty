package dev.novux.dynasty.cbb.ui.coach

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.card.CoachCardPolicy
import dev.novux.dynasty.cbb.core.coach.security.CoachSecuritySnapshot
import dev.novux.dynasty.cbb.core.coach.staff.SchoolStaffRoster
import dev.novux.dynasty.cbb.core.coach.staff.StaffKind

@Composable
fun SchoolStaffTabPanel(
    roster: SchoolStaffRoster,
    adTrust: CoachSecuritySnapshot? = null,
    onHeadCoachClick: () -> Unit,
    onAssistantClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        adTrust?.let { AdTrustMeter(it) }
        Text(
            stringResource(R.string.staff_tab_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            stringResource(R.string.staff_tab_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        StaffRow(
            title = roster.headCoach.displayName,
            subtitle = stringResource(R.string.coach_card_role_head),
            meta = roster.headCoach.seasonRecordLabel,
            clickable = true,
            onClick = onHeadCoachClick,
        )
        Text(
            stringResource(R.string.staff_assistants_header),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
        )
        roster.assistants.forEach { assistant ->
            StaffRow(
                title = assistant.displayName,
                subtitle = assistant.role.label,
                meta = stringResource(
                    R.string.staff_rating_chip,
                    assistant.ratings.recruiting,
                ),
                clickable = CoachCardPolicy.eligibleForCoachCard(StaffKind.CoachTrack),
                onClick = { onAssistantClick(assistant.staffId) },
            )
        }
        Text(
            stringResource(R.string.staff_support_header),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
        )
        roster.supportStaff.forEach { member ->
            StaffRow(
                title = member.displayName,
                subtitle = member.role.label,
                meta = stringResource(R.string.staff_rating_chip, member.contributionRating),
                clickable = false,
                onClick = {},
            )
        }
        Text(
            stringResource(R.string.staff_support_no_card),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StaffRow(
    title: String,
    subtitle: String,
    meta: String,
    clickable: Boolean,
    onClick: () -> Unit,
) {
    val colors = if (clickable) {
        CardDefaults.cardColors()
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (clickable) Modifier.clickable(onClick = onClick) else Modifier),
        colors = colors,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
            Text(meta, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

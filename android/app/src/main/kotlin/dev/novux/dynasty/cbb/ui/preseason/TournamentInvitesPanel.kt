package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInviteResolution
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInvitesState
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.preseason.TournamentInvitesRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import kotlinx.coroutines.launch

@Composable
fun TournamentInvitesPanel(
    dynastyId: String,
    seasonIndex: Int,
    invitesRepository: TournamentInvitesRepository,
    progressRepository: PreseasonProgressRepository,
    onResolved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var state by remember { mutableStateOf<TournamentInvitesState?>(null) }
    val scope = rememberCoroutineScope()
    val prestige = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE

    suspend fun refresh() {
        state = invitesRepository.loadState(dynastyId, seasonIndex, prestige)
    }

    LaunchedEffect(dynastyId, seasonIndex) {
        refresh()
    }

    val inviteState = state ?: return
    val resolved = inviteState.isResolved

    suspend fun finishTask() {
        progressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.TournamentInvites)
        onResolved()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.tournament_invites_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (resolved) {
            BudgetSummary(inviteState)
            Text(
                text = resolvedLabel(inviteState.resolution),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else if (inviteState.offers.isEmpty()) {
            Text(
                text = stringResource(R.string.tournament_invites_none),
                style = MaterialTheme.typography.bodyLarge,
            )
            OutlinedButton(
                onClick = {
                    scope.launch {
                        invitesRepository.acknowledgeNoInvites(dynastyId, seasonIndex)
                        refresh()
                        finishTask()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.tournament_invites_acknowledge))
            }
        } else {
            Text(
                text = stringResource(R.string.tournament_invites_pick_one),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            inviteState.offers.forEach { event ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(event.displayName, fontWeight = FontWeight.SemiBold)
                        Text(
                            stringResource(R.string.tournament_invites_event_games, event.rsGameSpots),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    invitesRepository.acceptInvite(dynastyId, seasonIndex, event.templateId)
                                    refresh()
                                    finishTask()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.tournament_invites_accept))
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = {
                    scope.launch {
                        invitesRepository.declineAll(dynastyId, seasonIndex)
                        refresh()
                        finishTask()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.tournament_invites_decline_all))
            }
        }
    }
}

@Composable
private fun BudgetSummary(state: TournamentInvitesState) {
    val budget = state.slotBudget
    Text(
        text = stringResource(
            R.string.tournament_invites_budget,
            budget.totalRsCap,
            budget.eventRsGames,
            budget.builderSlots,
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun resolvedLabel(resolution: TournamentInviteResolution): String = when (resolution) {
    is TournamentInviteResolution.Accepted -> stringResource(
        R.string.tournament_invites_accepted,
        resolution.event.displayName,
    )
    TournamentInviteResolution.DeclinedAll -> stringResource(R.string.tournament_invites_declined_all)
    TournamentInviteResolution.NoInvitesAcknowledged -> stringResource(R.string.tournament_invites_none_ack)
    TournamentInviteResolution.Unresolved -> ""
}

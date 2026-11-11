package dev.novux.dynasty.cbb.ui.postgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.novux.dynasty.cbb.core.balance.BalanceMedicalRedshirtRules
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.injuries.MedicalRedshirtPolicy
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/**
 * Shows postgame injury bottom sheet + medical redshirt confirmation when [events] is non-empty.
 */
@Composable
fun PostgameInjuryFlow(
    events: List<GameSimInjuryEvent>,
    players: List<RosterPlayer>,
    rsWeekIndex: Int,
    medicalRules: BalanceMedicalRedshirtRules,
    onDismiss: () -> Unit,
    onApplyMedicalRedshirt: (String) -> Unit,
) {
  if (events.isEmpty()) return
    var confirmPlayer by remember(events) { mutableStateOf<RosterPlayer?>(null) }
    PostgameInjurySheet(
        events = events,
        players = players,
        medicalEligible = { playerId ->
            val player = players.firstOrNull { it.playerId == playerId } ?: return@PostgameInjurySheet false
            MedicalRedshirtPolicy.evaluate(player, rsWeekIndex, medicalRules).eligible
        },
        onDismiss = onDismiss,
        onApplyMedicalRedshirt = { playerId ->
            confirmPlayer = players.firstOrNull { it.playerId == playerId }
        },
    )
    confirmPlayer?.let { player ->
        MedicalRedshirtConfirmDialog(
            player = player,
            medicalRules = medicalRules,
            onConfirm = {
                onApplyMedicalRedshirt(player.playerId)
                confirmPlayer = null
            },
            onCancel = { confirmPlayer = null },
        )
    }
}

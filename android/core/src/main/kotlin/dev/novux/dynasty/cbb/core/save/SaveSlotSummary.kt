package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.balance.BalanceRuleset
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState

/**
 * UI / API summary for one save slot — [docs/ADR-001-dynasty-save-backend.md].
 */
data class SaveSlotSummary(
    val slotIndex: Int,
    val isEmpty: Boolean,
    val dynastyId: String? = null,
    val schoolDisplayName: String? = null,
    val coachDisplayName: String? = null,
    val seasonLabel: String? = null,
    val wins: Int = 0,
    val losses: Int = 0,
    val lastPlayedAtEpochMillis: Long? = null,
    val packId: String? = null,
)

data class LoadedDynasty(
    val slotIndex: Int,
    val dynastyId: String,
    val summary: SaveSlotSummary,
    val metadata: DynastyMetadata,
    val weekCheckpoint: WeekCheckpointState,
    val balanceRuleset: BalanceRuleset,
    val simTuning: SimTuning,
)

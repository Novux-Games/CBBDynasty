package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * Per-dynasty simulation header — persisted in Room `dynasty_metadata`.
 * See [planning/game-design.md](../planning/game-design.md) save model and Phase 2.3 checklist.
 */
data class DynastyMetadata(
    val dynastyId: String,
    val seasonIndex: Int,
    val phase: CalendarPhase,
    val packId: String,
    val dynastySchemaVersion: Int = DynastySchema.CURRENT_VERSION,
    val balanceRulesetId: String = DynastySchema.DEFAULT_BALANCE_RULESET_ID,
    val balanceSchemaVersion: Int = DynastySchema.DEFAULT_BALANCE_SCHEMA_VERSION,
    val simTuningId: String = DynastySchema.DEFAULT_SIM_TUNING_ID,
    val simSchemaVersion: Int = DynastySchema.DEFAULT_SIM_SCHEMA_VERSION,
) {
    init {
        require(dynastyId.isNotBlank()) { "dynastyId required" }
        require(seasonIndex >= 1) { "seasonIndex must be >= 1" }
        require(packId.isNotBlank()) { "packId required" }
        require(dynastySchemaVersion >= 1) { "dynastySchemaVersion must be >= 1" }
        require(balanceRulesetId.isNotBlank()) { "balanceRulesetId required" }
        require(balanceSchemaVersion >= 1) { "balanceSchemaVersion must be >= 1" }
        require(simTuningId.isNotBlank()) { "simTuningId required" }
        require(simSchemaVersion >= 1) { "simSchemaVersion must be >= 1" }
    }
}

/** Initial metadata for a newly signed dynasty (pre–game-frame). */
object DynastyBootstrap {
    fun newDynasty(
        dynastyId: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
        balanceRulesetId: String = DynastySchema.DEFAULT_BALANCE_RULESET_ID,
        balanceSchemaVersion: Int = DynastySchema.DEFAULT_BALANCE_SCHEMA_VERSION,
        simTuningId: String = DynastySchema.DEFAULT_SIM_TUNING_ID,
        simSchemaVersion: Int = DynastySchema.DEFAULT_SIM_SCHEMA_VERSION,
    ): DynastyMetadata =
        DynastyMetadata(
            dynastyId = dynastyId,
            seasonIndex = 1,
            phase = CalendarPhase.Offseason,
            packId = packId,
            dynastySchemaVersion = DynastySchema.CURRENT_VERSION,
            balanceRulesetId = balanceRulesetId,
            balanceSchemaVersion = balanceSchemaVersion,
            simTuningId = simTuningId,
            simSchemaVersion = simSchemaVersion,
        )
}

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
) {
    init {
        require(dynastyId.isNotBlank()) { "dynastyId required" }
        require(seasonIndex >= 1) { "seasonIndex must be >= 1" }
        require(packId.isNotBlank()) { "packId required" }
        require(dynastySchemaVersion >= 1) { "dynastySchemaVersion must be >= 1" }
    }
}

/** Initial metadata for a newly signed dynasty (pre–game-frame). */
object DynastyBootstrap {
    fun newDynasty(
        dynastyId: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
    ): DynastyMetadata =
        DynastyMetadata(
            dynastyId = dynastyId,
            seasonIndex = 1,
            phase = CalendarPhase.Offseason,
            packId = packId,
            dynastySchemaVersion = DynastySchema.CURRENT_VERSION,
        )
}

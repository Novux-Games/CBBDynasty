package dev.novux.dynasty.cbb.database.dynasty

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

@Entity(tableName = "dynasty_metadata")
data class DynastyMetadataEntity(
    @PrimaryKey val dynastyId: String,
    val seasonIndex: Int,
    val phase: CalendarPhase,
    val packId: String,
    val dynastySchemaVersion: Int,
    val balanceRulesetId: String,
    val balanceSchemaVersion: Int,
    val simTuningId: String,
    val simSchemaVersion: Int,
)

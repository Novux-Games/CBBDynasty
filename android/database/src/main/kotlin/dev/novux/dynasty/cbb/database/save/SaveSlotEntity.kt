package dev.novux.dynasty.cbb.database.save

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save_slots")
data class SaveSlotEntity(
    @PrimaryKey val slotIndex: Int,
    val dynastyId: String?,
    val lastPlayedAtEpochMillis: Long?,
    val schoolDisplayName: String?,
    val coachDisplayName: String?,
    val seasonLabel: String?,
    val wins: Int,
    val losses: Int,
    val packId: String?,
)

package dev.novux.dynasty.cbb.database.save

import dev.novux.dynasty.cbb.core.save.SaveSlotSummary

fun SaveSlotEntity.toSummary(): SaveSlotSummary {
    val occupied = !dynastyId.isNullOrBlank()
    return SaveSlotSummary(
        slotIndex = slotIndex,
        isEmpty = !occupied,
        dynastyId = dynastyId,
        schoolDisplayName = schoolDisplayName,
        coachDisplayName = coachDisplayName,
        seasonLabel = seasonLabel,
        wins = wins,
        losses = losses,
        lastPlayedAtEpochMillis = lastPlayedAtEpochMillis,
        packId = packId,
    )
}

fun emptySlotEntity(slotIndex: Int): SaveSlotEntity =
    SaveSlotEntity(
        slotIndex = slotIndex,
        dynastyId = null,
        lastPlayedAtEpochMillis = null,
        schoolDisplayName = null,
        coachDisplayName = null,
        seasonLabel = null,
        wins = 0,
        losses = 0,
        packId = null,
    )

fun ensureSlotRows(existing: List<SaveSlotEntity>): List<SaveSlotEntity> {
    val byIndex = existing.associateBy { it.slotIndex }
    return SaveSlots.INDICES.map { index ->
        byIndex[index] ?: emptySlotEntity(index)
    }
}

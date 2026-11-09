package dev.novux.dynasty.cbb.data.save

import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.save.SaveSlotSummary
import dev.novux.dynasty.cbb.core.save.SaveSlots
import dev.novux.dynasty.cbb.database.save.SaveSlotDao
import dev.novux.dynasty.cbb.database.save.SaveSlotEntity
import dev.novux.dynasty.cbb.database.save.emptySlotEntity
import dev.novux.dynasty.cbb.database.save.ensureSlotRows
import dev.novux.dynasty.cbb.database.save.toSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveSlotRepository(
    private val dao: SaveSlotDao,
) {
    private val mutex = Any()

    suspend fun ensureInitialized() {
        synchronized(mutex) {
            val existing = dao.listAll()
            if (existing.size < SaveSlots.COUNT) {
                val merged = ensureSlotRows(existing)
                dao.upsertAll(merged)
            }
        }
    }

    fun observeSlots(): Flow<List<SaveSlotSummary>> =
        dao.observeAll().map { rows ->
            ensureSlotRows(rows).map { it.toSummary() }
        }

    fun observeContinueSlot(): Flow<SaveSlotSummary?> =
        observeSlots().map { slots ->
            slots
                .filter { !it.isEmpty }
                .maxByOrNull { it.lastPlayedAtEpochMillis ?: 0L }
        }

    suspend fun listSlots(): List<SaveSlotSummary> {
        ensureInitialized()
        return ensureSlotRows(dao.listAll()).map { it.toSummary() }
    }

    suspend fun continueSlot(): SaveSlotSummary? {
        ensureInitialized()
        return dao.getMostRecentlyPlayed()?.toSummary()
    }

    suspend fun loadSlot(slotIndex: Int): LoadedDynasty? {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val entity = dao.getByIndex(slotIndex) ?: return null
        val summary = entity.toSummary()
        if (summary.isEmpty || summary.dynastyId == null) return null
        val now = System.currentTimeMillis()
        dao.update(entity.copy(lastPlayedAtEpochMillis = now))
        return LoadedDynasty(
            slotIndex = slotIndex,
            dynastyId = summary.dynastyId,
            summary = summary.copy(lastPlayedAtEpochMillis = now),
        )
    }

    suspend fun deleteSlot(slotIndex: Int) {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        dao.clearSlot(slotIndex)
    }

    /** Writes slot summary metadata when a dynasty is created (New Game flow, Phase 2.3+). */
    suspend fun writeOccupiedSlot(
        slotIndex: Int,
        dynastyId: String,
        schoolDisplayName: String,
        coachDisplayName: String,
        seasonLabel: String,
        packId: String = "default",
    ) {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val now = System.currentTimeMillis()
        val entity = SaveSlotEntity(
            slotIndex = slotIndex,
            dynastyId = dynastyId,
            lastPlayedAtEpochMillis = now,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachDisplayName,
            seasonLabel = seasonLabel,
            wins = 0,
            losses = 0,
            packId = packId,
        )
        dao.upsert(entity)
    }
}

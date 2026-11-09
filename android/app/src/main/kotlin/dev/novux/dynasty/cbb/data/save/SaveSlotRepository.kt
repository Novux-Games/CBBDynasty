package dev.novux.dynasty.cbb.data.save

import androidx.room.withTransaction
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.save.DynastyBootstrap
import dev.novux.dynasty.cbb.core.save.DynastySchema
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.save.SaveSlotSummary
import dev.novux.dynasty.cbb.core.save.SaveSlots
import dev.novux.dynasty.cbb.database.DynastyDatabase
import dev.novux.dynasty.cbb.database.dynasty.toEntity
import dev.novux.dynasty.cbb.database.dynasty.toDomain
import dev.novux.dynasty.cbb.database.save.SaveSlotEntity
import dev.novux.dynasty.cbb.database.save.ensureSlotRows
import dev.novux.dynasty.cbb.database.save.toSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SaveSlotRepository(
    private val database: DynastyDatabase,
) {
    private val slotDao = database.saveSlotDao()
    private val metadataDao = database.dynastyMetadataDao()
    private val mutex = Any()

    suspend fun ensureInitialized() {
        synchronized(mutex) {
            val existing = slotDao.listAll()
            if (existing.size < SaveSlots.COUNT) {
                val merged = ensureSlotRows(existing)
                slotDao.upsertAll(merged)
            }
        }
    }

    fun observeSlots(): Flow<List<SaveSlotSummary>> =
        slotDao.observeAll().map { rows ->
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
        return ensureSlotRows(slotDao.listAll()).map { it.toSummary() }
    }

    suspend fun continueSlot(): SaveSlotSummary? {
        ensureInitialized()
        return slotDao.getMostRecentlyPlayed()?.toSummary()
    }

    suspend fun firstEmptySlotIndex(): Int? {
        ensureInitialized()
        return ensureSlotRows(slotDao.listAll())
            .firstOrNull { it.dynastyId.isNullOrBlank() }
            ?.slotIndex
    }

    suspend fun loadSlot(slotIndex: Int): LoadedDynasty? {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val entity = slotDao.getByIndex(slotIndex) ?: return null
        val summary = entity.toSummary()
        val dynastyId = summary.dynastyId ?: return null
        if (summary.isEmpty) return null
        val metadata = metadataDao.getById(dynastyId)?.toDomain() ?: return null
        val now = System.currentTimeMillis()
        val updatedSummary = summary.copy(lastPlayedAtEpochMillis = now)
        slotDao.update(entity.copy(lastPlayedAtEpochMillis = now))
        return LoadedDynasty(
            slotIndex = slotIndex,
            dynastyId = dynastyId,
            summary = updatedSummary,
            metadata = metadata,
        )
    }

    suspend fun deleteSlot(slotIndex: Int) {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val dynastyId = slotDao.getByIndex(slotIndex)?.dynastyId
        database.withTransaction {
            if (!dynastyId.isNullOrBlank()) {
                metadataDao.deleteById(dynastyId)
            }
            slotDao.clearSlot(slotIndex)
        }
    }

    /**
     * Creates a new dynasty with default metadata (offseason, season 1, default pack).
     * Full new-game wizard will replace placeholder display names.
     */
    suspend fun createEmptyDynasty(
        slotIndex: Int,
        schoolDisplayName: String,
        coachDisplayName: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
        dynastyId: String = UUID.randomUUID().toString(),
    ): LoadedDynasty? {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val existing = slotDao.getByIndex(slotIndex)?.toSummary()
        if (existing != null && !existing.isEmpty) return null

        val metadata = DynastyBootstrap.newDynasty(dynastyId = dynastyId, packId = packId)
        val seasonLabel = SeasonMembershipYear.label(metadata.seasonIndex)
        val now = System.currentTimeMillis()

        database.withTransaction {
            metadataDao.upsert(metadata.toEntity())
            slotDao.upsert(
                SaveSlotEntity(
                    slotIndex = slotIndex,
                    dynastyId = dynastyId,
                    lastPlayedAtEpochMillis = now,
                    schoolDisplayName = schoolDisplayName,
                    coachDisplayName = coachDisplayName,
                    seasonLabel = seasonLabel,
                    wins = 0,
                    losses = 0,
                    packId = packId,
                ),
            )
        }

        val summary = SaveSlotSummary(
            slotIndex = slotIndex,
            isEmpty = false,
            dynastyId = dynastyId,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachDisplayName,
            seasonLabel = seasonLabel,
            wins = 0,
            losses = 0,
            lastPlayedAtEpochMillis = now,
            packId = packId,
        )
        return LoadedDynasty(
            slotIndex = slotIndex,
            dynastyId = dynastyId,
            summary = summary,
            metadata = metadata,
        )
    }

    suspend fun createEmptyDynastyInFirstOpenSlot(
        schoolDisplayName: String,
        coachDisplayName: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
    ): LoadedDynasty? {
        val slotIndex = firstEmptySlotIndex() ?: return null
        return createEmptyDynasty(
            slotIndex = slotIndex,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachDisplayName,
            packId = packId,
        )
    }
}

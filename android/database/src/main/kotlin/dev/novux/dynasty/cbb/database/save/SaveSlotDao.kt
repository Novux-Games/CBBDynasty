package dev.novux.dynasty.cbb.database.save

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveSlotDao {
    @Query("SELECT * FROM save_slots ORDER BY slotIndex ASC")
    fun observeAll(): Flow<List<SaveSlotEntity>>

    @Query("SELECT * FROM save_slots ORDER BY slotIndex ASC")
    suspend fun listAll(): List<SaveSlotEntity>

    @Query("SELECT * FROM save_slots WHERE slotIndex = :slotIndex LIMIT 1")
    suspend fun getByIndex(slotIndex: Int): SaveSlotEntity?

    @Query(
        """
        SELECT * FROM save_slots
        WHERE dynastyId IS NOT NULL
        ORDER BY lastPlayedAtEpochMillis DESC
        LIMIT 1
        """,
    )
    suspend fun getMostRecentlyPlayed(): SaveSlotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(slots: List<SaveSlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(slot: SaveSlotEntity)

    @Update
    suspend fun update(slot: SaveSlotEntity)

    @Query(
        """
        UPDATE save_slots SET
            dynastyId = NULL,
            lastPlayedAtEpochMillis = NULL,
            schoolDisplayName = NULL,
            coachDisplayName = NULL,
            seasonLabel = NULL,
            wins = 0,
            losses = 0,
            packId = NULL
        WHERE slotIndex = :slotIndex
        """,
    )
    suspend fun clearSlot(slotIndex: Int)
}

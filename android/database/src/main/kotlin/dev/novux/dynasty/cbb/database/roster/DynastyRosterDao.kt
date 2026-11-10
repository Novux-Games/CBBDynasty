package dev.novux.dynasty.cbb.database.roster

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DynastyRosterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<DynastyRosterPlayerEntity>)

    @Query("SELECT * FROM dynasty_roster_player WHERE dynastyId = :dynastyId ORDER BY depthOrder ASC")
    suspend fun listForDynasty(dynastyId: String): List<DynastyRosterPlayerEntity>

    @Query("DELETE FROM dynasty_roster_player WHERE dynastyId = :dynastyId")
    suspend fun deleteForDynasty(dynastyId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(player: DynastyRosterPlayerEntity)

    @Query("DELETE FROM dynasty_roster_player WHERE dynastyId = :dynastyId AND playerId = :playerId")
    suspend fun deletePlayer(dynastyId: String, playerId: String)
}

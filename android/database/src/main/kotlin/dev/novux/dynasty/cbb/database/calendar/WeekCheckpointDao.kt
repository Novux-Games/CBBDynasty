package dev.novux.dynasty.cbb.database.calendar

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DynastyCalendarDao {
    @Query("SELECT * FROM dynasty_calendar WHERE dynastyId = :dynastyId LIMIT 1")
    suspend fun getByDynastyId(dynastyId: String): DynastyCalendarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DynastyCalendarEntity)

    @Query("DELETE FROM dynasty_calendar WHERE dynastyId = :dynastyId")
    suspend fun deleteByDynastyId(dynastyId: String)
}

@Dao
interface ProvisionalGameDao {
    @Query("SELECT * FROM provisional_games WHERE dynastyId = :dynastyId")
    suspend fun listForDynasty(dynastyId: String): List<ProvisionalGameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProvisionalGameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<ProvisionalGameEntity>)

    @Query("DELETE FROM provisional_games WHERE dynastyId = :dynastyId")
    suspend fun deleteAllForDynasty(dynastyId: String)

    @Query("DELETE FROM provisional_games WHERE dynastyId = :dynastyId AND gameId = :gameId")
    suspend fun deleteGame(dynastyId: String, gameId: String)
}

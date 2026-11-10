package dev.novux.dynasty.cbb.database.dynasty

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DynastyCoachDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DynastyCoachEntity)

    @Query("SELECT * FROM dynasty_coach WHERE dynastyId = :dynastyId LIMIT 1")
    suspend fun getByDynastyId(dynastyId: String): DynastyCoachEntity?

    @Query("DELETE FROM dynasty_coach WHERE dynastyId = :dynastyId")
    suspend fun deleteByDynastyId(dynastyId: String)
}

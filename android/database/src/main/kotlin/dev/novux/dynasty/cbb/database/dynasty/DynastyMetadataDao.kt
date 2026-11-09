package dev.novux.dynasty.cbb.database.dynasty

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DynastyMetadataDao {
    @Query("SELECT * FROM dynasty_metadata WHERE dynastyId = :dynastyId LIMIT 1")
    suspend fun getById(dynastyId: String): DynastyMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DynastyMetadataEntity)

    @Query("DELETE FROM dynasty_metadata WHERE dynastyId = :dynastyId")
    suspend fun deleteById(dynastyId: String)
}

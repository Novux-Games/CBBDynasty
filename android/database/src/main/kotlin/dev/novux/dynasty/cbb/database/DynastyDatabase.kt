package dev.novux.dynasty.cbb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.novux.dynasty.cbb.database.save.SaveSlotDao
import dev.novux.dynasty.cbb.database.save.SaveSlotEntity

@Database(
    entities = [SaveSlotEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DynastyDatabase : RoomDatabase() {
    abstract fun saveSlotDao(): SaveSlotDao

    companion object {
        const val DB_NAME = "dynasty.db"

        fun build(context: Context): DynastyDatabase =
            Room.databaseBuilder(context, DynastyDatabase::class.java, DB_NAME).build()
    }
}

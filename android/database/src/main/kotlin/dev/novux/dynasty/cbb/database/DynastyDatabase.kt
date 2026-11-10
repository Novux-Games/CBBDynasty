package dev.novux.dynasty.cbb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.novux.dynasty.cbb.database.calendar.DynastyCalendarDao
import dev.novux.dynasty.cbb.database.calendar.DynastyCalendarEntity
import dev.novux.dynasty.cbb.database.calendar.ProvisionalGameDao
import dev.novux.dynasty.cbb.database.calendar.ProvisionalGameEntity
import dev.novux.dynasty.cbb.database.dynasty.DynastyCoachDao
import dev.novux.dynasty.cbb.database.dynasty.DynastyCoachEntity
import dev.novux.dynasty.cbb.database.dynasty.DynastyMetadataDao
import dev.novux.dynasty.cbb.database.dynasty.DynastyMetadataEntity
import dev.novux.dynasty.cbb.database.roster.DynastyRosterDao
import dev.novux.dynasty.cbb.database.roster.DynastyRosterPlayerEntity
import dev.novux.dynasty.cbb.database.save.SaveSlotDao
import dev.novux.dynasty.cbb.database.save.SaveSlotEntity

@Database(
    entities = [
        SaveSlotEntity::class,
        DynastyMetadataEntity::class,
        DynastyCalendarEntity::class,
        ProvisionalGameEntity::class,
        DynastyCoachEntity::class,
        DynastyRosterPlayerEntity::class,
    ],
    version = 10,
    exportSchema = false,
)
@androidx.room.TypeConverters(Converters::class)
abstract class DynastyDatabase : RoomDatabase() {
    abstract fun saveSlotDao(): SaveSlotDao

    abstract fun dynastyMetadataDao(): DynastyMetadataDao

    abstract fun dynastyCoachDao(): DynastyCoachDao

    abstract fun dynastyRosterDao(): DynastyRosterDao

    abstract fun dynastyCalendarDao(): DynastyCalendarDao

    abstract fun provisionalGameDao(): ProvisionalGameDao

    companion object {
        const val DB_NAME = "dynasty.db"

        fun build(context: Context): DynastyDatabase =
            Room.databaseBuilder(context, DynastyDatabase::class.java, DB_NAME)
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6,
                    MIGRATION_6_7,
                    MIGRATION_7_8,
                    MIGRATION_8_9,
                    MIGRATION_9_10,
                )
                .build()
    }
}

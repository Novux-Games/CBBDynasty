package dev.novux.dynasty.cbb.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dynasty_metadata (
                dynastyId TEXT NOT NULL PRIMARY KEY,
                seasonIndex INTEGER NOT NULL,
                phase TEXT NOT NULL,
                packId TEXT NOT NULL,
                dynastySchemaVersion INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}

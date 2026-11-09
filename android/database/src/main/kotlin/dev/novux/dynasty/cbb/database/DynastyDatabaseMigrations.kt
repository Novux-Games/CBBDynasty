package dev.novux.dynasty.cbb.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dynasty_calendar (
                dynastyId TEXT NOT NULL PRIMARY KEY,
                seasonIndex INTEGER NOT NULL,
                phase TEXT NOT NULL,
                activeWeekIndex INTEGER NOT NULL,
                committedSeasonIndex INTEGER,
                committedPhase TEXT,
                committedWeekIndex INTEGER
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS provisional_games (
                dynastyId TEXT NOT NULL,
                gameId TEXT NOT NULL,
                homeScore INTEGER NOT NULL,
                awayScore INTEGER NOT NULL,
                simGeneration INTEGER NOT NULL,
                PRIMARY KEY(dynastyId, gameId)
            )
            """.trimIndent(),
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS index_provisional_games_dynastyId ON provisional_games(dynastyId)",
        )
    }
}

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

package dev.novux.dynasty.cbb.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.novux.dynasty.cbb.core.save.DynastySchema

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dynasty_roster_player (
                playerId TEXT NOT NULL PRIMARY KEY,
                dynastyId TEXT NOT NULL,
                positionWire TEXT NOT NULL,
                classYearWire TEXT NOT NULL,
                isStarter INTEGER NOT NULL,
                depthOrder INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_dynasty_roster_player_dynastyId
            ON dynasty_roster_player (dynastyId)
            """.trimIndent(),
        )
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS dynasty_coach (
                dynastyId TEXT NOT NULL PRIMARY KEY,
                userTeamId TEXT NOT NULL,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                nameSuffixWire TEXT,
                age INTEGER NOT NULL,
                almaMaterTeamId TEXT,
                skinToneId TEXT NOT NULL,
                hairColorId TEXT NOT NULL,
                hairStyleId TEXT NOT NULL,
                beardStyleId TEXT NOT NULL,
                avatarPresetId TEXT NOT NULL,
                archetypeWire TEXT NOT NULL,
                offensiveSchemeWire TEXT NOT NULL,
                defensiveSchemeWire TEXT NOT NULL,
                autoUpgrade INTEGER NOT NULL,
                unlimitedCoachAge INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE dynasty_metadata ADD COLUMN balanceRulesetId TEXT NOT NULL
            DEFAULT '${DynastySchema.DEFAULT_BALANCE_RULESET_ID}'
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE dynasty_metadata ADD COLUMN balanceSchemaVersion INTEGER NOT NULL
            DEFAULT ${DynastySchema.DEFAULT_BALANCE_SCHEMA_VERSION}
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE dynasty_metadata ADD COLUMN simTuningId TEXT NOT NULL
            DEFAULT '${DynastySchema.DEFAULT_SIM_TUNING_ID}'
            """.trimIndent(),
        )
        db.execSQL(
            """
            ALTER TABLE dynasty_metadata ADD COLUMN simSchemaVersion INTEGER NOT NULL
            DEFAULT ${DynastySchema.DEFAULT_SIM_SCHEMA_VERSION}
            """.trimIndent(),
        )
    }
}

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

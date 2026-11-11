package dev.novux.dynasty.cbb.database

import org.junit.Assert.assertEquals
import org.junit.Test

class DynastyDatabaseMigrationsTest {
    @Test
    fun migration_3_4_version_span() {
        assertEquals(3, MIGRATION_3_4.startVersion)
        assertEquals(4, MIGRATION_3_4.endVersion)
    }

    @Test
    fun migration_16_17_version_span() {
        assertEquals(16, MIGRATION_16_17.startVersion)
        assertEquals(17, MIGRATION_16_17.endVersion)
    }

    @Test
    fun migration_17_18_version_span() {
        assertEquals(17, MIGRATION_17_18.startVersion)
        assertEquals(18, MIGRATION_17_18.endVersion)
    }

    @Test
    fun migration_18_19_version_span() {
        assertEquals(18, MIGRATION_18_19.startVersion)
        assertEquals(DYNASTY_DB_VERSION, MIGRATION_18_19.endVersion)
    }
}

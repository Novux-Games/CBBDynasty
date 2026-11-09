package dev.novux.dynasty.cbb.database

import org.junit.Assert.assertEquals
import org.junit.Test

class DynastyDatabaseMigrationsTest {
    @Test
    fun migration_3_4_version_span() {
        assertEquals(3, MIGRATION_3_4.startVersion)
        assertEquals(4, MIGRATION_3_4.endVersion)
    }
}

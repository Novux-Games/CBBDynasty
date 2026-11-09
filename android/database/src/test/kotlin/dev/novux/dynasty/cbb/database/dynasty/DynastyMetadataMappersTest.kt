package dev.novux.dynasty.cbb.database.dynasty

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.save.DynastyMetadata
import dev.novux.dynasty.cbb.core.save.DynastySchema
import org.junit.Assert.assertEquals
import org.junit.Test

class DynastyMetadataMappersTest {
    @Test
    fun entityDomain_roundTrip() {
        val original = DynastyMetadata(
            dynastyId = "abc-123",
            seasonIndex = 1,
            phase = CalendarPhase.Offseason,
            packId = DynastySchema.DEFAULT_PACK_ID,
            dynastySchemaVersion = DynastySchema.CURRENT_VERSION,
        )
        val roundTrip = original.toEntity().toDomain()
        assertEquals(original, roundTrip)
    }
}

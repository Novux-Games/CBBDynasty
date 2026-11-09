package dev.novux.dynasty.cbb.core.pack

import dev.novux.dynasty.cbb.core.save.DynastySchema
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class PackDisplayNamesTest {
    @Test
    fun embeddedPack_neverShowsInternalId() {
        val label = PackDisplayNames.forPackId(DynastySchema.DEFAULT_PACK_ID, "2026–27")
        assertEquals("2026–27 League", label)
        assertFalse(label.contains("embedded", ignoreCase = true))
        assertFalse(label.contains("default pack", ignoreCase = true))
    }
}

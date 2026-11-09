package dev.novux.dynasty.cbb.core.save

import org.junit.Assert.assertEquals
import org.junit.Test

class SaveSlotsTest {
    @Test
    fun tenIndexedSlots() {
        assertEquals(10, SaveSlots.COUNT)
        assertEquals(0, SaveSlots.INDICES.first)
        assertEquals(9, SaveSlots.INDICES.last)
    }
}

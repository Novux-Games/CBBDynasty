package dev.novux.dynasty.cbb.core.player.scout

import org.junit.Assert.assertEquals
import org.junit.Test

class ScoutLetterScaleTest {
    @Test
    fun mapsRatingsToLetters() {
        assertEquals("A", ScoutLetterScale.fromRating(95))
        assertEquals("B+", ScoutLetterScale.fromRating(85))
        assertEquals("C", ScoutLetterScale.fromRating(63))
    }

    @Test
    fun fuzzSoftensBand() {
        assertEquals("B", ScoutLetterScale.fromRating(85, fuzzSteps = 1))
    }
}

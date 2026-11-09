package dev.novux.dynasty.cbb.core

import org.junit.Assert.assertEquals
import org.junit.Test

class GameConstantsTest {
    @Test
    fun titleMatchesBranding() {
        assertEquals("Dynasty: College Basketball", GameConstants.GAME_TITLE)
        assertEquals("Novux Games", GameConstants.STUDIO_NAME)
    }
}

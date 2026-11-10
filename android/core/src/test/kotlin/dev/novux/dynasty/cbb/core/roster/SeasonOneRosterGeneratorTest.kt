package dev.novux.dynasty.cbb.core.roster

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonOneRosterGeneratorTest {
    @Test
    fun generate_fifteenPlayers_withFiveStarters() {
        val roster = SeasonOneRosterGenerator.generate("dyn-test")
        assertEquals(15, roster.size)
        assertEquals(5, roster.count { it.isStarter })
        assertEquals(15, roster.maxOf { it.depthOrder })
        assertTrue(roster.map { it.playerId }.toSet().size == 15)
    }
}

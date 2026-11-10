package dev.novux.dynasty.cbb.core.preseason.tournament

import org.junit.Assert.assertTrue
import org.junit.Test

class TournamentInviteGeneratorTest {
    @Test
    fun lowPrestigeGetsNoInvites() {
        assertTrue(TournamentInviteGenerator.invites(25, seed = 1).isEmpty())
    }

    @Test
    fun highPrestigeGetsMultipleInvites() {
        val invites = TournamentInviteGenerator.invites(80, seed = 99)
        assertTrue(invites.size >= 2)
    }
}

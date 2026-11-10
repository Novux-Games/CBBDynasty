package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import org.junit.Assert.assertTrue
import org.junit.Test

class SigningDayHardSignEngineTest {
    @Test
    fun hardSign_requiresRankingsAndPositions() {
        val incomplete = SigningDayProgress(cycleSeasonIndex = 1, rankingsViewed = true)
        try {
            SigningDayHardSignEngine.finalizeClass(incomplete, emptyList())
            error("expected failure")
        } catch (_: IllegalArgumentException) {
        }
        val done = SigningDayHardSignEngine.finalizeClass(
            incomplete.copy(positionsConfirmed = true),
            listOf(
                IncomingSignee(
                    "p1",
                    "Test Player",
                    4,
                    PlayerPosition.SmallForward,
                    "OH",
                    100_000,
                    12,
                    "B+",
                ),
            ),
        )
        assertTrue(done.progress.hardSignComplete)
    }
}

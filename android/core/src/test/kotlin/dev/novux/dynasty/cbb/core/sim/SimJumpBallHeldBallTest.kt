package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.strategy.ResolvedSchemeEffects
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SimJumpBallHeldBallTest {
    private val baseEffects = ResolvedSchemeEffects(
        tripPaceScale = 1.0,
        threePointAttemptRate = 0.38,
        turnoverChance = 0.22,
        makeRateAdjust = 0.0,
        offensiveRatingDelta = 0,
        defensiveRatingDelta = 0,
    )

    @Test
    fun jumpBallTurnoverLabel() {
        assertEquals("held ball | jump ball", PossessionTurnoverFlavorPolicy.logLabel(TurnoverTerminal.JumpBall))
    }

    @Test
    fun deterministicTrigger_isRare() {
        var hits = 0
        repeat(4_000) {
            if (SimJumpBallHeldBallPolicy.shouldTriggerDeterministic(99L, it, "team-a")) hits++
        }
        assertTrue(hits in 0..80)
    }
}
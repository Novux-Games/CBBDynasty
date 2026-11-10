package dev.novux.dynasty.cbb.core.strategy

import org.junit.Assert.assertTrue
import org.junit.Test

class GameplanEffectsResolverTest {
    @Test
    fun heavyUsageBoostsMakeRate() {
        val light = GameplanEffectsResolver.offenseTweaks(GameplanInput(usageWeights = mapOf("a" to 20)), 0.20)
        val heavy = GameplanEffectsResolver.offenseTweaks(GameplanInput(usageWeights = mapOf("a" to 80)), 0.50)
        assertTrue(heavy.makeRateAdjust > light.makeRateAdjust)
    }

    @Test
    fun denyPrimaryAddsDefensivePressure() {
        val off = GameplanEffectsResolver.defenseTweaks(
            GameplanInput(denyPrimaryScorer = true),
            gameClockSeconds = 600,
            defenseScore = 40,
            offenseScore = 42,
        )
        val base = GameplanEffectsResolver.defenseTweaks(
            GameplanInput(),
            gameClockSeconds = 600,
            defenseScore = 40,
            offenseScore = 42,
        )
        assertTrue(off.turnoverAdjust > base.turnoverAdjust)
        assertTrue(off.makeRateAdjust < base.makeRateAdjust)
    }

    @Test
    fun foulEarlyWhenTrailingLate() {
        val tweak = GameplanEffectsResolver.defenseTweaks(
            GameplanInput(lateGameFoulStrategy = LateGameFoulStrategy.FoulEarly),
            gameClockSeconds = 90,
            defenseScore = 55,
            offenseScore = 60,
        )
        assertTrue(tweak.turnoverAdjust > 0.0)
        assertTrue(tweak.makeRateAdjust < 0.0)
    }
}

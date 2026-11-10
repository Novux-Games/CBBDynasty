package dev.novux.dynasty.cbb.core.strategy

import org.junit.Assert.assertTrue
import org.junit.Test

class SchemeMatchupResolverTest {
    @Test
    fun perimeterOffenseRaisesThreeRateVsBalancedMan() {
        val balanced = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Balanced,
            DefensiveSchemePreset.ManToMan,
        )
        val perimeter = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Perimeter,
            DefensiveSchemePreset.ManToMan,
        )
        assertTrue(perimeter.threePointAttemptRate > balanced.threePointAttemptRate)
    }

    @Test
    fun pressDefenseRaisesTurnoversVsBalancedMan() {
        val man = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Balanced,
            DefensiveSchemePreset.ManToMan,
        )
        val press = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Balanced,
            DefensiveSchemePreset.FullCourtPress,
        )
        assertTrue(press.turnoverChance > man.turnoverChance)
    }

    @Test
    fun earlyPressInteractionStacksTurnovers() {
        val earlyPress = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Early,
            DefensiveSchemePreset.FullCourtPress,
        )
        val balancedMan = SchemeMatchupResolver.resolve(
            OffensiveSchemePreset.Balanced,
            DefensiveSchemePreset.ManToMan,
        )
        assertTrue(earlyPress.turnoverChance > balancedMan.turnoverChance + 0.05)
    }
}

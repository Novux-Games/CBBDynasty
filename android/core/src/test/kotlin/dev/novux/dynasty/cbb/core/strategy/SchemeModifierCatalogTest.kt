package dev.novux.dynasty.cbb.core.strategy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SchemeModifierCatalogTest {
    @Test
    fun allSevenOffensivePresetsHaveProfiles() {
        OffensiveSchemePreset.entries.forEach { preset ->
            val profile = SchemeModifierCatalog.offensive(preset)
            assertTrue(profile.threePointAttemptRate in 0.12..0.62)
            assertTrue(profile.turnoverChance in 0.10..0.40)
            assertTrue(profile.tripPaceScale in 0.82..1.18)
        }
        assertEquals(7, OffensiveSchemePreset.entries.size)
    }

    @Test
    fun allSevenDefensivePresetsHaveProfiles() {
        DefensiveSchemePreset.entries.forEach { preset ->
            val profile = SchemeModifierCatalog.defensive(preset)
            assertTrue(profile.turnoverPressure in 0.0..0.12)
        }
        assertEquals(7, DefensiveSchemePreset.entries.size)
    }
}

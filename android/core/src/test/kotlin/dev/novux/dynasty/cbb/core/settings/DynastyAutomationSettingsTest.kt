package dev.novux.dynasty.cbb.core.settings

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DynastyAutomationSettingsTest {
  @Test
    fun defaults_matchDesignLock() {
        val settings = DynastyAutomationSettings()
        assertTrue(settings.recruitingAutoMaintain)
        assertFalse(settings.coachAutoSpend)
    }
}

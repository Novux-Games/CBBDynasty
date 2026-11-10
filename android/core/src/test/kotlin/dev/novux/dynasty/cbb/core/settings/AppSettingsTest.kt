package dev.novux.dynasty.cbb.core.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppSettingsTest {
    @Test
    fun defaults_matchLaunchExpectations() {
        val settings = AppSettings()
        assertFalse(settings.skipStudioIntro)
        assertEquals(100, settings.masterVolumePercent)
        assertTrue(settings.showDashboardNotificationFeed)
    }
}

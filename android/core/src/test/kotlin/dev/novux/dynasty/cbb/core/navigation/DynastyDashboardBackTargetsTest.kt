package dev.novux.dynasty.cbb.core.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DynastyDashboardBackTargetsTest {
    @Test
    fun ordered_prefersInSeasonShellFirst() {
        assertEquals(DynastyDashboardBackTargets.DYNASTY_SHELL, DynastyDashboardBackTargets.ORDERED.first())
    }

    @Test
    fun ordered_excludesMainMenu() {
        assertTrue(DynastyDashboardBackTargets.ORDERED.none { it == "home" })
    }
}

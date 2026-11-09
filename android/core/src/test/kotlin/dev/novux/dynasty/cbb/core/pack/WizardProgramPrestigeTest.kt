package dev.novux.dynasty.cbb.core.pack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WizardProgramPrestigeTest {
    private fun team(id: String, prestige: Int? = null) = TeamPack(
        id = id,
        conferenceId = "conf-test",
        displayName = "Test",
        shortName = "TST",
        homeCity = "City",
        homeState = "ST",
        colors = TeamColorsPack(primary = "#112233", secondary = "#445566"),
        arenaBaseName = "Arena",
        programPrestige = prestige,
    )

    @Test
    fun selectable_respectsStarterCap() {
        assertTrue(WizardProgramPrestige.isSelectable(40, unlockAll = false))
        assertFalse(WizardProgramPrestige.isSelectable(41, unlockAll = false))
        assertTrue(WizardProgramPrestige.isSelectable(99, unlockAll = true))
    }

    @Test
    fun effective_usesPackValueWhenPresent() {
        assertEquals(35, WizardProgramPrestige.effective(team("team-conf-test-a", prestige = 35)))
    }
}

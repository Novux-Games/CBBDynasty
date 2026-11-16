package dev.novux.dynasty.cbb.core.preview

import dev.novux.dynasty.cbb.core.lineup.RotationMinutesAllocator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpponentRotationPreviewBuilderTest {
    @Test
    fun build_returnsTenManRotationAtTwoHundredMinutes() {
        val snapshot = OpponentRotationPreviewBuilder.build(
            teamId = "unc",
            displayName = "North Carolina",
            programPrestige = 88,
        )
        assertEquals("North Carolina", snapshot.teamDisplayName)
        assertEquals(10, snapshot.lines.size)
        assertEquals(5, snapshot.lines.count { it.starter })
        val totalMin = snapshot.lines.sumOf { it.targetMinutes }
        assertEquals(RotationMinutesAllocator.TEAM_MINUTES_REGULATION, totalMin)
        assertTrue(snapshot.lines.all { it.playerLabel.isNotBlank() })
    }
}
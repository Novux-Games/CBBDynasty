package dev.novux.dynasty.cbb.core.school

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import org.junit.Assert.assertEquals
import org.junit.Test

class SchoolNetRankIndexTest {
    @Test
    fun ordersByPrestige() {
        val teams = listOf(
            team("low", 40),
            team("high", 95),
            team("mid", 70),
        )
        val ranks = SchoolNetRankIndex.build(teams)
        assertEquals(1, ranks["high"])
        assertEquals(2, ranks["mid"])
        assertEquals(3, ranks["low"])
    }

    private fun team(id: String, prestige: Int) = TeamDisplayOverlay(
        teamId = id,
        conferenceId = "c",
        displayName = id,
        shortName = id,
        homeCity = "X",
        homeState = "OH",
        primaryColorHex = "#000",
        secondaryColorHex = "#fff",
        logoUri = null,
        postseasonEligible = true,
        programPrestige = prestige,
    )
}

class SchoolMetricsEstimatorTest {
    @Test
    fun sosWithinRange() {
        val team = TeamDisplayOverlay(
            teamId = "a",
            conferenceId = "c",
            displayName = "A",
            shortName = "A",
            homeCity = "X",
            homeState = "OH",
            primaryColorHex = "#000",
            secondaryColorHex = "#fff",
            logoUri = null,
            postseasonEligible = true,
            programPrestige = 80,
        )
        val mates = listOf(team.copy(teamId = "b", programPrestige = 60))
        val sos = SchoolMetricsEstimator.strengthOfSchedule(team, mates)
        assertEquals(true, sos in 0..99)
    }
}

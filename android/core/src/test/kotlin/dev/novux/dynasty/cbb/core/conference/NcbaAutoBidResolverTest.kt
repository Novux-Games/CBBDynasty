package dev.novux.dynasty.cbb.core.conference

import org.junit.Assert.assertEquals
import org.junit.Test

class NcbaAutoBidResolverTest {
    @Test
    fun passesAutoBidToRunnerUpWhenChampionIneligible() {
        val result = ConferenceTournamentResult(
            conferenceId = "conf",
            templateId = "tpl",
            qualifiedTeamIds = listOf("reclass", "eligible", "other"),
            championTeamId = "reclass",
            runnerUpTeamId = "eligible",
            finishOrder = listOf("reclass", "eligible", "other"),
            ncbaAutoBidTeamId = "reclass",
            gameResults = emptyList(),
        )
        val bid = NcbaAutoBidResolver.resolve(result) { it == "eligible" || it == "other" }
        assertEquals("eligible", bid)
    }

    @Test
    fun championKeepsBidWhenEligible() {
        val result = ConferenceTournamentResult(
            conferenceId = "conf",
            templateId = "tpl",
            qualifiedTeamIds = listOf("a", "b"),
            championTeamId = "a",
            runnerUpTeamId = "b",
            finishOrder = listOf("a", "b"),
            ncbaAutoBidTeamId = "a",
            gameResults = emptyList(),
        )
        assertEquals("a", NcbaAutoBidResolver.resolve(result) { it == "a" || it == "b" })
    }

    @Test
    fun skipsMultipleIneligibleFinishers() {
        val result = ConferenceTournamentResult(
            conferenceId = "conf",
            templateId = "tpl",
            qualifiedTeamIds = listOf("r1", "r2", "ok"),
            championTeamId = "r1",
            runnerUpTeamId = "r2",
            finishOrder = listOf("r1", "r2", "ok"),
            ncbaAutoBidTeamId = "r1",
            gameResults = emptyList(),
        )
        assertEquals("ok", NcbaAutoBidResolver.resolve(result) { it == "ok" })
    }
}

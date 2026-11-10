package dev.novux.dynasty.cbb.core.selection

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SelectionSundayEngineTest {
    @Test
    fun partitionsNcbaNciAndHeritage() {
        val teams = (1..140).map { "t$it" }
        val net = teams.withIndex().associate { (i, id) -> id to i + 1 }
        val eligible = teams.associateWith { true }
        val champs = teams.take(32).toSet()
        val out = SelectionSundayEngine.run(
            SelectionSundayInput(
                seasonIndex = 1,
                netRankByTeam = net,
                postseasonEligible = eligible,
                heritageEligible = eligible,
                conferenceChampions = champs,
                programPrestige = teams.associateWith { 60 },
                records = emptyMap(),
                results = emptyList(),
            ),
        )
        assertEquals(76, out.ncbaField.size)
        assertEquals(32, out.nciField.size)
        assertEquals(16, out.heritageField.size)
        assertEquals(52, out.ncbaMainBracketTeamIds.size)
        assertEquals(24, out.ncbaField.count { it.bidType == NcbaBidType.OpeningRound })
        assertTrue(out.ncbaField.first().ncbaSeed == 1)
        assertTrue(out.ncbaField.any { it.ncbaSeed == 64 })
    }

    @Test
    fun assignsSeedIndexOrderOnMainBracket() {
        val teams = listOf("a", "b", "c")
        val net = mapOf("a" to 2, "b" to 1, "c" to 3)
        val champs = setOf("a", "b", "c")
        val out = SelectionSundayEngine.run(
            SelectionSundayInput(
                seasonIndex = 1,
                netRankByTeam = net,
                postseasonEligible = teams.associateWith { true },
                heritageEligible = teams.associateWith { true },
                conferenceChampions = champs,
                programPrestige = teams.associateWith { 70 },
                records = emptyMap(),
                results = emptyList(),
            ),
        )
        assertEquals(3, out.ncbaField.size)
        assertEquals(setOf(1, 2, 3), out.ncbaField.map { it.ncbaSeed }.toSet())
    }
}

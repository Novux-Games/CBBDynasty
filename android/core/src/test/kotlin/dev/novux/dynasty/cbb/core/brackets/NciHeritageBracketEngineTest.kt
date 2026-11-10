package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.SelectionSundayEngine
import dev.novux.dynasty.cbb.core.selection.SelectionSundayInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NciHeritageBracketEngineTest {
    @Test
    fun simulatesNciAndHeritageFromSelectionSunday() {
        val teams = (1..140).map { "t$it" }
        val net = teams.withIndex().associate { (i, id) -> id to i + 1 }
        val selection = SelectionSundayEngine.run(
            SelectionSundayInput(
                seasonIndex = 1,
                netRankByTeam = net,
                postseasonEligible = teams.associateWith { true },
                heritageEligible = teams.associateWith { true },
                conferenceChampions = teams.take(32).toSet(),
                programPrestige = teams.associateWith { 60 },
                records = emptyMap(),
                results = emptyList(),
            ),
        )
        val nciDone = NciHeritageBracketEngine.simulateNci(
            NciHeritageBracketEngine.planNci(selection),
            seed = 11L,
        )
        val heritageDone = NciHeritageBracketEngine.simulateHeritage(
            NciHeritageBracketEngine.planHeritage(selection),
            seed = 12L,
        )
        assertTrue(nciDone.completed)
        assertTrue(heritageDone.completed)
        assertEquals(31, nciDone.gameResults.size)
        assertEquals(15, heritageDone.gameResults.size)
        assertEquals(6, nciDone.gameResults.last().rsWeekIndex)
        assertEquals(5, heritageDone.gameResults.last().rsWeekIndex)
    }
}

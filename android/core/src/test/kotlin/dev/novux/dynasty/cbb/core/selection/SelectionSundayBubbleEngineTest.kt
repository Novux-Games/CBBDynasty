package dev.novux.dynasty.cbb.core.selection

import org.junit.Assert.assertTrue
import org.junit.Test

class SelectionSundayBubbleEngineTest {
    @Test
    fun buildsBubbleSummaryForSyntheticField() {
        val teams = (1..80).map { "t$it" }
        val net = teams.withIndex().associate { (i, id) -> id to i + 1 }
        val input = SelectionSundayInput(
            seasonIndex = 0,
            netRankByTeam = net,
            postseasonEligible = teams.take(70).associateWith { true },
            heritageEligible = HeritageSelectionEligibilityPolicy.mapForNetTeams(net),
            conferenceChampions = teams.take(32).toSet(),
            programPrestige = emptyMap(),
            records = emptyMap(),
            results = emptyList(),
        )
        val bubble = SelectionSundayBubbleEngine.build(input) { it }
        assertTrue(bubble.fieldSummary.contains("NCBA"))
        assertTrue(bubble.lines.isNotEmpty())
    }
}
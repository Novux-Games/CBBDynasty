package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NcbaNetRankingEngineTest {
    @Test
    fun ranksTeamsByResults() {
        val prestige = mapOf("a" to 85, "b" to 60, "c" to 45)
        val results = listOf(
            LeagueGameResult("a", "b", 88, 65, rsWeekIndex = 1),
            LeagueGameResult("a", "c", 82, 60, rsWeekIndex = 2),
            LeagueGameResult("b", "c", 70, 68, rsWeekIndex = 2),
        )
        val records = mapOf(
            "a" to LeagueTeamRecord("a", wins = 2, losses = 0),
            "b" to LeagueTeamRecord("b", wins = 1, losses = 1),
            "c" to LeagueTeamRecord("c", wins = 0, losses = 2),
        )
        val table = NcbaNetRankingEngine.compute(prestige.keys, prestige, records, results)
        assertEquals(3, table.rows.size)
        assertEquals(1, table.byTeamId.getValue("a").netRank)
        assertTrue(table.byTeamId.getValue("a").netScore > table.byTeamId.getValue("b").netScore)
        assertEquals(listOf(1, 2, 3), table.rows.map { it.netRank })
    }

    @Test
    fun producesEntryPerTeamWithNoGames() {
        val prestige = mapOf("x" to 70, "y" to 55)
        val table = NcbaNetRankingEngine.compute(prestige.keys, prestige, emptyMap(), emptyList())
        assertEquals(2, table.rows.size)
        table.rows.forEach { assertTrue(it.netRank in 1..2) }
    }
}

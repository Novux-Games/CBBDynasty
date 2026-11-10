package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TeamResumeEngineTest {
    @Test
    fun sosWithinRangeAfterGames() {
        val prestige = mapOf(
            "strong" to 90,
            "weak" to 40,
            "mid" to 65,
        )
        val results = listOf(
            LeagueGameResult("strong", "weak", 80, 60, rsWeekIndex = 1),
            LeagueGameResult("mid", "weak", 72, 68, rsWeekIndex = 1),
            LeagueGameResult("strong", "mid", 75, 70, rsWeekIndex = 2),
        )
        val metrics = TeamResumeEngine.computeLeague(prestige.keys, prestige, results)
        metrics.values.forEach { m ->
            assertTrue("${m.teamId} sos", m.sosRating in 0..99)
            assertTrue("${m.teamId} nem", m.nem in -30.0..30.0)
        }
        val weak = metrics.getValue("weak")
        assertEquals(2, weak.gamesPlayed)
        assertTrue(weak.quadRecordScore <= 0.0)
        val strong = metrics.getValue("strong")
        assertEquals(2, strong.gamesPlayed)
        assertTrue(strong.quadRecordScore > weak.quadRecordScore)
    }

    @Test
    fun zeroGamesFallsBackToPrestigeSpread() {
        val prestige = mapOf("a" to 80, "b" to 55)
        val metrics = TeamResumeEngine.computeLeague(prestige.keys, prestige, emptyList())
        assertEquals(0, metrics.getValue("a").gamesPlayed)
        assertTrue(metrics.getValue("a").sosRating >= metrics.getValue("b").sosRating)
    }
}

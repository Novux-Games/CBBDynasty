package dev.novux.dynasty.cbb.core.player.stats

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerStatsEngineTest {
    @Test
    fun seasonAverages_divideTotalsByGp() {
        val rates = PlayerStatsEngine.rateStats(
            PlayerCountingStats(
                points = 200,
                assists = 40,
                rebounds = 80,
                fieldGoalsMade = 70,
                fieldGoalsAttempted = 150,
            ),
            gamesPlayed = 10,
        )
        assertEquals(20.0, rates.pointsPerGame, 0.01)
        assertEquals(4.0, rates.assistsPerGame, 0.01)
        assertEquals(0.467, rates.fieldGoalPct, 0.01)
    }

    @Test
    fun careerRollup_blendsPriorSeasons() {
        val s1 = PlayerCountingStats(points = 100, assists = 20, rebounds = 30, steals = 5, blocks = 2, turnovers = 15)
        val s2 = PlayerCountingStats(points = 150, assists = 30, rebounds = 40, steals = 8, blocks = 3, turnovers = 20)
        val summary = PlayerStatsEngine.seasonSummary(
            gamesPlayed = 10,
            seasonTotals = s2,
            priorSeasonTotals = listOf(s1),
            priorGamesPlayed = listOf(10),
        )
        assertEquals(20, summary.careerGamesPlayed)
        assertEquals(250, summary.careerTotals.points)
        assertEquals(12.5, summary.careerAverages.pointsPerGame, 0.01)
    }
}

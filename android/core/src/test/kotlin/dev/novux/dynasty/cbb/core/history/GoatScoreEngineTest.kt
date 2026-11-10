package dev.novux.dynasty.cbb.core.history

import org.junit.Assert.assertTrue
import org.junit.Test

class GoatScoreEngineTest {
    @Test
    fun nationalPotyBoostsScore() {
        val base = career(nationalPoty = false)
        val poty = career(nationalPoty = true)
        val weights = BalanceGoatWeights()
        val s1 = GoatScoreEngine.score(base, weights)
        val s2 = GoatScoreEngine.score(poty, weights)
        assertTrue(s2 > s1)
    }

    private fun career(nationalPoty: Boolean) = PlayerCareerHistory(
        playerId = "p1",
        displayName = "Player",
        finalTeamId = "t1",
        seasons = listOf(
            PlayerSeasonHistoryLine(
                playerId = "p1",
                seasonIndex = 1,
                teamId = "t1",
                displayName = "Player",
                winSharesSeason = 6.0,
                peakOvrSeason = 88,
                minutesSeason = 900,
                nationalPoty = nationalPoty,
                nationalTitle = nationalPoty,
            ),
        ),
    )
}

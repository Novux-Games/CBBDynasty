package dev.novux.dynasty.cbb.core.preview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GamePreviewEngineTest {
    private val userStrong = TeamPreviewSide(
        teamId = "u",
        displayName = "State",
        offensiveRating = 84,
        defensiveRating = 82,
        rpiIndex = 0.72,
    )
    private val userWeak = userStrong.copy(offensiveRating = 64, defensiveRating = 62, rpiIndex = 0.35)

    @Test
    fun worksWithZeroGamesPlayedRpiFromPrestige() {
        val side = TeamPreviewSide.fromPrestige("a", "Alpha", 62)
        val opp = TeamPreviewSide.fromPrestige("b", "Beta", 60)
        val result = GamePreviewEngine.predict(
            GamePreviewRequest(user = side, opponent = opp, venue = GamePreviewVenue.Neutral),
        )
        assertTrue(result.userWinPercent in 1..99)
        assertTrue(result.predictedUserScore in 55..95)
        assertTrue(result.predictedOpponentScore in 55..95)
    }

    @Test
    fun strongerTeamAndHomeIncreaseWinChance() {
        val neutral = GamePreviewEngine.predict(
            GamePreviewRequest(userStrong, userWeak, GamePreviewVenue.Neutral),
        )
        val home = GamePreviewEngine.predict(
            GamePreviewRequest(userStrong, userWeak, GamePreviewVenue.Home),
        )
        assertTrue(home.userWinPercent >= neutral.userWinPercent)
        assertTrue(neutral.userWinPercent > 55)
    }

    @Test
    fun headToHeadPassesThrough() {
        val h2h = HeadToHeadHistory.summarize(
            listOf(
                HeadToHeadMeeting("2024-25", 78, 71),
                HeadToHeadMeeting("2023-24", 65, 70),
            ),
        )
        val result = GamePreviewEngine.predict(
            GamePreviewRequest(userStrong, userWeak, GamePreviewVenue.Home, h2h),
        )
        assertEquals(1, result.headToHead.userWins)
        assertEquals(1, result.headToHead.userLosses)
    }
}

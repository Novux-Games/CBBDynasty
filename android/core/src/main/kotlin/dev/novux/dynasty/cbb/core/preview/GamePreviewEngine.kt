package dev.novux.dynasty.cbb.core.preview

import kotlin.math.exp
import kotlin.math.roundToInt

/**
 * Win % and predicted score from ratings + RPI — [planning/game-frame.md#game-preview-sub-menu].
 */
object GamePreviewEngine {
    private const val LEAGUE_AVG_SCORE = 72.0
    private const val HOME_EDGE = 3.5
    private const val RPI_WEIGHT = 6.0

    fun predict(request: GamePreviewRequest): GamePreviewResult {
        val userPower = composite(request.user)
        val oppPower = composite(request.opponent)
        val venueEdge = when (request.venue) {
            GamePreviewVenue.Home -> HOME_EDGE
            GamePreviewVenue.Away -> -HOME_EDGE
            GamePreviewVenue.Neutral -> 0.0
        }
        val spread = (userPower - oppPower) + venueEdge
        val winProb = logistic(spread / 5.5)
        val userScore = (LEAGUE_AVG_SCORE + spread / 2.0).roundToInt().coerceIn(55, 95)
        val oppScore = (LEAGUE_AVG_SCORE - spread / 2.0).roundToInt().coerceIn(55, 95)
        return GamePreviewResult(
            userWinPercent = (winProb * 100.0).roundToInt().coerceIn(1, 99),
            predictedUserScore = userScore,
            predictedOpponentScore = oppScore,
            headToHead = request.headToHead,
        )
    }

    private fun composite(side: TeamPreviewSide): Double {
        val rating = (side.offensiveRating + side.defensiveRating) / 2.0
        return rating + side.rpiIndex * RPI_WEIGHT
    }

    private fun logistic(x: Double): Double = 1.0 / (1.0 + exp(-x))
}

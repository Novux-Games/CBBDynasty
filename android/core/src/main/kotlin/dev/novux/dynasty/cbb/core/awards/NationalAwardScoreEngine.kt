package dev.novux.dynasty.cbb.core.awards

/** Composite national award score ([awards.md#national-awards], [MEGA-POST-070], [MEGA-POST-205]). */
object NationalAwardScoreEngine {
    const val MEGA_ID: String = "MEGA-POST-070"
    const val WEIGHTS_ID: String = "MEGA-POST-205"

    data class Weights(
        val winShares: Double = 12.0,
        val postseasonTier: Double = 4.0,
        val peakOvrScale: Double = 2.0,
        val statRankNationalMax: Double = 3.0,
    )

    fun score(
        winShares: Double,
        postseasonTier: Int,
        peakOvr: Int,
        weights: Weights = Weights(),
    ): Double =
        winShares * weights.winShares +
            postseasonTier * weights.postseasonTier +
            peakOvr * weights.peakOvrScale +
            weights.statRankNationalMax
}
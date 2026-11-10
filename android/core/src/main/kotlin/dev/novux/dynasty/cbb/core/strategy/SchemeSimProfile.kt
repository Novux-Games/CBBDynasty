package dev.novux.dynasty.cbb.core.strategy

/**
 * Tunable possession tendencies per preset — [planning/strategy-schemes.md].
 * tripPaceScale below 1.0 burns game clock faster (higher pace).
 */
data class OffensiveSchemeSimProfile(
    val tripPaceScale: Double = 1.0,
    val threePointAttemptRate: Double = 0.38,
    val turnoverChance: Double = 0.22,
    val makeRateBonus: Double = 0.0,
    val ratingDelta: Int = 0,
)

data class DefensiveSchemeSimProfile(
    val ratingDelta: Int = 0,
    val turnoverPressure: Double = 0.0,
    val threePointAttemptAdjust: Double = 0.0,
    val makeRatePenalty: Double = 0.0,
)

data class ResolvedSchemeEffects(
    val tripPaceScale: Double,
    val threePointAttemptRate: Double,
    val turnoverChance: Double,
    val makeRateAdjust: Double,
    val offensiveRatingDelta: Int,
    val defensiveRatingDelta: Int,
)

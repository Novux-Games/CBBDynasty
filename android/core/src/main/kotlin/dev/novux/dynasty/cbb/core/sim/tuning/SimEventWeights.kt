package dev.novux.dynasty.cbb.core.sim.tuning

import kotlinx.serialization.Serializable

/** Tuning tables consumed each possession ([ADR-003](../docs/ADR-003-sim-tuning-bundle.md), MEGA-SIM-099). */
@Serializable
data class SimTripBucketWeights(
    val quickTurnover: Double = 1.0 / 3.0,
    val quickShot: Double = 1.0 / 3.0,
    val foulStoppage: Double = 1.0 / 3.0,
)

@Serializable
data class SimRatingGapMakeRate(
    val base: Double = 0.42,
    val perPointEdge: Double = 0.004,
    val min: Double = 0.25,
    val max: Double = 0.65,
    val kickLookBonus: Double = 0.025,
)

@Serializable
data class SimRatingGapBlockOnMiss(
    val base: Double = 0.08,
    val perDefEdge: Double = 0.002,
    val min: Double = 0.03,
    val max: Double = 0.18,
    val foulOnBlock: Double = 0.15,
)

@Serializable
data class SimShootingFoulWeights(
    val andOneRate: Double = 0.045,
    val missFoulRate: Double = 0.062,
    val lateClockMultiplier: Double = 1.35,
)

@Serializable
data class SimTurnoverSubPhaseWeights(
    val driveShare: Double = 0.28,
    val kickShare: Double = 0.22,
)

/** Made basket / FT at horn ([planning/simulation-engine.md#possession-loop--clock], MEGA-SIM-100). */
@Serializable
data class SimPeriodEndRules(
    val countMadeBasketAtPeriodEnd: Boolean = true,
    val countFreeThrowsAtPeriodEnd: Boolean = true,
)

@Serializable
data class SimEventWeights(
    val tripBuckets: SimTripBucketWeights = SimTripBucketWeights(),
    val ratingGapMakeRate: SimRatingGapMakeRate = SimRatingGapMakeRate(),
    val ratingGapBlockOnMiss: SimRatingGapBlockOnMiss = SimRatingGapBlockOnMiss(),
    val offensiveReboundRate: Double = 0.28,
    val shootingFoul: SimShootingFoulWeights = SimShootingFoulWeights(),
    val turnoverSubPhase: SimTurnoverSubPhaseWeights = SimTurnoverSubPhaseWeights(),
    val periodEnd: SimPeriodEndRules = SimPeriodEndRules(),
) {
    fun makeRateForEdge(edge: Int): Double =
        (
            ratingGapMakeRate.base +
                edge * ratingGapMakeRate.perPointEdge
            ).coerceIn(ratingGapMakeRate.min, ratingGapMakeRate.max)

    fun blockRateForEdge(defEdge: Int): Double =
        (
            ratingGapBlockOnMiss.base +
                defEdge * ratingGapBlockOnMiss.perDefEdge
            ).coerceIn(ratingGapBlockOnMiss.min, ratingGapBlockOnMiss.max)
}

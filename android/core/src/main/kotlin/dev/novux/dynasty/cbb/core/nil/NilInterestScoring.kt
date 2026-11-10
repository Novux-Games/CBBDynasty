package dev.novux.dynasty.cbb.core.nil

import kotlin.math.pow

object NilInterestScoring {
    /** NIL factor for interest meter — 1.0 when offer meets ask ([planning/game-design.md#nil-budget-workflow-design-lock]). */
    fun factor(nilOfferUsd: Int, nilAskUsd: Int, ratioExponent: Double): Double {
        if (nilOfferUsd <= 0) return 0.0
        val ask = nilAskUsd.coerceAtLeast(1)
        val ratio = (nilOfferUsd.toDouble() / ask).coerceIn(0.0, 1.0)
        return ratio.pow(ratioExponent).coerceIn(0.0, 1.0)
    }
}

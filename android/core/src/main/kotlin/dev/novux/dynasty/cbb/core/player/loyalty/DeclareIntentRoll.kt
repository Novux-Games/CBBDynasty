package dev.novux.dynasty.cbb.core.player.loyalty

import dev.novux.dynasty.cbb.core.balance.BalanceLoyaltyTuning
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
/**
 * Probabilistic early declare — anti auto-exit for elites [player-loyalty-promises.md].
 */
object DeclareIntentRoll {
    fun declareProbability(
        player: DynastyPlayer,
        mockDraftRank: Int?,
        tuning: BalanceLoyaltyTuning,
    ): Double {
        val rank = mockDraftRank ?: 80
        val base = (1.0 - (rank / 120.0)).coerceIn(0.05, 0.85)
        val proBoost = (player.ratings.proPotential / 120.0)
        var modifier = base + proBoost * 0.15
        modifier -= player.loyalty.loyalty.value * tuning.declareWeightLoyalty
        val promise = player.loyalty.promise
        if (promise?.status == PromiseStatus.Kept) {
            modifier -= tuning.promiseKeptBonus
        } else if (promise?.status == PromiseStatus.Broken) {
            modifier += tuning.promiseBrokenPenalty
        }
        if (promise?.type == CoachPromiseType.StayPledgeLottery ||
            promise?.type == CoachPromiseType.StayPledgeTopN
        ) {
            if (promise.status != PromiseStatus.Broken) {
                modifier -= 0.18
            }
        }
        val odds = modifier.coerceIn(0.0, 0.95)
        return odds
    }

    fun shouldEnterPortalFromPromise(player: DynastyPlayer, tuning: BalanceLoyaltyTuning): Boolean {
        if (player.loyalty.promise?.status == PromiseStatus.Broken) return true
        return player.loyalty.loyalty.value < 30 && !player.role.isStarter &&
            tuning.portalBrokenPromiseWeight > 0.2
    }

    fun retentionStayWeight(player: DynastyPlayer, tuning: BalanceLoyaltyTuning): Double {
        var weight = player.loyalty.loyalty.value / 99.0
        when (player.loyalty.promise?.status) {
            PromiseStatus.Kept -> weight += 0.25
            PromiseStatus.Broken -> weight -= tuning.portalBrokenPromiseWeight
            else -> Unit
        }
        return weight.coerceIn(0.0, 1.5)
    }

    /** Deterministic roll helper for tests — maps loyalty-adjusted odds to stable pseudo-random. */
    fun rollDeclare(playerId: String, probability: Double): Boolean {
        val draw = (playerId.hashCode().absoluteValue % 10_000) / 10_000.0
        return draw < probability.coerceIn(0.0, 1.0)
    }

    private val Int.absoluteValue: Int get() = if (this < 0) -this else this
}

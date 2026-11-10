package dev.novux.dynasty.cbb.core.nil

import dev.novux.dynasty.cbb.core.balance.BalanceNilRules

data class NilAssignOfferResult(
    val clampedOfferUsd: Long,
    val accepted: Boolean,
    val rejectReason: NilAssignRejectReason? = null,
)

enum class NilAssignRejectReason {
    ZeroOffer,
    ExceedsAskMultiplier,
    ExceedsRemainingBudget,
}

object NilOfferEngine {
    fun capFromAsk(nilAskUsd: Int, rules: BalanceNilRules): Long =
        nilAskUsd.coerceAtLeast(0).toLong() * rules.maxOfferMultiplierOfAsk

    fun maxAssignableUsd(
        nilAskUsd: Int,
        rules: BalanceNilRules,
        ledger: NilBudgetLedger,
        priorReservationForTargetUsd: Long,
    ): Long {
        val cap = capFromAsk(nilAskUsd, rules)
        val budgetCap = ledger.nilRemainingUsd + priorReservationForTargetUsd
        return minOf(cap, budgetCap)
    }

    fun validateAssign(
        nilAskUsd: Int,
        requestedOfferUsd: Long,
        rules: BalanceNilRules,
        ledger: NilBudgetLedger,
        priorReservationForTargetUsd: Long,
    ): NilAssignOfferResult {
        if (requestedOfferUsd <= 0L) {
            return NilAssignOfferResult(0L, accepted = true)
        }
        val cap = capFromAsk(nilAskUsd, rules)
        if (requestedOfferUsd > cap) {
            return NilAssignOfferResult(
                clampedOfferUsd = cap.coerceAtMost(maxAssignableUsd(nilAskUsd, rules, ledger, priorReservationForTargetUsd)),
                accepted = false,
                rejectReason = NilAssignRejectReason.ExceedsAskMultiplier,
            )
        }
        val maxBudget = maxAssignableUsd(nilAskUsd, rules, ledger, priorReservationForTargetUsd)
        if (requestedOfferUsd > maxBudget) {
            return NilAssignOfferResult(
                clampedOfferUsd = maxBudget,
                accepted = false,
                rejectReason = NilAssignRejectReason.ExceedsRemainingBudget,
            )
        }
        return NilAssignOfferResult(requestedOfferUsd, accepted = true)
    }
}

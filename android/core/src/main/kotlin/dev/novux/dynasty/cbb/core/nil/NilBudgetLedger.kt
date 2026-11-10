package dev.novux.dynasty.cbb.core.nil

/**
 * Collective ledger for the active recruiting cycle ([planning/game-design.md#nil-budget-workflow-design-lock]).
 */
data class NilBudgetLedger(
    val nilPoolAnnualUsd: Long,
    val nilCommittedUsd: Long,
    val nilReservedUsd: Long,
) {
    val nilRemainingUsd: Long =
        (nilPoolAnnualUsd - nilCommittedUsd - nilReservedUsd).coerceAtLeast(0)
}

object NilBudgetLedgerCalculator {
    fun compute(
        poolAnnualUsd: Long,
        committedUsd: Long,
        pendingOffersUsd: Map<String, Long>,
        scholarshipOfferProspectIds: Set<String>,
    ): NilBudgetLedger {
        val reserved = pendingOffersUsd.entries
            .filter { (prospectId, amount) ->
                prospectId in scholarshipOfferProspectIds && amount > 0L
            }
            .sumOf { it.value }
        return NilBudgetLedger(
            nilPoolAnnualUsd = poolAnnualUsd,
            nilCommittedUsd = committedUsd,
            nilReservedUsd = reserved,
        )
    }
}

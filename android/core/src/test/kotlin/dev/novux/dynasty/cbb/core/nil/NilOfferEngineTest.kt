package dev.novux.dynasty.cbb.core.nil

import dev.novux.dynasty.cbb.core.balance.BalanceNilRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NilOfferEngineTest {
    private val rules = BalanceNilRules(maxOfferMultiplierOfAsk = 3, interestNilRatioExponent = 1.0)

    @Test
    fun threeTimesAskCap_enforced() {
        assertEquals(6_000_000L, NilOfferEngine.capFromAsk(2_000_000, rules))
    }

    @Test
    fun assignWithinBudget_accepted() {
        val ledger = NilBudgetLedger(10_000_000, 0, 0)
        val result = NilOfferEngine.validateAssign(2_000_000, 2_000_000L, rules, ledger, 0)
        assertTrue(result.accepted)
        assertEquals(2_000_000L, result.clampedOfferUsd)
    }

    @Test
    fun assignAboveMultiplier_rejected() {
        val ledger = NilBudgetLedger(10_000_000, 0, 0)
        val result = NilOfferEngine.validateAssign(2_000_000, 7_000_000L, rules, ledger, 0)
        assertEquals(false, result.accepted)
        assertEquals(NilAssignRejectReason.ExceedsAskMultiplier, result.rejectReason)
    }

    @Test
    fun reservedOnlyCountsOpenScholarshipOffers() {
        val ledger = NilBudgetLedgerCalculator.compute(
            poolAnnualUsd = 10_000_000,
            committedUsd = 0,
            pendingOffersUsd = mapOf("a" to 2_000_000L, "b" to 500_000L),
            scholarshipOfferProspectIds = setOf("a"),
        )
        assertEquals(2_000_000L, ledger.nilReservedUsd)
        assertEquals(8_000_000L, ledger.nilRemainingUsd)
    }
}

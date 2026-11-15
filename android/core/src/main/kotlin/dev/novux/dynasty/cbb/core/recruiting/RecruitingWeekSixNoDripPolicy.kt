package dev.novux.dynasty.cbb.core.recruiting

/**
 * Signing Day hard sign closes the cycle — no new weekly drip on admin weeks ([MEGA-RECR-021]).
 */
object RecruitingWeekSixNoDripPolicy {
    const val MEGA_ID: String = "MEGA-RECR-021"

    fun ledgerBlocksDrip(ledger: RecruitingCycleLedger): Boolean =
        ledger.cycleClosed || RecruitingClosedCycleWeekGrantPolicy.skipsWeeklyGrant(ledger)
}
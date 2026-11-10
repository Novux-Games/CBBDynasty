package dev.novux.dynasty.cbb.core.offseason.weeknine

data class FundraisingOutcome(
    val cashUsd: Int,
    val nilBoostUsd: Int,
    val facilitySeedUsd: Int,
) {
    val totalCashGain: Int = cashUsd + facilitySeedUsd
}

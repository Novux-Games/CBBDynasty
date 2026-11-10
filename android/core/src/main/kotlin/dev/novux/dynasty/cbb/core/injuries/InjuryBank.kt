package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.balance.BalanceInjuryBankEntry

data class InjuryBankRow(
    val id: String,
    val label: String,
    val bodyPart: String,
    val severity: InjurySeverity,
    val durationWeeksMin: Int,
    val durationWeeksMax: Int,
    val weight: Double,
) {
    fun rollDurationWeeks(seed: Long): Int {
        val span = (durationWeeksMax - durationWeeksMin).coerceAtLeast(0)
        val pick = ((seed.absoluteValue % (span + 1)).toInt()) + durationWeeksMin
        return pick.coerceAtLeast(1)
    }

    companion object {
        fun fromBalance(entry: BalanceInjuryBankEntry): InjuryBankRow? {
            val severity = InjurySeverity.fromWire(entry.severity) ?: return null
            return InjuryBankRow(
                id = entry.id,
                label = entry.label,
                bodyPart = entry.bodyPart,
                severity = severity,
                durationWeeksMin = entry.durationWeeksMin,
                durationWeeksMax = entry.durationWeeksMax,
                weight = entry.weight,
            )
        }

        private val Long.absoluteValue: Long get() = if (this < 0) -this else this
    }
}

object InjuryBankPicker {
    fun pickWeighted(bank: List<InjuryBankRow>, seed: Long): InjuryBankRow? {
        if (bank.isEmpty()) return null
        val total = bank.sumOf { it.weight }
        if (total <= 0) return bank.first()
        var roll = (seed.absoluteValue % 10_000) / 10_000.0 * total
        for (row in bank) {
            roll -= row.weight
            if (roll <= 0) return row
        }
        return bank.last()
    }

    private val Long.absoluteValue: Long get() = if (this < 0) -this else this
}

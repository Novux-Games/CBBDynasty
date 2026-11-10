package dev.novux.dynasty.cbb.core.player.loyalty

/** 0–99 loyalty — [planning/player-loyalty-promises.md#loyalty-rating]. */
@JvmInline
value class LoyaltyRating(val value: Int) {
    init {
        require(value in 0..99) { "loyalty must be 0–99" }
    }

    fun tendencyBand(): LoyaltyTendencyBand = when {
        value >= 67 -> LoyaltyTendencyBand.High
        value >= 40 -> LoyaltyTendencyBand.Medium
        else -> LoyaltyTendencyBand.Low
    }

    fun applyDelta(delta: Int): LoyaltyRating = LoyaltyRating((value + delta).coerceIn(0, 99))

    companion object {
        val Default = LoyaltyRating(50)
    }
}

enum class LoyaltyTendencyBand(val label: String) {
    High("High"),
    Medium("Med"),
    Low("Low"),
}

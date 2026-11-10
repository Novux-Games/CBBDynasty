package dev.novux.dynasty.cbb.core.discipline

import dev.novux.dynasty.cbb.core.balance.BalanceDisciplineConferenceDraw

object ConferenceDrawEngine {
    fun resolve(draw: BalanceDisciplineConferenceDraw, seed: Long): Pair<ConferenceDrawOutcome, Int?> {
        val outcomes = listOf(
            ConferenceDrawOutcome.Cleared to draw.cleared,
            ConferenceDrawOutcome.ShortSuspension to draw.shortSuspensionWeight,
            ConferenceDrawOutcome.LongSuspension to draw.longSuspensionWeight,
            ConferenceDrawOutcome.Dismissed to draw.dismissedWeight,
        )
        val total = outcomes.sumOf { it.second }
        var roll = (seed.absoluteValue % 10_000) / 10_000.0 * total
        val outcome = outcomes.firstOrNull {
            roll -= it.second
            roll <= 0
        }?.first ?: ConferenceDrawOutcome.ShortSuspension
        val games = when (outcome) {
            ConferenceDrawOutcome.Cleared -> null
            ConferenceDrawOutcome.ShortSuspension -> {
                val span = (draw.shortSuspensionGamesMax - draw.shortSuspensionGamesMin).coerceAtLeast(0)
                draw.shortSuspensionGamesMin + (seed.absoluteValue % (span + 1)).toInt()
            }
            ConferenceDrawOutcome.LongSuspension -> {
                val span = (draw.longSuspensionGamesMax - draw.longSuspensionGamesMin).coerceAtLeast(0)
                draw.longSuspensionGamesMin + (seed.absoluteValue % (span + 1)).toInt()
            }
            ConferenceDrawOutcome.Dismissed -> null
        }
        return outcome to games
    }

    private val Long.absoluteValue: Long get() = if (this < 0) -this else this
}

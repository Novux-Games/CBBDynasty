package dev.novux.dynasty.cbb.core.portal

import kotlin.math.roundToInt

/** Star percentiles on final entrant pool only — [planning/recruiting-engine.md#star-distribution]. */
object PortalStarBands {
    private const val FIVE_STAR_SHARE = 0.02
    private const val FOUR_STAR_SHARE = 0.18
    private const val THREE_STAR_SHARE = 0.30

    fun applyToEntrants(entrants: List<PortalEntrant>): List<PortalEntrant> {
        if (entrants.isEmpty()) return entrants
        val sorted = entrants.sortedByDescending { it.hiddenRating }
        val poolSize = sorted.size
        val fiveCount = (poolSize * FIVE_STAR_SHARE).roundToInt().coerceAtLeast(1)
        val fourCount = (poolSize * FOUR_STAR_SHARE).roundToInt()
        val threeCount = (poolSize * THREE_STAR_SHARE).roundToInt()
        val starById = sorted.mapIndexed { index, entrant ->
            val stars = when {
                index < fiveCount -> 5
                index < fiveCount + fourCount -> 4
                index < fiveCount + fourCount + threeCount -> 3
                else -> 2
            }
            entrant.entrantId to stars
        }.toMap()
        return entrants.map { it.copy(stars = starById[it.entrantId]) }
    }
}

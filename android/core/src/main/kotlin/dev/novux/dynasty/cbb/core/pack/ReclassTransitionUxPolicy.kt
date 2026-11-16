package dev.novux.dynasty.cbb.core.pack

/** Four-year reclass transition copy on school hubs ([MEGA-GDD-145]). */
object ReclassTransitionUxPolicy {
    const val MEGA_ID: String = "MEGA-GDD-145"

    const val MAX_TRANSITION_YEARS: Int = 4

    fun seasonLabel(transitionYear: Int?, dynastySeasonIndex: Int): String? {
        val start = transitionYear ?: return null
        val year = start + dynastySeasonIndex - 1
        if (year < 1 || year > MAX_TRANSITION_YEARS) return null
        return "Reclass Y$year of $MAX_TRANSITION_YEARS"
    }
}
package dev.novux.dynasty.cbb.core.pack

/** Maps template sitePolicy to display venue ([MEGA-WORLD-144]). */
object ConferenceTournamentVenuePolicy {
    const val MEGA_ID = "MEGA-WORLD-144"

    fun venueLabel(sitePolicy: String, templateDisplayName: String): String = when (sitePolicy) {
        "neutral-primary" -> "Neutral site"
        "home-top-seed" -> "Top seed campus"
        "campus-rotation" -> "Rotating campus"
        else -> templateDisplayName.ifBlank { sitePolicy }
    }
}
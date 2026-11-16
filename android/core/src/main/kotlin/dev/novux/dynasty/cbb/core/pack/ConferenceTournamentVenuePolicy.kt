package dev.novux.dynasty.cbb.core.pack

/** Conf tournament venue column from pack template ([MEGA-WORLD-144]). */
object ConferenceTournamentVenuePolicy {
    const val MEGA_ID: String = "MEGA-WORLD-144"

    fun displayVenue(template: TournamentTemplatePack): String =
        template.championshipVenueName?.takeIf { it.isNotBlank() }
            ?: venueLabel(template.sitePolicy, template.id)

    fun venueLabel(sitePolicy: String, templateDisplayName: String): String = when (sitePolicy) {
        "neutral-primary" -> "Neutral site"
        "home-top-seed" -> "Top seed campus"
        "campus-rotation" -> "Rotating campus"
        else -> templateDisplayName.ifBlank { sitePolicy }
    }
}
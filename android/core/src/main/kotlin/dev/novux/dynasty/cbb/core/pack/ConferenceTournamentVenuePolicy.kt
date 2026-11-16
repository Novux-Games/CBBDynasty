package dev.novux.dynasty.cbb.core.pack

/** Conf tournament venue column prefers pack template label, else `sitePolicy` ([MEGA-WORLD-144]). */
object ConferenceTournamentVenuePolicy {
    const val MEGA_ID: String = "MEGA-WORLD-144"

    fun displayVenue(template: TournamentTemplatePack): String =
        template.championshipVenueName?.takeIf { it.isNotBlank() } ?: template.sitePolicy
}
package dev.novux.dynasty.cbb.core.pack

/** Directory championship venue labels on conf tournament templates ([MEGA-WORLD-144]). */
object ConferenceTournamentVenueManifestPolicy {
    const val MEGA_ID = "MEGA-WORLD-144"

    fun templateHasVenueLabel(template: TournamentTemplatePack): Boolean =
        !template.championshipVenueLabel.isNullOrBlank()
}
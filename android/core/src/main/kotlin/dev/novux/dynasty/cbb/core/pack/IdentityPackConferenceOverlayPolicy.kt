package dev.novux.dynasty.cbb.core.pack

/** Conference overlays expose pack tiebreaker stacks ([MEGA-WORLD-151]). */
object IdentityPackConferenceOverlayPolicy {
    const val MEGA_ID = "MEGA-WORLD-151"

    fun exposesTiebreakers(overlay: ConferenceDisplayOverlay): Boolean =
        overlay.standingsTiebreakers.isNotEmpty()
}
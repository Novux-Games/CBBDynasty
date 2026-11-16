package dev.novux.dynasty.cbb.core.portrait

/** Decorative headshots with initials fallback when preset id missing ([MEGA-COACH-106]). */
object PortraitAccessibilityFallbackPolicy {
    const val MEGA_ID: String = "MEGA-COACH-106"

    fun useInitialsFallback(presetId: String): Boolean = presetId.isBlank()
}
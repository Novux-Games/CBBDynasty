package dev.novux.dynasty.cbb.core.coach

/** Head coach card extension + interview accept matrix ([MEGA-GDD-120]). */
object CoachCarouselOfferAcceptPolicy {
    const val MEGA_ID: String = "MEGA-GDD-120"

    enum class OfferKind {
        Extension,
        Interview,
        Poach,
    }

    fun canAcceptWhileEmployed(kind: OfferKind): Boolean =
        kind == OfferKind.Extension || kind == OfferKind.Interview
}
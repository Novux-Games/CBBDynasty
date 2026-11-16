package dev.novux.dynasty.cbb.core.carousel

/** User head-coach card surfaces accept/decline extension offers ([MEGA-GDD-120]). */
object CarouselExtensionOfferSurfacePolicy {
    const val MEGA_ID: String = "MEGA-GDD-120"

    fun showExtensionOfferCard(isUserCoach: Boolean, hasOffer: Boolean): Boolean =
        isUserCoach && hasOffer
}
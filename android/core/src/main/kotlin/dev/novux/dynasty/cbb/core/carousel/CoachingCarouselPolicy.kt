package dev.novux.dynasty.cbb.core.carousel

/**
 * Coaching Carousel — Season 2+ only ([planning/offseason.md#coaching-carousel]).
 */
object CoachingCarouselPolicy {
    fun requiredForMembershipYear(seasonIndex: Int): Boolean = seasonIndex >= 2
}

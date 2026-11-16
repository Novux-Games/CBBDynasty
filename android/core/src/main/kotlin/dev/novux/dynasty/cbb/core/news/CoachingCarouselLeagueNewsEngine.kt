package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.carousel.CarouselLeagueBackgroundSimEngine.CarouselBackgroundSimSummary

/** League-wide coaching carousel headlines ([MEGA-SHELL-139]). */
object CoachingCarouselLeagueNewsEngine {
    const val MEGA_ID: String = "MEGA-SHELL-139"

    fun headlineAfterBackgroundSim(
        seasonIndex: Int,
        summary: CarouselBackgroundSimSummary?,
    ): NewsHeadline? {
        val total = summary?.let { it.aiFired + it.aiHired + it.aiExtended } ?: return null
        if (total <= 0) return null
        return NewsHeadline(
            id = "carousel-league-$seasonIndex-${System.currentTimeMillis()}",
            seasonIndex = seasonIndex,
            rsWeekIndex = null,
            kind = NewsHeadlineKind.CoachingCarousel,
            title = "Coaching carousel: $total league moves (fired ${summary.aiFired}, hired ${summary.aiHired}).",
            priority = 42,
            createdAtMs = System.currentTimeMillis(),
        )
    }
}
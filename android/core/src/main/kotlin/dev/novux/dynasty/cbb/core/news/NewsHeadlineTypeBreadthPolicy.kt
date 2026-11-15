package dev.novux.dynasty.cbb.core.news

/** Publisher upsert breadth for league week append ([MEGA-SHELL-040]). */
object NewsHeadlineTypeBreadthPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-040"

    fun upsertKinds(): Set<NewsHeadlineKind> = setOf(
        NewsHeadlineKind.LeagueResult,
        NewsHeadlineKind.PollNetMovement,
        NewsHeadlineKind.CoachingCarousel,
    )
}
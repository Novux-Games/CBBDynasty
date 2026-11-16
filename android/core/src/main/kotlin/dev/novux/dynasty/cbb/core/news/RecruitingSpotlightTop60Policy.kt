package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.preseason.PreseasonNewsFeedPolicy

/** Single Top 60 spotlight story per enrolling class ([MEGA-SHELL-142]). */
object RecruitingSpotlightTop60Policy {
    const val MEGA_ID: String = "MEGA-SHELL-142"

    fun storyId(seasonIndex: Int): String = PreseasonNewsFeedPolicy.top60StoryId(seasonIndex)

    fun isTop60Spotlight(headline: NewsHeadline): Boolean =
        headline.kind == NewsHeadlineKind.RecruitingSpotlight &&
            headline.id == storyId(headline.seasonIndex)
}
package dev.novux.dynasty.cbb.core.news

/**
 * Dashboard shell slice uses the same priority + recency ordering as hub stories
 * ([MEGA-SHELL-021]).
 */
object ShallowNewsFeedDashboardMergePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-021"

    fun mergedSnapshot(
        archive: List<NewsHeadline>,
        extra: List<NewsHeadline> = emptyList(),
        maxItems: Int = NewsHeadlinePublisher.SHALLOW_FEED_CAP,
        sortContext: ShallowNewsFeedSortContext? = null,
    ): ShallowNewsFeedSnapshot = ShallowNewsFeed.snapshot(archive, extra, maxItems, sortContext)
}
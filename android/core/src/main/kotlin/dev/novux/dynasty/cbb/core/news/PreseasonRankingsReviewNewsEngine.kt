package dev.novux.dynasty.cbb.core.news

/** Team poll publication task reflected in News feed ([MEGA-SHELL-152]). */
object PreseasonRankingsReviewNewsEngine {
    const val MEGA_ID: String = "MEGA-SHELL-152"

    fun headlineAfterPublication(
        seasonIndex: Int,
        schoolName: String,
        netRank: Int?,
        mediaRank: Int?,
    ): NewsHeadline {
        val net = netRank?.let { "NET #$it" }
        val media = mediaRank?.let { "media ~#$it" }
        val ranks = listOfNotNull(net, media).joinToString(", ")
        val rankNote = ranks.takeIf { it.isNotBlank() }?.let { " ($it)" }.orEmpty()
        return NewsHeadline(
            id = "preseason-polls-$seasonIndex",
            seasonIndex = seasonIndex,
            kind = NewsHeadlineKind.PollNetMovement,
            title = "Preseason polls published: $schoolName$rankNote",
            priority = 44,
            createdAtMs = System.currentTimeMillis(),
        )
    }
}
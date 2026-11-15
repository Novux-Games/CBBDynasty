package dev.novux.dynasty.cbb.core.news

data class ShallowNewsFeedSortContext(
    val portalWindowOpen: Boolean = false,
)

/** Boost portal / user-facing headlines in shallow sort ([MEGA-SHELL-146]). */
object ShallowNewsFeedPriorityBoostPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-146"

    const val PORTAL_WINDOW_BOOST: Int = 12
    const val USER_PROGRAM_BOOST: Int = 10

    fun effectivePriority(headline: NewsHeadline, context: ShallowNewsFeedSortContext?): Int {
        var priority = headline.priority
        if (context?.portalWindowOpen == true && NewsPortalHeadlineKindPolicy.isPortalKind(headline.kind)) {
            priority += PORTAL_WINDOW_BOOST
        }
        if (headline.kind == NewsHeadlineKind.UserInjury || headline.title.startsWith("Your program")) {
            priority += USER_PROGRAM_BOOST
        }
        return priority
    }
}
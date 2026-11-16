package dev.novux.dynasty.cbb.core.news

/** Portal kinds flow into shallow feed merge ([MEGA-SHELL-039]). */
object NewsPortalHeadlineFeedSurfacePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-039"

    fun includeKind(kind: NewsHeadlineKind): Boolean = NewsPortalHeadlineKindPolicy.isPortalKind(kind)
}
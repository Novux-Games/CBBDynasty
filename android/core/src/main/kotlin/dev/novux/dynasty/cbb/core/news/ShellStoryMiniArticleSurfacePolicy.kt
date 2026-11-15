package dev.novux.dynasty.cbb.core.news

/** Shell + hub story detail uses mini-article builder ([MEGA-SHELL-038]). */
object ShellStoryMiniArticleSurfacePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-038"

    fun miniArticle(headline: NewsHeadline) = NewsHeadlineArticleBuilder.miniArticle(headline)
}
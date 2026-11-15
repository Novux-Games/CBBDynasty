package dev.novux.dynasty.cbb.core.news

/** Shell news cards open story detail on tap ([MEGA-SHELL-024] → shared drill-in). */
object ShellNewsHeadlineTapPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-024"

    fun opensStoryDetail(): Boolean = NewsStoryDrillInPolicy.opensMiniArticleDetail()
}
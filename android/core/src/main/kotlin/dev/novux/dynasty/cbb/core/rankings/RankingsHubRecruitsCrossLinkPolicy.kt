package dev.novux.dynasty.cbb.core.rankings

/** Team poll rankings hub ≠ recruit Top 60 — cross-link to News ([MEGA-RECR-123], [MEGA-SHELL-150]). */
object RankingsHubRecruitsCrossLinkPolicy {
    const val MEGA_ID: String = "MEGA-RECR-123"

    const val SHELL_ID: String = "MEGA-SHELL-150"

    fun showNewsRecruitsLink(published: Boolean): Boolean = published
}
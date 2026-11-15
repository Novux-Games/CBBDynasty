package dev.novux.dynasty.cbb.core.qa

/** CI lint scope for notification composer copy ([MEGA-SHELL-087]). */
object NotificationComposerLintPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-087"

    const val LINT_SCRIPT: String = "scripts/lint_player_strings.py"

    fun lintAppliesToComposer(): Boolean = true
}
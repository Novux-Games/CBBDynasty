package dev.novux.dynasty.cbb.core.shell

import dev.novux.dynasty.cbb.core.notification.NotificationFeedCategory

/** Settings exposes master feed toggle plus per-category mutes ([MEGA-SHELL-182]). */
object NotificationCategorySettingsSectionPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-182"

    val configurableCategories: List<NotificationFeedCategory> = listOf(
        NotificationFeedCategory.Recruiting,
        NotificationFeedCategory.RosterHealth,
        NotificationFeedCategory.CoachCareer,
        NotificationFeedCategory.GameWeek,
        NotificationFeedCategory.LeagueUpdates,
    )
}
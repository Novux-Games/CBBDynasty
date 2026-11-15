package dev.novux.dynasty.cbb.core.notification

/** Move notification titles toward `strings.xml` ([MEGA-SHELL-044], [MEGA-SHELL-160], [MEGA-SHELL-165]). */
object NotificationFeedBrandingStringsPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-044"
    const val COMPOSER_MEGA_ID: String = "MEGA-SHELL-160"
    const val LINT_SCOPE_MEGA_ID: String = "MEGA-SHELL-165"

    fun titleResourceKey(kind: NotificationEventKind): String? = when (kind) {
        NotificationEventKind.UserInjury -> "notification_injury_title"
        NotificationEventKind.GameThisWeek -> "notification_game_week_title"
        NotificationEventKind.CoachSundayPoints -> "notification_coach_skill_title"
        NotificationEventKind.SigningDayPending -> "notification_signing_day_title"
        NotificationEventKind.CareerFired -> "notification_career_fired_title"
        NotificationEventKind.SelectionSunday -> "notification_selection_sunday_title"
        else -> null
    }
}
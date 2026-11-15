package dev.novux.dynasty.cbb.core.notification

/** Week advance loads extended notification snapshot fields ([MEGA-SHELL-048]). */
object WeekAdvanceFeedSnapshotPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-048"

    fun includesRecruitingEvents(input: NotificationSnapshotInput): Boolean =
        input.userCommitsThisWeek.isNotEmpty() ||
            input.userDecommitsThisWeek.isNotEmpty() ||
            input.userPortalCommitsThisWeek.isNotEmpty()
}
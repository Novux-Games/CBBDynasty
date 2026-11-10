package dev.novux.dynasty.cbb.core.notification

import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import java.util.UUID

data class NotificationSnapshotInput(
    val asOfMs: Long,
    val newInjurySummaries: List<String> = emptyList(),
    val gameThisWeekLabel: String? = null,
    val coachSkillPointsBank: Int = 0,
    val signingDayUnsignedCount: Int = 0,
    val signingDayJustCompleted: Boolean = false,
    val careerFired: Boolean = false,
    val postseasonEliminated: Boolean = false,
    val userCommitsThisWeek: List<String> = emptyList(),
    val userDecommitsThisWeek: List<String> = emptyList(),
    val majorRecordLines: List<String> = emptyList(),
    val rankingsMoveLine: String? = null,
)

object NotificationFeedComposer {
    fun compose(input: NotificationSnapshotInput): List<NotificationFeedItem> {
        val id = { UUID.randomUUID().toString() }
        return buildList {
            input.newInjurySummaries.forEach { summary ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "injury:${summary.hashCode()}",
                        kind = NotificationEventKind.UserInjury,
                        priority = NotificationPriority.High,
                        deepLink = DashboardDeepLink.Strategy,
                        title = "Injury report",
                        body = summary,
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            input.userCommitsThisWeek.forEach { name ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "commit:$name",
                        kind = NotificationEventKind.UserCommit,
                        priority = NotificationPriority.High,
                        deepLink = DashboardDeepLink.RecruitingRecommended,
                        title = "New commit",
                        body = "$name signed with your program.",
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            input.userDecommitsThisWeek.forEach { name ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "decommit:$name",
                        kind = NotificationEventKind.UserDecommit,
                        priority = NotificationPriority.High,
                        deepLink = DashboardDeepLink.RecruitingRecommended,
                        title = "Decommit",
                        body = "$name reopened recruitment.",
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            if (input.careerFired) {
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "career:fired",
                        kind = NotificationEventKind.CareerFired,
                        priority = NotificationPriority.High,
                        deepLink = DashboardDeepLink.CoachCard,
                        title = "Contract ended",
                        body = "The board has released you. Visit the job center to continue your career.",
                        createdAtMs = input.asOfMs,
                        requiresBlockingModal = true,
                    ),
                )
            }
            if (input.postseasonEliminated) {
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "career:postseason_out",
                        kind = NotificationEventKind.PostseasonEliminated,
                        priority = NotificationPriority.High,
                        deepLink = DashboardDeepLink.CoachCard,
                        title = "Season over",
                        body = "Your team has been eliminated from the postseason.",
                        createdAtMs = input.asOfMs,
                        requiresBlockingModal = true,
                    ),
                )
            }
            input.gameThisWeekLabel?.let { label ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "game:${label.hashCode()}",
                        kind = NotificationEventKind.GameThisWeek,
                        priority = NotificationPriority.Medium,
                        deepLink = DashboardDeepLink.PlayWeekGame,
                        title = "Game this week",
                        body = label,
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            if (input.coachSkillPointsBank > 0) {
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "coach_points:${input.coachSkillPointsBank}",
                        kind = NotificationEventKind.CoachSundayPoints,
                        priority = NotificationPriority.Medium,
                        deepLink = DashboardDeepLink.CoachCard,
                        title = "Coach skill points",
                        body = "You have ${input.coachSkillPointsBank} skill points to assign.",
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            if (input.signingDayUnsignedCount > 0) {
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "signing:unsigned",
                        kind = NotificationEventKind.SigningDayPending,
                        priority = NotificationPriority.Medium,
                        deepLink = DashboardDeepLink.SigningDayPositions,
                        title = "Signing Day",
                        body = "${input.signingDayUnsignedCount} prospects still need to sign.",
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            if (input.signingDayJustCompleted) {
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "signing:complete",
                        kind = NotificationEventKind.SigningDayComplete,
                        priority = NotificationPriority.Medium,
                        deepLink = DashboardDeepLink.RecruitingRecommended,
                        title = "Class signed",
                        body = "Your incoming class is locked for Signing Day.",
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            input.majorRecordLines.forEach { line ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "record:${line.hashCode()}",
                        kind = NotificationEventKind.MajorNationalRecord,
                        priority = NotificationPriority.Low,
                        deepLink = DashboardDeepLink.Strategy,
                        title = "National record",
                        body = line,
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
            input.rankingsMoveLine?.let { line ->
                add(
                    NotificationFeedItem(
                        id = id(),
                        eventKey = "rankings:${line.hashCode()}",
                        kind = NotificationEventKind.RankingsMove,
                        priority = NotificationPriority.Low,
                        deepLink = DashboardDeepLink.Strategy,
                        title = "Rankings update",
                        body = line,
                        createdAtMs = input.asOfMs,
                    ),
                )
            }
        }
    }
}

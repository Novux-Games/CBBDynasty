package dev.novux.dynasty.cbb.core.notification

import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationFeedEngineTest {
  @Test
    fun sort_prioritizesHighBeforeMedium() {
        val feed = listOf(
            item(NotificationPriority.Medium, 100L),
            item(NotificationPriority.High, 50L),
        )
        val sorted = NotificationFeedEngine.sort(feed)
        assertEquals(NotificationPriority.High, sorted.first().priority)
    }

  @Test
    fun majorRecord_eligibleAtFiftyPoints() {
        assertTrue(
            MajorRecordFeedEligibility.isFeedEligible(
                MajorRecordFeedEligibility.StatKind.Points,
                50.0,
                isNewNationalRecord = false,
            ),
        )
    }

    private fun item(priority: NotificationPriority, at: Long) = NotificationFeedItem(
        id = at.toString(),
        eventKey = "k$at",
        kind = NotificationEventKind.GameThisWeek,
        priority = priority,
        deepLink = DashboardDeepLink.PlayWeekGame,
        title = "t",
        body = "b",
        createdAtMs = at,
    )
}

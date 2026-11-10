package dev.novux.dynasty.cbb.core.news

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShallowNewsFeedTest {
    @Test
    fun capsAndPrioritizesUserInjury() {
        val low = NewsHeadline("a", 1, kind = NewsHeadlineKind.LeagueResult, title = "game", priority = 10, createdAtMs = 1)
        val high = NewsHeadline("b", 1, kind = NewsHeadlineKind.UserInjury, title = "injury", priority = 100, createdAtMs = 2)
        val feed = ShallowNewsFeed.select(listOf(low, high), maxItems = 1)
        assertEquals(1, feed.size)
        assertEquals(NewsHeadlineKind.UserInjury, feed.first().kind)
    }

    @Test
    fun leagueInjuryTierFromCount() {
        val headline = NewsHeadlinePublisher.leagueInjuryWire(1, rsWeekIndex = 3, programsAffected = 6)
        assertTrue(headline != null)
        assertEquals(NewsHeadlineKind.LeagueInjury, headline!!.kind)
    }
}

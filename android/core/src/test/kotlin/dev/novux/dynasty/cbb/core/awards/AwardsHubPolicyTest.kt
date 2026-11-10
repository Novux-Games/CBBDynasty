package dev.novux.dynasty.cbb.core.awards

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AwardsHubPolicyTest {
    @Test
    fun yearlyLockedInRegularSeason() {
        assertTrue(AwardsHubPolicy.yearlyCurrentSeasonLocked(CalendarPhase.RegularSeason))
    }

    @Test
    fun yearlyUnlockedInSeasonReview() {
        assertFalse(AwardsHubPolicy.yearlyCurrentSeasonLocked(CalendarPhase.SeasonReview))
    }

    @Test
    fun weeklyLiveOnlyRegularSeason() {
        assertTrue(AwardsHubPolicy.weeklyAwardsLive(CalendarPhase.RegularSeason))
        assertFalse(AwardsHubPolicy.weeklyAwardsLive(CalendarPhase.Postseason))
    }
}

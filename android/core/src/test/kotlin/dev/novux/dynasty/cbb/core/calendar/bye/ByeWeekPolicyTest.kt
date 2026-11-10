package dev.novux.dynasty.cbb.core.calendar.bye

import dev.novux.dynasty.cbb.core.GameConstants
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ByeWeekPolicyTest {
    @Test
    fun regularSeason_supportsWeeks1Through32() {
        assertEquals(32, ByeWeekPolicy.regularSeasonWeekIndices().count())
        assertEquals(1, ByeWeekPolicy.regularSeasonWeekIndices().first)
        assertEquals(GameConstants.MAX_REGULAR_SEASON_GAMES, ByeWeekPolicy.regularSeasonWeekIndices().last)
    }

    @Test
    fun rsWeekWithNoGame_isByeAndAdvanceAllowed() {
        assertTrue(ByeWeekPolicy.isByeWeek(CalendarPhase.RegularSeason, teamHasScheduledGame = false))
        assertTrue(ByeWeekPolicy.canAdvanceWeek(CalendarPhase.RegularSeason, teamHasScheduledGame = false))
    }

    @Test
    fun eventElimination_marksRemainingFootprintWeeksAsByes() {
        val footprint = listOf(5, 6, 7, 8)
        assertEquals(listOf(7, 8), EventByeWeeks.remainingByeWeeksAfterElimination(footprint, eliminatedAtRsWeek = 6))
    }

    @Test
    fun shortConference_byesLateConfTourneyWeeks() {
        assertEquals(listOf(5), ConferenceByeWeeks.byeWeekIndices(leagueMaxConfTourneyWeeks = 5, conferenceBracketRounds = 4))
    }

    @Test
    fun postseason_nonParticipant_allSevenWeeksAreByes() {
        assertEquals(7, PostseasonByeWeeks.nonParticipantWeeks().count())
        assertFalse(ByeWeekPolicy.isByeWeek(CalendarPhase.Postseason, teamHasScheduledGame = true))
        assertTrue(ByeWeekPolicy.isByeWeek(CalendarPhase.Postseason, teamHasScheduledGame = false))
    }

    @Test
    fun postseason_afterElimination_laterWeeksAreByes() {
        assertEquals(listOf(3, 4, 5, 6, 7), PostseasonByeWeeks.weeksAfterElimination(eliminatedAtPostseasonWeek = 2))
    }
}

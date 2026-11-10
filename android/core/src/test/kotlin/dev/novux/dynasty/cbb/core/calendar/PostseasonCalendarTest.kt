package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.GameConstants
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PostseasonCalendarTest {
    @Test
    fun leagueAlwaysRunsSevenWeeks() {
        assertEquals(7, PostseasonCalendar.leagueWeekCount)
        assertEquals(7, PostseasonCalendar.leagueWeekIndices().count())
        assertEquals(GameConstants.POSTSEASON_WEEKS, PhaseWeekPolicy.weeksInPhase(CalendarPhase.Postseason))
    }

    @Test
    fun week7Label_isNationalChampionship() {
        assertEquals("National Championship", PostseasonCalendar.ncbaRoundLabel(7))
    }

    @Test
    fun nonParticipant_canAdvanceEveryWeekAndViewBrackets() {
        assertTrue(PostseasonCalendar.mayViewLeagueBrackets(PostseasonTeamStatus.NotInNationalPostseason))
        for (week in PostseasonCalendar.leagueWeekIndices()) {
            assertTrue(
                PostseasonCalendar.canAdvanceWeek(
                    PostseasonTeamStatus.NotInNationalPostseason,
                    teamHasScheduledGame = false,
                ),
            )
            assertTrue(
                PostseasonCalendar.isByeWeek(
                    PostseasonTeamStatus.NotInNationalPostseason,
                    teamHasScheduledGame = false,
                ),
            )
        }
    }

    @Test
    fun nonParticipant_advancingSevenWeeks_entersSeasonReview() {
        val start = WeekCheckpointState.bootstrap(1, CalendarPhase.Postseason)
        val end = PostseasonCalendar.advanceAllLeagueWeeksAsNonParticipant(start)
        assertEquals(CalendarPhase.SeasonReview, end.activePosition.phase)
        assertEquals(1, end.activePosition.weekIndex)
    }
}

package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.GameConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CalendarPhaseStateMachineTest {
    @Test
    fun phases_matchTimeSystemLayerOrder() {
        val expected = listOf(
            CalendarPhase.Offseason,
            CalendarPhase.Preseason,
            CalendarPhase.Exhibition,
            CalendarPhase.RegularSeason,
            CalendarPhase.ConferenceTournament,
            CalendarPhase.Postseason,
            CalendarPhase.SeasonReview,
        )
        assertEquals(expected, CalendarPhaseStateMachine.phasesInSeasonOrder())
    }

    @Test
    fun fullMembershipYear_returnsToOffseasonWeek1NextSeason() {
        val start = PhaseWeekPolicy.bootstrapPosition(1, CalendarPhase.Offseason)
        var pos = start
        repeat(seasonOneAdvanceSteps()) {
            pos = CalendarPhaseStateMachine.advanceWeek(pos)
        }
        assertEquals(CalendarPhase.Offseason, pos.phase)
        assertEquals(2, pos.seasonIndex)
        assertEquals(1, pos.weekIndex)
    }

    @Test
    fun phaseAfterCompleting_seasonReviewStartsNewYear() {
        assertEquals(CalendarPhase.Preseason, CalendarPhaseStateMachine.phaseAfterCompleting(CalendarPhase.Offseason))
        assertEquals(CalendarPhase.SeasonReview, CalendarPhaseStateMachine.phaseAfterCompleting(CalendarPhase.Postseason))
        assertNull(CalendarPhaseStateMachine.phaseAfterCompleting(CalendarPhase.SeasonReview))
    }

    @Test
    fun exhibitionWeek3_advancesToRegularSeasonWeek1() {
        val pos = CalendarPosition(1, CalendarPhase.Exhibition, 3)
        val next = CalendarPhaseStateMachine.advanceWeek(pos)
        assertEquals(CalendarPhase.RegularSeason, next.phase)
        assertEquals(1, next.weekIndex)
    }

    private fun membershipYearWeekCount(): Int {
        var total = 0
        for (phase in CalendarPhase.entries) {
            total += PhaseWeekPolicy.weeksInPhase(phase)
        }
        check(total == 10 + 1 + 3 + GameConstants.MAX_REGULAR_SEASON_GAMES + 5 + GameConstants.POSTSEASON_WEEKS + 1)
        return total
    }

    /** Season 1 skips offseason week 10 when advancing week-by-week from a year-1 save. */
    private fun seasonOneAdvanceSteps(): Int = membershipYearWeekCount() - 1
}

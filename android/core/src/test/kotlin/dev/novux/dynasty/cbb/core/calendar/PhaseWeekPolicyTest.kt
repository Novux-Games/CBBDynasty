package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.GameConstants
import org.junit.Assert.assertEquals
import org.junit.Test

class PhaseWeekPolicyTest {
    @Test
    fun regularSeason_week32_advancesToConferenceTournament() {
        val pos = CalendarPosition(
            seasonIndex = 1,
            phase = CalendarPhase.RegularSeason,
            weekIndex = GameConstants.MAX_REGULAR_SEASON_GAMES,
        )
        val next = PhaseWeekPolicy.nextPosition(pos)
        assertEquals(CalendarPhase.ConferenceTournament, next.phase)
        assertEquals(1, next.weekIndex)
    }
}

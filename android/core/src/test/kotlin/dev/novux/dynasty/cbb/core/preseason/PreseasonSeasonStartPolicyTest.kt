package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub
import org.junit.Assert.assertEquals
import org.junit.Test

class PreseasonSeasonStartPolicyTest {
    @Test
    fun exhibitionsFirstWhenScheduled() {
        val plan = PreseasonSchedulePlan(
            exhibitions = listOf(
                ScheduledGameStub(ScheduleGameKind.Exhibition, "a", "Alpha U", homeGame = true),
            ),
            regularSeason = emptyList(),
            conferenceGameCount = 0,
            nonConferenceGameCount = 0,
            earlyEventGameCount = 0,
        )
        val focus = PreseasonSeasonStartPolicy.resolve(1, exhibitionCount = 1, plan)
        assertEquals(CalendarPhase.Exhibition, focus.calendarPosition.phase)
        assertEquals(ScheduleGameKind.Exhibition, focus.firstGame.kind)
    }

    @Test
    fun skipsToRegularSeasonWhenNoExhibitions() {
        val plan = PreseasonSchedulePlan(
            exhibitions = emptyList(),
            regularSeason = listOf(
                ScheduledGameStub(
                    ScheduleGameKind.EarlyEvent,
                    "e",
                    "Tourney",
                    homeGame = false,
                    rsWeekIndex = 2,
                ),
            ),
            conferenceGameCount = 0,
            nonConferenceGameCount = 0,
            earlyEventGameCount = 1,
        )
        val focus = PreseasonSeasonStartPolicy.resolve(1, exhibitionCount = 0, plan)
        assertEquals(CalendarPhase.RegularSeason, focus.calendarPosition.phase)
        assertEquals(2, focus.calendarPosition.weekIndex)
    }
}

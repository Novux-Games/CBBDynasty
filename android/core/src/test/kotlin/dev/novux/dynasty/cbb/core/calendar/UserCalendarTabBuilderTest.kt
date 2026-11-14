package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserCalendarTabBuilderTest {
    @Test
    fun exhibitionByeWhenNoGameThatWeek() {
        val plan = PreseasonSchedulePlan(
            exhibitions = listOf(
                stub(ScheduleGameKind.Exhibition, "A"),
            ),
            regularSeason = emptyList(),
            conferenceGameCount = 0,
            nonConferenceGameCount = 0,
            earlyEventGameCount = 0,
        )
        val rows = UserCalendarTabBuilder.build(
            activePosition = CalendarPosition(1, CalendarPhase.Exhibition, 1),
            plan = plan,
            exhibitionCount = 1,
        )
        val week2 = rows.first { it.phase == CalendarPhase.Exhibition && it.weekIndex == 2 }
        assertEquals(UserCalendarRowKind.Bye, week2.rowKind)
    }

    @Test
    fun regularSeasonGameOnAssignedWeek() {
        val plan = PreseasonSchedulePlan(
            exhibitions = emptyList(),
            regularSeason = listOf(
                stub(ScheduleGameKind.NonConference, "B", rsWeek = 4),
            ),
            conferenceGameCount = 0,
            nonConferenceGameCount = 1,
            earlyEventGameCount = 0,
        )
        val rows = UserCalendarTabBuilder.build(
            activePosition = CalendarPosition(1, CalendarPhase.RegularSeason, 4),
            plan = plan,
            exhibitionCount = 0,
        )
        val week4 = rows.first { it.phase == CalendarPhase.RegularSeason && it.weekIndex == 4 }
        assertEquals(UserCalendarRowKind.Game, week4.rowKind)
        assertEquals(1, week4.games.size)
        assertEquals("Dec 4", week4.games.single().gameDateLabel)
        assertTrue(week4.isCurrentWeek)
    }

    @Test
    fun postseasonWeekHeaderIncludesNcbaRoundName() {
        val plan = PreseasonSchedulePlan(
            exhibitions = emptyList(),
            regularSeason = emptyList(),
            conferenceGameCount = 0,
            nonConferenceGameCount = 0,
            earlyEventGameCount = 0,
        )
        val rows = UserCalendarTabBuilder.build(
            activePosition = CalendarPosition(1, CalendarPhase.Postseason, 4),
            plan = plan,
            exhibitionCount = 0,
        )
        val week4 = rows.first { it.phase == CalendarPhase.Postseason && it.weekIndex == 4 }
        assertTrue(week4.weekLabel.contains("Super 16"))
    }

    @Test
    fun sameWeek_multipleGamesKeepPlanOrder() {
        val plan = PreseasonSchedulePlan(
            exhibitions = emptyList(),
            regularSeason = listOf(
                stub(ScheduleGameKind.NonConference, "First", rsWeek = 2),
                stub(ScheduleGameKind.Conference, "Second", rsWeek = 2),
            ),
            conferenceGameCount = 1,
            nonConferenceGameCount = 1,
            earlyEventGameCount = 0,
        )
        val rows = UserCalendarTabBuilder.build(
            activePosition = CalendarPosition(1, CalendarPhase.RegularSeason, 2),
            plan = plan,
            exhibitionCount = 0,
        )
        val week2 = rows.first { it.phase == CalendarPhase.RegularSeason && it.weekIndex == 2 }
        assertEquals(2, week2.games.size)
        assertEquals(ScheduleGameKind.NonConference, week2.games[0].gameKind)
        assertEquals(ScheduleGameKind.Conference, week2.games[1].gameKind)
    }

    @Test
    fun conferenceTournament_structuralByeWeekMarked() {
        val plan = PreseasonSchedulePlan(
            exhibitions = emptyList(),
            regularSeason = emptyList(),
            conferenceGameCount = 0,
            nonConferenceGameCount = 0,
            earlyEventGameCount = 0,
        )
        val rows = UserCalendarTabBuilder.build(
            activePosition = CalendarPosition(1, CalendarPhase.ConferenceTournament, 5),
            plan = plan,
            exhibitionCount = 0,
            conferenceTourneyByeWeeks = listOf(5),
            conferenceTourneySimmed = false,
        )
        val week5 = rows.first { it.phase == CalendarPhase.ConferenceTournament && it.weekIndex == 5 }
        assertEquals(UserCalendarRowKind.Bye, week5.rowKind)
    }

    private fun stub(
        kind: ScheduleGameKind,
        name: String,
        rsWeek: Int? = null,
    ) = ScheduledGameStub(
        kind = kind,
        opponentTeamId = "t-$name",
        opponentDisplayName = name,
        homeGame = true,
        rsWeekIndex = rsWeek,
    )
}

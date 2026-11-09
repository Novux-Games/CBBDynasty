package dev.novux.dynasty.cbb.database.calendar

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameMark
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeekCheckpointMappersTest {
    @Test
    fun calendarEntity_roundTripsWithProvisionalGames() {
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.Offseason)
            .stageSim(ProvisionalGameMark("g1", 70, 68))
        val entity = state.toCalendarEntity("dyn-1")
        val provisional = state.provisionalGames.values.map { it.toEntity("dyn-1") }
        val restored = entity.toCheckpointState(provisional)
        assertEquals(state.activePosition, restored.activePosition)
        assertEquals(state.provisionalGames, restored.provisionalGames)
    }

    @Test
    fun advance_persistsCommittedBoundary() {
        val advanced = WeekCheckpointState.bootstrap(1, CalendarPhase.Offseason).advanceWeek()
        val entity = advanced.nextState.toCalendarEntity("dyn-1")
        val restored = entity.toCheckpointState(emptyList())
        assertTrue(restored.isWeekCommitted(advanced.committedWeek))
        assertEquals(2, restored.activePosition.weekIndex)
    }
}

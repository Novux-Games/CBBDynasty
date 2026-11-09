package dev.novux.dynasty.cbb.core.calendar.checkpoint

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeekCheckpointStateTest {
    private val state = WeekCheckpointState.bootstrap(seasonIndex = 1, phase = CalendarPhase.Offseason)

    @Test
    fun replay_allowed_beforeWeekAdvance() {
        val mark = ProvisionalGameMark(gameId = "g1", homeScore = 70, awayScore = 68)
        val afterSim = state.stageSim(mark)
        assertTrue(afterSim.canReplay("g1"))
    }

    @Test
    fun replay_replacesProvisionalMark() {
        val s1 = state.stageSim(ProvisionalGameMark("g1", 60, 58))
        val s2 = s1.stageSim(ProvisionalGameMark("g1", 72, 70))
        assertEquals(2, s2.provisionalGames.getValue("g1").simGeneration)
    }

    @Test
    fun games_locked_afterWeekAdvance() {
        val mark = ProvisionalGameMark(gameId = "g1", homeScore = 80, awayScore = 75)
        val afterSim = state.stageSim(mark)
        val advanced = afterSim.advanceWeek()
        assertFalse(advanced.nextState.canReplay("g1"))
        assertTrue(advanced.nextState.isGameLocked("g1"))
        assertEquals(1, advanced.gamesCommitted.size)
        assertTrue(advanced.nextState.isWeekCommitted(advanced.committedWeek))
    }

    @Test
    fun newWeek_startsFreshProvisionalBucket() {
        val advanced = state
            .stageSim(ProvisionalGameMark("g1", 1, 2))
            .advanceWeek()
            .nextState
        assertTrue(advanced.provisionalGames.isEmpty())
        assertEquals(2, advanced.activePosition.weekIndex)
    }
}

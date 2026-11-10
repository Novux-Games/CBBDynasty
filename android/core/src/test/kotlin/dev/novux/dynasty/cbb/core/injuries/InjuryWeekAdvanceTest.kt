package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.WeekAdvanceRunner
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InjuryWeekAdvanceTest {
  private val injury = InjuryInstance(playerId = "p1", injuryId = "ankle_sprain_minor", weeksRemaining = 3)

    @Test
    fun byeWeekAdvance_decrementsWeeksRemaining() {
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.RegularSeason).copy(
            activePosition = CalendarPosition(1, CalendarPhase.RegularSeason, 12),
        )
        val effects = WeekAdvanceRunner.advanceWeek(state, teamHasScheduledGame = false, injuries = listOf(injury))
        assertTrue(effects.wasByeWeek)
        assertEquals(2, effects.injuries.single().weeksRemaining)
    }

    @Test
    fun gameWeekAdvance_alsoDecrementsWeeksRemaining() {
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.RegularSeason)
        val effects = WeekAdvanceRunner.advanceWeek(state, teamHasScheduledGame = true, injuries = listOf(injury))
        assertEquals(2, effects.injuries.single().weeksRemaining)
    }

    @Test
    fun lastWeekTicksToHealthy() {
        val almostBack = injury.copy(weeksRemaining = 1)
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.RegularSeason)
        val effects = WeekAdvanceRunner.advanceWeek(state, teamHasScheduledGame = false, injuries = listOf(almostBack))
        assertTrue(effects.injuries.isEmpty())
    }

    @Test
    fun offseasonAdvance_doesNotTickInjuries() {
        val after = InjuryWeekAdvance.afterWeekCommit(CalendarPhase.Offseason, listOf(injury))
        assertEquals(injury, after.single())
    }
}

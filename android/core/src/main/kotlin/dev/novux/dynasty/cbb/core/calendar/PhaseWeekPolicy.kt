package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.GameConstants

/**
 * Week counts and transitions per phase — [planning/time-system.md].
 */
object PhaseWeekPolicy {
    fun weeksInPhase(phase: CalendarPhase): Int =
        when (phase) {
            CalendarPhase.Offseason -> 9
            CalendarPhase.Preseason -> 1
            CalendarPhase.Exhibition -> 3
            CalendarPhase.RegularSeason -> GameConstants.MAX_REGULAR_SEASON_GAMES
            CalendarPhase.ConferenceTournament -> 5
            CalendarPhase.Postseason -> GameConstants.POSTSEASON_WEEKS
            CalendarPhase.SeasonReview -> 1
        }

    fun nextPosition(current: CalendarPosition): CalendarPosition {
        val maxWeek = weeksInPhase(current.phase)
        return if (current.weekIndex < maxWeek) {
            current.copy(weekIndex = current.weekIndex + 1)
        } else {
            advancePhase(current)
        }
    }

    private fun advancePhase(current: CalendarPosition): CalendarPosition {
        val phases = CalendarPhase.entries
        val nextPhaseIndex = current.phase.ordinal + 1
        if (nextPhaseIndex >= phases.size) {
            return CalendarPosition(
                seasonIndex = current.seasonIndex + 1,
                phase = CalendarPhase.Offseason,
                weekIndex = 1,
            )
        }
        val nextPhase = phases[nextPhaseIndex]
        return CalendarPosition(
            seasonIndex = current.seasonIndex,
            phase = nextPhase,
            weekIndex = 1,
        )
    }

    fun bootstrapPosition(seasonIndex: Int, phase: CalendarPhase): CalendarPosition =
        CalendarPosition(seasonIndex = seasonIndex, phase = phase, weekIndex = 1)
}

package dev.novux.dynasty.cbb.core.calendar

/**
 * Legal week-by-week calendar transitions — [planning/time-system.md] layer order:
 * Offseason → Preseason → Exhibition → RegularSeason → ConferenceTournament → Postseason → SeasonReview → (next season Offseason).
 */
object CalendarPhaseStateMachine {
    /** Canonical in-season order (one membership year). */
    fun phasesInSeasonOrder(): List<CalendarPhase> = CalendarPhase.entries.toList()

    fun advanceWeek(position: CalendarPosition): CalendarPosition = PhaseWeekPolicy.nextPosition(position)

    /**
     * Phase entered after the final week of [completedPhase] is committed (week advance rolls forward).
     */
    fun phaseAfterCompleting(completedPhase: CalendarPhase): CalendarPhase? {
        val idx = completedPhase.ordinal
        val next = idx + 1
        return if (next >= CalendarPhase.entries.size) null else CalendarPhase.entries[next]
    }

    fun isFinalWeekOfPhase(position: CalendarPosition): Boolean =
        position.weekIndex >= PhaseWeekPolicy.weeksInPhase(position.phase)

    /**
     * Rolls [position] forward until [target] is reached or passed (inclusive), returning each step.
     */
    fun advanceUntil(
        start: CalendarPosition,
        target: CalendarPosition,
    ): List<CalendarPosition> {
        require(start.seasonIndex <= target.seasonIndex) { "start must not be after target season" }
        val steps = mutableListOf(start)
        var current = start
        while (current.compareToPosition(target) < 0) {
            current = advanceWeek(current)
            steps += current
            if (steps.size > 200) error("advanceUntil exceeded safety limit")
        }
        return steps
    }
}

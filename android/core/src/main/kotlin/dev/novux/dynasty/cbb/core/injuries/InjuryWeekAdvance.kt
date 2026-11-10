package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * Injury clock ticks on **week advance**, including bye weeks ([planning/injuries.md], [planning/time-system.md#advance-rules]).
 */
object InjuryCalendarPolicy {
    private val tickPhases: Set<CalendarPhase> = setOf(
        CalendarPhase.Exhibition,
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
        CalendarPhase.SeasonReview,
    )

    fun ticksOnWeekAdvance(phase: CalendarPhase): Boolean = phase in tickPhases
}

object InjuryWeekAdvance {
  /**
   * Decrements each injury by one committed week. Instances reaching 0 are removed (returned healthy).
   */
    fun afterWeekCommit(
        phaseCommitted: CalendarPhase,
        injuries: List<InjuryInstance>,
    ): List<InjuryInstance> {
        if (!InjuryCalendarPolicy.ticksOnWeekAdvance(phaseCommitted)) return injuries
        return injuries.mapNotNull { injury ->
            val next = injury.weeksRemaining - 1
            if (next <= 0) null else injury.copy(weeksRemaining = next)
        }
    }
}

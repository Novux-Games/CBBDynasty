package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub

/** Games on the user's calendar for the active week ([planning/game-frame.md#dashboard-task-gate]). */
object InSeasonWeekGames {
    fun forPosition(
        position: CalendarPosition,
        plan: PreseasonSchedulePlan?,
        exhibitionCount: Int,
    ): List<ScheduledGameStub> {
        if (plan == null) return emptyList()
        return when (position.phase) {
            CalendarPhase.Exhibition -> {
                val capped = exhibitionCount.coerceIn(0, plan.exhibitions.size)
                if (position.weekIndex in 1..capped) {
                    plan.exhibitions.getOrNull(position.weekIndex - 1)?.let { listOf(it) }.orEmpty()
                } else {
                    emptyList()
                }
            }
            CalendarPhase.RegularSeason ->
                plan.regularSeason.filter { it.rsWeekIndex == position.weekIndex }
            else -> emptyList()
        }
    }
}

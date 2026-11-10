package dev.novux.dynasty.cbb.core.offseason.weektwo

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
/**
 * Scholarship offers open after NBA draft results (offseason week 2, R1)
 * ([planning/recruiting-engine.md#signing-period-r1r4]).
 */
object OffseasonScholarshipPolicy {
    const val SCHOLARSHIPS_OPEN_OFFSEASON_WEEK: Int = 2

    fun scholarshipsOpen(phase: CalendarPhase, offseasonWeekIndex: Int?): Boolean {
        if (phase == CalendarPhase.Preseason ||
            phase == CalendarPhase.Exhibition ||
            phase == CalendarPhase.RegularSeason ||
            phase == CalendarPhase.ConferenceTournament ||
            phase == CalendarPhase.Postseason
        ) {
            return true
        }
        if (phase != CalendarPhase.Offseason) return false
        val week = offseasonWeekIndex ?: return false
        return week >= SCHOLARSHIPS_OPEN_OFFSEASON_WEEK
    }
}

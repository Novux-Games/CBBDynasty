package dev.novux.dynasty.cbb.core.offseason.weekten

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition

/** Offseason-only week transitions (conference week skip on season 1). */
object OffseasonWeekAdvancePolicy {
    fun nextPosition(current: CalendarPosition): CalendarPosition {
        require(current.phase == CalendarPhase.Offseason) { "offseason only" }
        if (current.weekIndex == 9 && OffseasonConferenceOffersPolicy.skipConferenceOffersWeek(current.seasonIndex)) {
            return CalendarPosition(
                seasonIndex = current.seasonIndex,
                phase = CalendarPhase.Preseason,
                weekIndex = 1,
            )
        }
        if (current.weekIndex < OffseasonWeekTenConstants.OFFSEASON_WEEK_INDEX) {
            return current.copy(weekIndex = current.weekIndex + 1)
        }
        return CalendarPosition(
            seasonIndex = current.seasonIndex,
            phase = CalendarPhase.Preseason,
            weekIndex = 1,
        )
    }
}

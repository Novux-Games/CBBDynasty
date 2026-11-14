package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.calendar.CalendarPosition

/** Scoped dismiss keys so the same [DashboardTaskId] can reappear on later weeks ([MEGA-FRAME-100]). */
object DashboardTaskDismissal {
    fun key(position: CalendarPosition, id: DashboardTaskId): String =
        "${position.phase.name}:${position.weekIndex}:$id"
}
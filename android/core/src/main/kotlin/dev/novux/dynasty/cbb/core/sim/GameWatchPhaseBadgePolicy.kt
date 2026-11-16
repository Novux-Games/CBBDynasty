package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/** Exhibition vs RS chrome label ([MEGA-SIM-128]). */
object GameWatchPhaseBadgePolicy {
    const val MEGA_ID = "MEGA-SIM-128"

    fun badge(phase: CalendarPhase?, isScrimmage: Boolean): String = when {
        isScrimmage -> "Scrimmage"
        phase == CalendarPhase.Exhibition -> "Exhibition"
        phase == CalendarPhase.RegularSeason -> "Regular season"
        phase == CalendarPhase.Postseason -> "Postseason"
        else -> ""
    }
}
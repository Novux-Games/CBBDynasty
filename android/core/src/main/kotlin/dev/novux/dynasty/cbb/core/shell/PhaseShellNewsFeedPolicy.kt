package dev.novux.dynasty.cbb.core.shell

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/** Offseason/preseason shell News tab must load shallow archive + portal sort context ([MEGA-SHELL-180]). */
object PhaseShellNewsFeedPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-180"

    fun offseasonWeekForFeed(phase: CalendarPhase, weekIndex: Int): Int? =
        when (phase) {
            CalendarPhase.Offseason, CalendarPhase.Preseason -> weekIndex
            else -> null
        }
}
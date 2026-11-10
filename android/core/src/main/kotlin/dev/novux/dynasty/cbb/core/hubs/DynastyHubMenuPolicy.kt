package dev.novux.dynasty.cbb.core.hubs

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition

/** Hamburger hub visibility ([planning/game-frame.md#top-menu-hamburger--hubs]). */
object DynastyHubMenuPolicy {
    fun conferenceTournamentMenuVisible(position: CalendarPosition): Boolean =
        position.phase == CalendarPhase.ConferenceTournament

    fun marchMayhemMenuVisible(position: CalendarPosition): Boolean =
        position.phase == CalendarPhase.Postseason
}

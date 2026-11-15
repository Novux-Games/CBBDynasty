package dev.novux.dynasty.cbb.core.gdd

import dev.novux.dynasty.cbb.core.preseason.tournament.RegularSeasonSlotBudget

/** Preseason task 8 enforces 32-game RS cap including non-conference picks ([MEGA-GDD-105]). */
object PreseasonNonConferenceCapPolicy {
    const val MEGA_ID: String = "MEGA-GDD-105"

  val rsCap: Int = RegularSeasonSlotBudget.RS_CAP

    fun nonConferenceSlotsRemaining(
        builderSlots: Int,
        conferenceLocked: Int,
    ): Int = (builderSlots - conferenceLocked).coerceAtLeast(0)
}
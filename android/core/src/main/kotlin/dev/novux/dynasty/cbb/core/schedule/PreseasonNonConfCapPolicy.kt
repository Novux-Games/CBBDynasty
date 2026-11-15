package dev.novux.dynasty.cbb.core.schedule

import dev.novux.dynasty.cbb.core.GameConstants

/** Task 8 non-conference picker enforces 32-game RS cap ([MEGA-GDD-105]). */
object PreseasonNonConfCapPolicy {
    const val MEGA_ID: String = "MEGA-GDD-105"

    val regularSeasonCap: Int = GameConstants.MAX_REGULAR_SEASON_GAMES

    fun nonConferenceSlotsWithinCap(context: RegularSeasonScheduleBuilderUxPolicy.BuilderContext): Boolean {
        val slots = RegularSeasonScheduleBuilderUxPolicy.previewSlotSummary(context)
        return slots.totalRsCap <= regularSeasonCap
    }

    fun planWithinCap(plan: PreseasonSchedulePlan): Boolean =
        plan.totalRsGames <= regularSeasonCap
}
package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * Year-long recruiting cycle — not offseason-only ([planning/recruiting-engine.md]).
 */
object RecruitingCyclePolicy {
    val inSeasonPhases: Set<CalendarPhase> = setOf(
        CalendarPhase.Preseason,
        CalendarPhase.Exhibition,
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
    )

    fun hubAccessible(phase: CalendarPhase, offseasonWeekIndex: Int? = null): Boolean =
        when (phase) {
            CalendarPhase.Offseason -> (offseasonWeekIndex ?: 1) >= 2
            in inSeasonPhases -> true
            else -> false
        }
}

enum class RecruitingHubTab {
    Recommended,
    HsJuco,
    TransferPortal,
}

object RecruitingHubDefaults {
    val tabs: List<RecruitingHubTab> = listOf(
        RecruitingHubTab.Recommended,
        RecruitingHubTab.HsJuco,
        RecruitingHubTab.TransferPortal,
    )

    val defaultTab: RecruitingHubTab = RecruitingHubTab.Recommended
}

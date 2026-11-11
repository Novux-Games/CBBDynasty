package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

data class RecruitingWatchlistNudge(
    val prospectName: String,
    val nationalRank: Int? = null,
    val reEngage: Boolean = false,
)

/**
 * In-season recruiting optional dashboard inputs ([planning/dashboard-tasks.md#in-season-exhibitions--postseason]).
 */
data class InSeasonRecruitingDashboardContext(
    val phase: CalendarPhase,
    val offseasonWeekIndex: Int? = null,
    val pointsAvailable: Int,
    val cycleClosed: Boolean,
    val watchlistNudge: RecruitingWatchlistNudge? = null,
)

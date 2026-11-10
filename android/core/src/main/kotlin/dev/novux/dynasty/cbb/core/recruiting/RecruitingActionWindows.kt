package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingScoutEntry
import dev.novux.dynasty.cbb.core.balance.BalanceVisitAllowedThrough
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * Scout game + official visit calendar gates ([planning/recruiting-engine.md#action-costs-design-lock]).
 */
object RecruitingActionWindows {
    private val inSeasonVisitPhases: Set<CalendarPhase> = setOf(
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
    )

    fun scoutTheirGameOpen(
        phase: CalendarPhase,
        scout: BalanceRecruitingScoutEntry = BalanceRecruitingScoutEntry(),
    ): Boolean {
        if (scout.allowedPhases.isEmpty()) {
            return phase == CalendarPhase.RegularSeason
        }
        return scout.allowedPhases.any { phaseKey -> phaseFromBalanceKey(phaseKey) == phase }
    }

    fun officialVisitOpen(
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
        allowedThrough: BalanceVisitAllowedThrough = BalanceVisitAllowedThrough(),
    ): Boolean {
        if (allowedThrough.includesRegularSeason && phase in inSeasonVisitPhases) {
            return true
        }
        if (phase == CalendarPhase.Offseason) {
            val week = offseasonWeekIndex ?: return false
            return week in 2..allowedThrough.weekIndexMax
        }
        return false
    }

    private fun phaseFromBalanceKey(key: String): CalendarPhase? =
        when (key.lowercase()) {
            "offseason" -> CalendarPhase.Offseason
            "preseason" -> CalendarPhase.Preseason
            "exhibition" -> CalendarPhase.Exhibition
            "regular_season" -> CalendarPhase.RegularSeason
            "conference_tournament" -> CalendarPhase.ConferenceTournament
            "postseason" -> CalendarPhase.Postseason
            "season_review" -> CalendarPhase.SeasonReview
            else -> null
        }
}

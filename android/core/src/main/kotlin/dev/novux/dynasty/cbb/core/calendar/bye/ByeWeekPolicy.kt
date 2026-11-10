package dev.novux.dynasty.cbb.core.calendar.bye

import dev.novux.dynasty.cbb.core.GameConstants
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * Bye weeks — no team game that week; advance still allowed ([planning/time-system.md]).
 */
object ByeWeekPolicy {
    val byeCapablePhases: Set<CalendarPhase> = setOf(
        CalendarPhase.Exhibition,
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
    )

    fun isByeWeek(phase: CalendarPhase, teamHasScheduledGame: Boolean): Boolean {
        if (phase !in byeCapablePhases) return false
        return !teamHasScheduledGame
    }

    /** Week advance is never blocked for lack of a user game (including byes). */
    fun canAdvanceWeek(phase: CalendarPhase, teamHasScheduledGame: Boolean): Boolean = true

    fun regularSeasonWeekIndices(): IntRange = 1..GameConstants.MAX_REGULAR_SEASON_GAMES
}

/**
 * Early-season event byes after bracket elimination ([planning/time-system.md#early-season-event-tournaments--byes]).
 */
object EventByeWeeks {
    fun remainingByeWeeksAfterElimination(
        eventFootprintRsWeeks: List<Int>,
        eliminatedAtRsWeek: Int,
    ): List<Int> = eventFootprintRsWeeks.filter { it > eliminatedAtRsWeek }
}

/**
 * Conference tournament byes when a league bracket is shorter than pack max weeks.
 */
object ConferenceByeWeeks {
    fun byeWeekIndices(
        leagueMaxConfTourneyWeeks: Int,
        conferenceBracketRounds: Int,
    ): List<Int> {
        require(conferenceBracketRounds <= leagueMaxConfTourneyWeeks) {
            "conference rounds cannot exceed league max conf weeks"
        }
        return (conferenceBracketRounds + 1..leagueMaxConfTourneyWeeks).toList()
    }
}

/**
 * National postseason byes for non-participants or eliminated teams ([planning/time-system.md#national-postseason-weeks]).
 */
object PostseasonByeWeeks {
    fun nonParticipantWeeks(): IntRange = 1..GameConstants.POSTSEASON_WEEKS

    fun weeksAfterElimination(eliminatedAtPostseasonWeek: Int): List<Int> =
        (eliminatedAtPostseasonWeek + 1..GameConstants.POSTSEASON_WEEKS).toList()
}

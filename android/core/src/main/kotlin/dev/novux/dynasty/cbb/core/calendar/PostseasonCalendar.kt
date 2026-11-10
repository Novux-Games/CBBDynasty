package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.GameConstants
import dev.novux.dynasty.cbb.core.calendar.bye.ByeWeekPolicy
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState

/**
 * National postseason runs **7** league-wide weeks for every dynasty ([planning/time-system.md]).
 */
enum class PostseasonTeamStatus {
    /** Still alive in NCBA / NCI / Heritage. */
    ActiveInBracket,
    /** Out of all national brackets. */
    Eliminated,
    /** One of the 241 teams with no national postseason assignment. */
    NotInNationalPostseason,
}

object PostseasonCalendar {
    const val OPENING_ROUND_WEEK = 1

    val leagueWeekCount: Int = GameConstants.POSTSEASON_WEEKS

    fun leagueWeekIndices(): IntRange = 1..leagueWeekCount

    /** NCBA-primary round labels on shared week indices ([planning/time-system.md#national-postseason-weeks]). */
    fun ncbaRoundLabel(weekIndex: Int): String =
        when (weekIndex) {
            1 -> "Opening Round"
            2 -> "Round 1"
            3 -> "Round 2"
            4 -> "Super 16"
            5 -> "Great 8"
            6 -> "Championship 4"
            7 -> "National Championship"
            else -> error("postseason week $weekIndex out of range 1–$leagueWeekCount")
        }

    fun displayWeekHeader(weekIndex: Int): String = "Postseason Week $weekIndex — ${ncbaRoundLabel(weekIndex)}"

    /** Non-participants and eliminated teams may still advance and follow live brackets ([planning/postseason.md]). */
    fun mayViewLeagueBrackets(status: PostseasonTeamStatus): Boolean = true

    fun canAdvanceWeek(status: PostseasonTeamStatus, teamHasScheduledGame: Boolean): Boolean =
        ByeWeekPolicy.canAdvanceWeek(CalendarPhase.Postseason, teamHasScheduledGame)

    fun isByeWeek(status: PostseasonTeamStatus, teamHasScheduledGame: Boolean): Boolean =
        when (status) {
            PostseasonTeamStatus.NotInNationalPostseason -> true
            PostseasonTeamStatus.Eliminated,
            PostseasonTeamStatus.ActiveInBracket,
            -> !teamHasScheduledGame
        }

    /**
     * Simulates advancing all seven postseason weeks for a team with no games (non-participant path).
     */
    fun advanceAllLeagueWeeksAsNonParticipant(
        start: WeekCheckpointState,
    ): WeekCheckpointState {
        require(start.activePosition.phase == CalendarPhase.Postseason) {
            "start must be in Postseason phase"
        }
        var state = start
        repeat(leagueWeekCount) {
            require(
                canAdvanceWeek(PostseasonTeamStatus.NotInNationalPostseason, teamHasScheduledGame = false),
            )
            state = state.advanceWeek().nextState
        }
        return state
    }
}

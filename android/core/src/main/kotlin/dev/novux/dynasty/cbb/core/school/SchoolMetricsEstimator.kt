package dev.novux.dynasty.cbb.core.school

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import kotlin.math.roundToInt

object SchoolMetricsEstimator {
    fun strengthOfSchedule(team: TeamDisplayOverlay, conferenceMates: List<TeamDisplayOverlay>): Int {
        if (conferenceMates.isEmpty()) return team.programPrestige
        val avg = conferenceMates.map { it.programPrestige }.average()
        return ((avg * 0.7) + (team.programPrestige * 0.3)).roundToInt().coerceIn(0, 99)
    }

    fun powerIndex(programPrestige: Int, sosRating: Int): Int =
        ((programPrestige * 0.6) + (sosRating * 0.4)).roundToInt().coerceIn(0, 99)

    fun teamRatings(team: TeamDisplayOverlay): Pair<Int, Int> {
        val bump = (team.teamId.hashCode() and 7) - 3
        val off = (team.programPrestige + bump).coerceIn(40, 95)
        val def = (team.programPrestige - bump / 2).coerceIn(40, 95)
        return off to def
    }

    fun conferenceStandingPlaceholder(confRank: Int, confSize: Int): String =
        "${ordinal(confRank)} in conference ($confSize teams)"

    private fun ordinal(rank: Int): String = when (rank) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        else -> "${rank}th"
    }
}

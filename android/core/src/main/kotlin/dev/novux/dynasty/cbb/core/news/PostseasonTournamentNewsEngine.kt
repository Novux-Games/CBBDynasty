package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState

/** Postseason bracket headlines after league batch ([MEGA-SHELL-143]). */
object PostseasonTournamentNewsEngine {
    const val MEGA_ID: String = "MEGA-SHELL-143"

    fun headlineForPostseasonWeek(
        standings: LeagueStandingsState,
        postseasonWeek: Int,
        userTeamId: String?,
        teamDisplayName: (String) -> String,
    ): NewsHeadline? {
        val userLine = userTeamId?.let { id ->
            standings.netRankByTeam[id]?.let { net ->
                val name = teamDisplayName(id)
                "$name (NET #$net) is in the postseason field."
            }
        }
        val title = buildString {
            append("Postseason Week $postseasonWeek: ")
            append(userLine ?: "National brackets advance across NCBA, NCI, and Heritage.")
        }
        return NewsHeadline(
            id = "postseason-${standings.seasonIndex}-pw$postseasonWeek",
            seasonIndex = standings.seasonIndex,
            rsWeekIndex = null,
            kind = NewsHeadlineKind.PostseasonTournament,
            title = title,
            priority = 50,
            createdAtMs = System.currentTimeMillis(),
        )
    }
}
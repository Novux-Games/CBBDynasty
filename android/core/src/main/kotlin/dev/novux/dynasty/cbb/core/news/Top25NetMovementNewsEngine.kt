package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState

/** Top-25 NET movement stories for mixed feed ([MEGA-SHELL-148]). */
object Top25NetMovementNewsEngine {
    const val MEGA_ID: String = "MEGA-SHELL-148"

    private const val TOP_N: Int = 25

    fun headlineForWeek(
        standings: LeagueStandingsState,
        rsWeekIndex: Int,
        teamDisplayName: (String) -> String,
        userTeamId: String? = null,
    ): NewsHeadline? {
        val ranks = standings.netRankByTeam
        if (ranks.isEmpty() || rsWeekIndex < 2) return null
        val top = ranks.entries
            .filter { it.value <= TOP_N }
            .sortedBy { it.value }
            .take(3)
        if (top.isEmpty()) return null
        val leaders = top.joinToString(" · ") { (id, rank) ->
            "${teamDisplayName(id)} #$rank"
        }
        val userInTop25 = userTeamId?.let { ranks[it] }?.let { net ->
            if (net <= TOP_N) "Your program sits at NET #$net." else null
        }
        val title = buildString {
            append("Top 25 NET (Week $rsWeekIndex): ")
            append(leaders)
            userInTop25?.let { append(" ").append(it) }
        }
        return NewsHeadline(
            id = "top25-net-${standings.seasonIndex}-w$rsWeekIndex",
            seasonIndex = standings.seasonIndex,
            rsWeekIndex = rsWeekIndex,
            kind = NewsHeadlineKind.PollNetMovement,
            title = title,
            priority = 36,
            createdAtMs = System.currentTimeMillis(),
        )
    }
}
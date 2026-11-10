package dev.novux.dynasty.cbb.core.school

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay

object SchoolNetRankIndex {
    fun build(teams: List<TeamDisplayOverlay>): Map<String, Int> {
        val sorted = teams.sortedByDescending { it.programPrestige }
        return sorted.mapIndexed { index, team -> team.teamId to index + 1 }.toMap()
    }

    fun mediaPollRank(netRank: Int): Int = (netRank + netRank / 5).coerceAtMost(365)

    fun coachesPollRank(netRank: Int): Int = (netRank - netRank / 7).coerceAtLeast(1)
}

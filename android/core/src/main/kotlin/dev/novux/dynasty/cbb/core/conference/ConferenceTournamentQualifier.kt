package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack

object ConferenceTournamentQualifier {
    fun qualify(
        template: TournamentTemplatePack,
        teamIds: List<String>,
        records: Map<String, LeagueTeamRecord>,
    ): List<String> {
        val cap = qualificationCount(template.qualificationPolicy, template.bracketSize)
        return teamIds
            .sortedWith(
                compareByDescending<String> { records[it]?.wins ?: 0 }
                    .thenBy { records[it]?.losses ?: 0 }
                    .thenBy { it },
            )
            .take(cap.coerceAtMost(teamIds.size))
    }

    private fun qualificationCount(policy: String, bracketSize: Int): Int {
        val trimmed = policy.trim().lowercase()
        if (trimmed == "all" || trimmed == "all-members") return bracketSize
        val top = Regex("^top-(\\d+)$").find(trimmed)?.groupValues?.getOrNull(1)?.toIntOrNull()
        if (top != null) return top
        return bracketSize
    }
}

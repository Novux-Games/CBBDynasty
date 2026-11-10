package dev.novux.dynasty.cbb.core.preview

import dev.novux.dynasty.cbb.core.school.SchoolHubOverview

/** Résumé index for preview — [planning/strategy-schemes.md#game-preview-no-games-played]. */
object RpiIndex {
    fun fromPrestige(programPrestige: Int, seasonWins: Int, seasonLosses: Int): Double {
        val resume = programPrestige.coerceIn(0, 99) / 100.0
        val played = seasonWins + seasonLosses
        if (played == 0) return resume
        val winPct = seasonWins.toDouble() / played
        return (resume * 0.55 + winPct * 0.45).coerceIn(0.0, 1.0)
    }

    fun fromOverview(overview: SchoolHubOverview): Double {
        val power = overview.powerIndex.coerceIn(0, 100) / 100.0
        val prestige = fromPrestige(
            overview.programPrestige,
            overview.record.wins,
            overview.record.losses,
        )
        return ((power + prestige) / 2.0).coerceIn(0.0, 1.0)
    }
}

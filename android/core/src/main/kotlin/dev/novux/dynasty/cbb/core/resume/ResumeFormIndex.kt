package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.league.LeagueGameResult

internal object ResumeFormIndex {
    fun formNormMap(
        teamIds: Collection<String>,
        programPrestige: Map<String, Int>,
        results: List<LeagueGameResult>,
    ): Map<String, Double> {
        val raw = teamIds.associateWith { formValue(it, programPrestige[it] ?: 50, results) }
        return ResumePercentileNorm.toHundred(raw)
    }

    fun formValue(teamId: String, prestige: Int, results: List<LeagueGameResult>): Double {
        val recent = results
            .filter { it.homeTeamId == teamId || it.awayTeamId == teamId }
            .sortedWith(compareByDescending<LeagueGameResult> { it.rsWeekIndex }.thenBy { it.homeTeamId })
            .take(10)
            .sortedWith(compareBy({ it.rsWeekIndex }, { it.homeTeamId }))
        if (recent.isEmpty()) return prestigeForm(prestige)
        var points = 0
        var opp = 0
        var poss = 0.0
        recent.forEach { game ->
            val p = estimatePossessions(game.homeScore, game.awayScore)
            if (game.homeTeamId == teamId) {
                points += game.homeScore
                opp += game.awayScore
            } else {
                points += game.awayScore
                opp += game.homeScore
            }
            poss += p
        }
        val l10Nem = if (poss <= 0.0) prestigeForm(prestige) else {
            val off = points / poss * 100.0
            val def = opp / poss * 100.0
            (off - def).coerceIn(-30.0, 30.0)
        }
        val season = seasonNemFromAll(teamId, results, prestige)
        return l10Nem * 0.6 + season * 0.4
    }

    private fun seasonNemFromAll(teamId: String, results: List<LeagueGameResult>, prestige: Int): Double {
        var points = 0
        var opp = 0
        var poss = 0.0
        results.filter { it.homeTeamId == teamId || it.awayTeamId == teamId }.forEach { game ->
            val p = estimatePossessions(game.homeScore, game.awayScore)
            if (game.homeTeamId == teamId) {
                points += game.homeScore
                opp += game.awayScore
            } else {
                points += game.awayScore
                opp += game.homeScore
            }
            poss += p
        }
        if (poss <= 0.0) return prestigeForm(prestige)
        val off = points / poss * 100.0
        val def = opp / poss * 100.0
        return (off - def).coerceIn(-30.0, 30.0)
    }

    private fun prestigeForm(prestige: Int): Double =
        ((prestige.coerceIn(0, 99) - 50) * 0.35).coerceIn(-30.0, 30.0)

    private fun estimatePossessions(homeScore: Int, awayScore: Int): Double {
        val total = homeScore + awayScore
        return (68.0 + (total - 144) * 0.08).coerceIn(58.0, 78.0)
    }
}

package dev.novux.dynasty.cbb.core.history

import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import kotlin.math.round

/** Season win contribution — [planning/simulation-engine.md#season-win-contribution-design-lock]. */
object WinContributionEngine {
    fun allocateTeamWins(
        players: List<RosterPlayer>,
        teamWins: Int,
        tuning: BalanceWinContributionTuning,
    ): Map<String, Double> {
        if (teamWins <= 0) return emptyMap()
        val eligible = players.filter { it.rsGamesPlayedSeason > 0 || it.isStarter }
        if (eligible.isEmpty()) return emptyMap()
        val minuteEstimates = eligible.associateWith { estimatedMinutes(it) }
        val teamMinutes = minuteEstimates.values.sum().coerceAtLeast(1)
        val productions = eligible.associateWith { productionProxy(it, tuning) }
        val teamAvg = productions.values.average().coerceAtLeast(1.0)
        val raw = eligible.associateWith { player ->
            val minuteShare = minuteEstimates.getValue(player).toDouble() / teamMinutes
            val prodIdx = (productions.getValue(player) / teamAvg)
                .coerceIn(tuning.productionIdxClampMin, tuning.productionIdxClampMax)
            minuteShare * (tuning.minuteBlend + tuning.productionBlend * prodIdx)
        }
        val sum = raw.values.sum().coerceAtLeast(1e-6)
        return raw.mapValues { (_, credit) ->
            round(teamWins * credit / sum * 100.0) / 100.0
        }.mapKeys { it.key.playerId }
    }

    private fun estimatedMinutes(player: RosterPlayer): Int {
        val mpg = if (player.isStarter) 28 else 14 - player.depthOrder.coerceAtMost(10)
        return (player.rsGamesPlayedSeason.coerceAtLeast(1) * mpg.coerceAtLeast(5))
    }

    private fun productionProxy(player: RosterPlayer, tuning: BalanceWinContributionTuning): Double {
        val games = player.rsGamesPlayedSeason.coerceAtLeast(1)
        val pts = player.ovr * 0.45 * games
        val ast = player.off * 0.08 * games
        val reb = player.def * 0.06 * games
        val to = games * 2.0
        return pts * tuning.ptsWeight + ast * tuning.astWeight + reb * tuning.rebWeight + to * tuning.toWeight
    }
}

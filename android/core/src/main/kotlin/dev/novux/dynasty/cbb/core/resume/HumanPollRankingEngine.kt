package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.balance.BalancePollWeights
import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.league.LeagueGameResult

/**
 * Media & Coaches polls — [planning/selection-engine.md#media-poll--coaches-poll-weekly].
 */
object HumanPollRankingEngine {
    fun compute(
        netTable: NcbaNetRankingTable,
        programPrestige: Map<String, Int>,
        results: List<LeagueGameResult>,
        priorMediaRanks: Map<String, Int>,
        priorCoachesRanks: Map<String, Int>,
        lastWeekResults: List<LeagueGameResult>,
        tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
    ): HumanPollRankingTable {
        val teams = netTable.byTeamId.keys
        val netMax = netTable.rows.maxOfOrNull { it.netScore }?.coerceAtLeast(1e-6) ?: 1.0
        val formNorm = ResumeFormIndex.formNormMap(teams, programPrestige, results)
        val netRanks = netTable.byTeamId.mapValues { it.value.netRank }

        val mediaScored = teams.map { teamId ->
            val netRow = netTable.byTeamId.getValue(teamId)
            val score = pollScore(
                weights = tuning.mediaPollWeights,
                netScore = netRow.netScore,
                netMax = netMax,
                prestige = programPrestige[teamId] ?: 50,
                inertia = pollInertia(priorMediaRanks[teamId], tuning),
                formNorm = formNorm.getValue(teamId),
                narrative = narrativeBoost(teamId, lastWeekResults, netRanks),
            )
            teamId to score
        }
        val coachesScored = teams.map { teamId ->
            val netRow = netTable.byTeamId.getValue(teamId)
            val score = pollScore(
                weights = tuning.coachesPollWeights,
                netScore = netRow.netScore,
                netMax = netMax,
                prestige = programPrestige[teamId] ?: 50,
                inertia = pollInertia(priorCoachesRanks[teamId], tuning),
                formNorm = formNorm.getValue(teamId),
                narrative = narrativeBoost(teamId, lastWeekResults, netRanks) * 0.85,
            )
            teamId to score
        }
        val mediaOrdered = rankTeams(mediaScored, netTable)
        val coachesOrdered = rankTeams(coachesScored, netTable)
        return HumanPollRankingTable(
            mediaRankByTeam = mediaOrdered.ranks,
            coachesRankByTeam = coachesOrdered.ranks,
            mediaTop25TeamIds = mediaOrdered.top25,
            coachesTop25TeamIds = coachesOrdered.top25,
        )
    }

    private data class RankedPoll(
        val ranks: Map<String, Int>,
        val top25: List<String>,
    )

    private fun rankTeams(
        scored: List<Pair<String, Double>>,
        netTable: NcbaNetRankingTable,
    ): RankedPoll {
        val ordered = scored
            .sortedWith(
                compareByDescending<Pair<String, Double>> { it.second }
                    .thenBy { netTable.byTeamId.getValue(it.first).netRank }
                    .thenBy { it.first },
            )
        val ranks = ordered.mapIndexed { index, (id, _) -> id to index + 1 }.toMap()
        val top25 = ordered.take(HumanPollRankingTable.TOP_25_CUTOFF).map { it.first }
        return RankedPoll(ranks, top25)
    }

    private fun pollScore(
        weights: BalancePollWeights,
        netScore: Double,
        netMax: Double,
        prestige: Int,
        inertia: Double,
        formNorm: Double,
        narrative: Double,
    ): Double {
        val netAnchor = netScore / netMax
        val prestigeTerm = prestige.coerceIn(0, 99) / 99.0 * 100.0
        return weights.netAnchor * netAnchor +
            weights.prestige * prestigeTerm +
            weights.inertia * inertia +
            weights.form * formNorm +
            weights.narrative * narrative.coerceIn(0.0, 2.0)
    }

    private fun pollInertia(priorRank: Int?, tuning: BalanceSelectionTuning): Double {
        val rank = priorRank ?: 183
        return (tuning.pollInertiaK * (rank - 183))
            .coerceIn(-tuning.pollInertiaCap, tuning.pollInertiaCap)
    }

    private fun narrativeBoost(
        teamId: String,
        weekGames: List<LeagueGameResult>,
        netRanks: Map<String, Int>,
    ): Double {
        if (weekGames.isEmpty()) return 0.0
        var boost = 0.0
        weekGames.forEach { game ->
            val homeWon = game.homeScore > game.awayScore
            val awayWon = game.awayScore > game.homeScore
            if (homeWon && game.homeTeamId == teamId) {
                boost += upsetWin(netRanks[teamId] ?: 200, netRanks[game.awayTeamId] ?: 200)
            } else if (awayWon && game.awayTeamId == teamId) {
                boost += upsetWin(netRanks[teamId] ?: 200, netRanks[game.homeTeamId] ?: 200)
            }
        }
        return boost.coerceIn(0.0, 2.0)
    }

    private fun upsetWin(winnerRank: Int, loserRank: Int): Double {
        if (winnerRank <= loserRank) return 0.2
        return ((winnerRank - loserRank) / 60.0).coerceIn(0.0, 1.5)
    }
}

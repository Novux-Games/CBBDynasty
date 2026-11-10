package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.balance.BalanceQuadCutoffs
import dev.novux.dynasty.cbb.core.balance.BalanceQuadPoints
import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.balance.BalanceSosVenueWeights
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import kotlin.math.roundToInt

/**
 * SOS, NEM, and quad record score from finalized results —
 * [planning/selection-engine.md#team-indices-inputs].
 */
object TeamResumeEngine {
    fun computeLeague(
        teamIds: Collection<String>,
        programPrestige: Map<String, Int>,
        results: List<LeagueGameResult>,
        tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
    ): Map<String, TeamResumeMetrics> {
        val teams = teamIds.toSet()
        val sortedGames = results
            .filter { it.homeTeamId in teams && it.awayTeamId in teams }
            .sortedWith(compareBy({ it.rsWeekIndex }, { it.homeTeamId }, { it.awayTeamId }))
        val running = teams.associateWith { EfficiencyAccumulator() }
        val sosNumerator = teams.associateWith { 0.0 }.toMutableMap()
        val sosWeight = teams.associateWith { 0.0 }.toMutableMap()
        val qrs = teams.associateWith { 0.0 }.toMutableMap()
        val gamesPlayed = teams.associateWith { 0 }.toMutableMap()

        sortedGames.forEach { game ->
            val poss = estimatePossessions(game.homeScore, game.awayScore)
            val homeAcc = running.getValue(game.homeTeamId)
            val awayAcc = running.getValue(game.awayTeamId)
            val homeTipNem = nemAtTip(homeAcc, programPrestige[game.homeTeamId] ?: 50)
            val awayTipNem = nemAtTip(awayAcc, programPrestige[game.awayTeamId] ?: 50)
            val ranks = netRankProxy(running, programPrestige, teams)
            val homeWon = game.homeScore > game.awayScore

            accumulateSos(
                game.homeTeamId,
                awayTipNem,
                venueWeight = tuning.sosVenueWeights.home,
                sosNumerator,
                sosWeight,
            )
            accumulateSos(
                game.awayTeamId,
                homeTipNem,
                venueWeight = tuning.sosVenueWeights.road,
                sosNumerator,
                sosWeight,
            )
            qrs[game.homeTeamId] = qrs.getValue(game.homeTeamId) +
                quadPoints(ranks.getValue(game.awayTeamId), homeWon, tuning.quadCutoffs, tuning.quadPoints)
            qrs[game.awayTeamId] = qrs.getValue(game.awayTeamId) +
                quadPoints(ranks.getValue(game.homeTeamId), !homeWon, tuning.quadCutoffs, tuning.quadPoints)

            homeAcc.add(game.homeScore, game.awayScore, poss)
            awayAcc.add(game.awayScore, game.homeScore, poss)
            gamesPlayed[game.homeTeamId] = gamesPlayed.getValue(game.homeTeamId) + 1
            gamesPlayed[game.awayTeamId] = gamesPlayed.getValue(game.awayTeamId) + 1
        }

        val rawSos = teams.associateWith { id ->
            val w = sosWeight.getValue(id)
            if (w <= 0.0) prestigeNem(programPrestige[id] ?: 50) else sosNumerator.getValue(id) / w
        }
        val sosRatings = percentileTo99(rawSos)

        return teams.associateWith { teamId ->
            val acc = running.getValue(teamId)
            TeamResumeMetrics(
                teamId = teamId,
                nem = seasonNem(acc, programPrestige[teamId] ?: 50),
                sosRating = sosRatings.getValue(teamId),
                quadRecordScore = qrs.getValue(teamId),
                gamesPlayed = gamesPlayed.getValue(teamId),
            )
        }
    }

    private data class EfficiencyAccumulator(
        var points: Int = 0,
        var oppPoints: Int = 0,
        var possessions: Double = 0.0,
    ) {
        fun add(scored: Int, allowed: Int, poss: Double) {
            points += scored
            oppPoints += allowed
            possessions += poss
        }
    }

    private fun estimatePossessions(homeScore: Int, awayScore: Int): Double {
        val total = homeScore + awayScore
        return (68.0 + (total - 144) * 0.08).coerceIn(58.0, 78.0)
    }

    private fun seasonNem(acc: EfficiencyAccumulator, prestige: Int): Double {
        if (acc.possessions <= 0.0) return prestigeNem(prestige)
        val off = acc.points / acc.possessions * 100.0
        val def = acc.oppPoints / acc.possessions * 100.0
        return (off - def).coerceIn(-30.0, 30.0)
    }

    private fun nemAtTip(acc: EfficiencyAccumulator, prestige: Int): Double {
        if (acc.possessions <= 0.0) return prestigeNem(prestige)
        val off = acc.points / acc.possessions * 100.0
        val def = acc.oppPoints / acc.possessions * 100.0
        return (off - def).coerceIn(-30.0, 30.0)
    }

    private fun prestigeNem(prestige: Int): Double =
        ((prestige.coerceIn(0, 99) - 50) * 0.35).coerceIn(-30.0, 30.0)

    private fun netRankProxy(
        running: Map<String, EfficiencyAccumulator>,
        prestige: Map<String, Int>,
        teams: Set<String>,
    ): Map<String, Int> {
        val ordered = teams
            .map { id -> id to nemAtTip(running.getValue(id), prestige[id] ?: 50) }
            .sortedWith(compareByDescending<Pair<String, Double>> { it.second }.thenBy { it.first })
        return ordered.mapIndexed { index, (id, _) -> id to index + 1 }.toMap()
    }

    private fun accumulateSos(
        teamId: String,
        opponentTipNem: Double,
        venueWeight: Double,
        numerator: MutableMap<String, Double>,
        weight: MutableMap<String, Double>,
    ) {
        numerator[teamId] = numerator.getValue(teamId) + opponentTipNem * venueWeight
        weight[teamId] = weight.getValue(teamId) + venueWeight
    }

    private fun quadBucket(rank: Int, cutoffs: BalanceQuadCutoffs): Int = when {
        rank <= cutoffs.q1Max -> 1
        rank <= cutoffs.q2Max -> 2
        rank <= cutoffs.q3Max -> 3
        else -> 4
    }

    private fun quadPoints(
        opponentRank: Int,
        won: Boolean,
        cutoffs: BalanceQuadCutoffs,
        points: BalanceQuadPoints,
    ): Double {
        val quad = quadBucket(opponentRank, cutoffs)
        return when (quad) {
            1 -> if (won) points.q1Win else points.q1Loss
            2 -> if (won) points.q2Win else points.q2Loss
            3 -> if (won) points.q3Win else points.q3Loss
            else -> if (won) points.q4Win else points.q4Loss
        }
    }

    private fun percentileTo99(raw: Map<String, Double>): Map<String, Int> {
        if (raw.isEmpty()) return emptyMap()
        val ordered = raw.entries.sortedBy { it.value }
        val n = ordered.size
        if (n == 1) return mapOf(ordered.single().key to 50)
        return ordered.mapIndexed { index, (id, _) ->
            val pct = index.toDouble() / (n - 1).coerceAtLeast(1)
            id to (pct * 99.0).roundToInt().coerceIn(0, 99)
        }.toMap()
    }
}

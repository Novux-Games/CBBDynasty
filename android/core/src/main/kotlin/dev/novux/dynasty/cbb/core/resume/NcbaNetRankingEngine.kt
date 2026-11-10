package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord

/**
 * Weekly NCBA NET for all D-I teams — [planning/selection-engine.md#ncba-net-rankings-weekly].
 */
object NcbaNetRankingEngine {
    fun compute(
        teamIds: Collection<String>,
        programPrestige: Map<String, Int>,
        records: Map<String, LeagueTeamRecord>,
        results: List<LeagueGameResult>,
        tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
        rankingsWeek: Int = 0,
    ): NcbaNetRankingTable {
        val teams = teamIds.toSet()
        val resume = TeamResumeEngine.computeLeague(teams, programPrestige, results, tuning)
        val formRaw = teams.associateWith { formValue(it, programPrestige[it] ?: 50, results) }
        val rqRaw = teams.associateWith { id ->
            recordQuality(
                resume.getValue(id).sosRating,
                records[id] ?: LeagueTeamRecord(teamId = id),
            )
        }
        val nemNorm = ResumePercentileNorm.toHundred(resume.mapValues { it.value.nem })
        val qrsNorm = ResumePercentileNorm.toHundred(resume.mapValues { it.value.quadRecordScore })
        val formNorm = ResumePercentileNorm.toHundred(formRaw)
        val rqNorm = ResumePercentileNorm.toHundred(rqRaw)

        val weights = tuning.netWeights
        val scored = teams.map { teamId ->
            val m = resume.getValue(teamId)
            val sosTerm = m.sosRating / 99.0
            val netScore =
                weights.nem * nemNorm.getValue(teamId) +
                    weights.sos * sosTerm +
                    weights.qrs * qrsNorm.getValue(teamId) +
                    weights.form * formNorm.getValue(teamId) +
                    weights.rq * rqNorm.getValue(teamId)
            teamId to NcbaNetTeamRow(
                teamId = teamId,
                netRank = 0,
                netScore = netScore,
                nem = m.nem,
                sosRating = m.sosRating,
                quadRecordScore = m.quadRecordScore,
            )
        }
        val ordered = scored
            .sortedWith(
                compareByDescending<Pair<String, NcbaNetTeamRow>> { it.second.netScore }
                    .thenByDescending { resume.getValue(it.first).nem }
                    .thenByDescending { resume.getValue(it.first).sosRating }
                    .thenBy { it.first },
            )
            .mapIndexed { index, (_, row) ->
                row.copy(netRank = index + 1)
            }
        return NcbaNetRankingTable(
            rows = ordered,
            byTeamId = ordered.associateBy { it.teamId },
            rankingsWeek = rankingsWeek,
        )
    }

    private fun recordQuality(sosRating: Int, record: LeagueTeamRecord): Double {
        val played = record.wins + record.losses
        val winPct = if (played == 0) 0.5 else record.wins.toDouble() / played
        return (winPct * 0.6 + (sosRating / 99.0) * 0.4) * 100.0
    }

    private fun formValue(teamId: String, prestige: Int, results: List<LeagueGameResult>): Double {
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

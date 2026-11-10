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
        val formRaw = teams.associateWith { ResumeFormIndex.formValue(it, programPrestige[it] ?: 50, results) }
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

}

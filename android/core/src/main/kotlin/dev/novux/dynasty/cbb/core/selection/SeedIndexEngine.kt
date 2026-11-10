package dev.novux.dynasty.cbb.core.selection

import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.resume.ResumeFormIndex
import dev.novux.dynasty.cbb.core.resume.ResumePercentileNorm
import dev.novux.dynasty.cbb.core.resume.TeamResumeEngine

/** NCBA seed merit index — [planning/selection-engine.md#seeds-152-ncba-main-bracket]. */
object SeedIndexEngine {
    fun compute(
        teamIds: Collection<String>,
        programPrestige: Map<String, Int>,
        records: Map<String, LeagueTeamRecord>,
        results: List<LeagueGameResult>,
        tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
    ): Map<String, Double> {
        val teams = teamIds.toSet()
        val resume = TeamResumeEngine.computeLeague(teams, programPrestige, results, tuning)
        val formRaw = teams.associateWith {
            ResumeFormIndex.formValue(it, programPrestige[it] ?: 50, results)
        }
        val rqRaw = teams.associateWith { id ->
            val rec = records[id] ?: LeagueTeamRecord(teamId = id)
            val played = rec.wins + rec.losses
            val winPct = if (played == 0) 0.5 else rec.wins.toDouble() / played
            val sos = resume.getValue(id).sosRating
            (winPct * 0.6 + (sos / 99.0) * 0.4) * 100.0
        }
        val nemNorm = ResumePercentileNorm.toHundred(resume.mapValues { it.value.nem })
        val qrsNorm = ResumePercentileNorm.toHundred(resume.mapValues { it.value.quadRecordScore })
        val formNorm = ResumePercentileNorm.toHundred(formRaw)
        val rqNorm = ResumePercentileNorm.toHundred(rqRaw)
        val weights = tuning.seedIndexWeights
        return teams.associateWith { teamId ->
            val m = resume.getValue(teamId)
            val sosTerm = m.sosRating / 99.0
            weights.nem * nemNorm.getValue(teamId) +
                weights.qrs * qrsNorm.getValue(teamId) +
                weights.sos * sosTerm +
                weights.form * formNorm.getValue(teamId) +
                weights.rq * rqNorm.getValue(teamId)
        }
    }
}

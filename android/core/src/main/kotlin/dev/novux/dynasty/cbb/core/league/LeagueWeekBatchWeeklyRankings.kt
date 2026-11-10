package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.resume.HumanPollRankingEngine
import dev.novux.dynasty.cbb.core.resume.NcbaNetRankingEngine

/** Weekly résumé refresh after league games commit ([planning/selection-engine.md]). */
object LeagueWeekBatchWeeklyRankings {
    fun attach(
        standings: LeagueStandingsState,
        programPrestige: Map<String, Int>,
        weekIndex: Int,
        tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
    ): LeagueStandingsState {
        if (standings.seasonResults.isEmpty()) return standings
        val table = NcbaNetRankingEngine.compute(
            teamIds = programPrestige.keys,
            programPrestige = programPrestige,
            records = standings.records,
            results = standings.seasonResults,
            tuning = tuning,
            rankingsWeek = weekIndex,
        )
        val polls = HumanPollRankingEngine.compute(
            netTable = table,
            programPrestige = programPrestige,
            results = standings.seasonResults,
            priorMediaRanks = standings.mediaPollRankByTeam,
            priorCoachesRanks = standings.coachesPollRankByTeam,
            lastWeekResults = standings.lastWeekResults,
            tuning = tuning,
        )
        return standings.copy(
            netRankByTeam = table.byTeamId.mapValues { it.value.netRank },
            mediaPollRankByTeam = polls.mediaRankByTeam,
            coachesPollRankByTeam = polls.coachesRankByTeam,
            netRankingsWeek = weekIndex,
        )
    }
}

package dev.novux.dynasty.cbb.data.school

import dev.novux.dynasty.cbb.core.balance.selectionTuning
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.resume.TeamResumeEngine
import dev.novux.dynasty.cbb.core.resume.TeamResumeMetrics
import dev.novux.dynasty.cbb.core.school.SchoolHubOverview
import dev.novux.dynasty.cbb.core.school.SchoolHubSnapshotFactory
import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class SchoolHubRepository(
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
    private val preseasonProgressRepository: PreseasonProgressRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val balanceRulesetRepository: BalanceRulesetRepository,
) {
    suspend fun loadOverview(
        dynastyId: String,
        teamId: String,
        seasonIndex: Int,
        record: SchoolSeasonRecord = SchoolSeasonRecord.Zero,
    ): SchoolHubOverview? {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        val index = packRepository.activeSession().index
        val team = index.team(teamId) ?: return null
        val progress = preseasonProgressRepository.load(dynastyId, seasonIndex)
        val rankingsPublished = progress.isComplete(PreseasonTaskId.RankingsReview)
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)
        val resume = standings?.let { resumeMetrics(it, teamId) }
        val netRankings = standings?.netRankByTeam?.takeIf { it.isNotEmpty() }
        val mediaPoll = standings?.mediaPollRankByTeam?.takeIf { it.isNotEmpty() }
        val coachesPoll = standings?.coachesPollRankByTeam?.takeIf { it.isNotEmpty() }
        return SchoolHubSnapshotFactory.build(
            team = team,
            packIndex = index,
            userTeamId = coach.userTeamId,
            record = record,
            rankingsPublished = rankingsPublished,
            resume = resume,
            netRankings = netRankings,
            mediaPollRankings = mediaPoll,
            coachesPollRankings = coachesPoll,
        )
    }

    private suspend fun resumeMetrics(
        standings: dev.novux.dynasty.cbb.core.league.LeagueStandingsState,
        teamId: String,
    ): TeamResumeMetrics? {
        if (standings.seasonResults.isEmpty()) return null
        val prestige = packRepository.activeSession().index.allTeamDisplays()
            .associate { it.teamId to it.programPrestige }
        val tuning = balanceRulesetRepository.activeRuleset().selectionTuning()
        return TeamResumeEngine.computeLeague(
            prestige.keys,
            prestige,
            standings.seasonResults,
            tuning,
        )[teamId]
    }

    suspend fun loadUserSchoolOverview(
        dynastyId: String,
        seasonIndex: Int,
        record: SchoolSeasonRecord = SchoolSeasonRecord.Zero,
    ): SchoolHubOverview? {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        return loadOverview(dynastyId, coach.userTeamId, seasonIndex, record)
    }

    suspend fun loadOpponentOverview(
        dynastyId: String,
        opponentTeamId: String,
        seasonIndex: Int,
    ): SchoolHubOverview? =
        loadOverview(dynastyId, opponentTeamId, seasonIndex)
}

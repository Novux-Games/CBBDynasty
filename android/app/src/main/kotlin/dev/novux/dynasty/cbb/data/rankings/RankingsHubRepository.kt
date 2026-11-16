package dev.novux.dynasty.cbb.data.rankings

import dev.novux.dynasty.cbb.core.news.PreseasonRankingsReviewNewsEngine
import dev.novux.dynasty.cbb.core.preseason.PreseasonRankingsReviewUxPolicy
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.rankings.RankingsHubPublishPolicy
import dev.novux.dynasty.cbb.core.rankings.RankingsHubSnapshot
import dev.novux.dynasty.cbb.core.rankings.RankingsHubSnapshotFactory
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class RankingsHubRepository(
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val preseasonProgressRepository: PreseasonProgressRepository,
    private val newsFeedRepository: NewsFeedRepository,
    private val database: DynastyDatabase,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): RankingsHubSnapshot {
        val index = packRepository.activeSession().index
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)
        val preseasonComplete = preseasonProgressRepository
            .load(dynastyId, seasonIndex)
            .isComplete(PreseasonTaskId.RankingsReview)
        val published = RankingsHubPublishPolicy.rankingsPublished(preseasonComplete, standings)
        return RankingsHubSnapshotFactory.build(index, standings, published)
    }

    suspend fun publishPreseasonRankings(dynastyId: String, seasonIndex: Int): RankingsHubSnapshot {
        preseasonProgressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.RankingsReview)
        val snapshot = load(dynastyId, seasonIndex)
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return snapshot
        val schoolName = packRepository.activeSession().index.team(coach.userTeamId)?.displayName ?: coach.userTeamId
        val ranks = PreseasonRankingsReviewUxPolicy.userProgramRanks(
            coach.userTeamId,
            snapshot.net,
            snapshot.media,
            snapshot.coaches,
        )
        val headline = PreseasonRankingsReviewNewsEngine.headlineAfterPublication(
            seasonIndex = seasonIndex,
            schoolName = schoolName,
            netRank = ranks?.netRank,
            mediaRank = ranks?.mediaRank,
        )
        newsFeedRepository.appendHeadlines(dynastyId, seasonIndex, listOf(headline))
        return snapshot
    }
}

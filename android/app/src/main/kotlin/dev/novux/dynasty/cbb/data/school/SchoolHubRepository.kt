package dev.novux.dynasty.cbb.data.school

import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.school.SchoolHubOverview
import dev.novux.dynasty.cbb.core.school.SchoolHubSnapshotFactory
import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class SchoolHubRepository(
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
    private val preseasonProgressRepository: PreseasonProgressRepository,
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
        return SchoolHubSnapshotFactory.build(
            team = team,
            packIndex = index,
            userTeamId = coach.userTeamId,
            record = record,
            rankingsPublished = rankingsPublished,
        )
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

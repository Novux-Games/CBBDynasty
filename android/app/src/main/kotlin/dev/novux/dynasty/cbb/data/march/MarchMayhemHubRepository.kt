package dev.novux.dynasty.cbb.data.march

import dev.novux.dynasty.cbb.core.march.MarchMayhemHubSnapshot
import dev.novux.dynasty.cbb.core.march.MarchMayhemHubSnapshotFactory
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase

class MarchMayhemHubRepository(
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val database: DynastyDatabase,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): MarchMayhemHubSnapshot {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId)
        val userTeamId = coach?.userTeamId.orEmpty()
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)
        val index = packRepository.activeSession().index
        return MarchMayhemHubSnapshotFactory.build(index, standings, userTeamId)
    }
}

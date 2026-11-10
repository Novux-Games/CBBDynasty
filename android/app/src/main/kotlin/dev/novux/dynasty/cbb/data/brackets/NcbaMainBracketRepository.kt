package dev.novux.dynasty.cbb.data.brackets

import dev.novux.dynasty.cbb.core.brackets.NcbaMainBracketEngine
import dev.novux.dynasty.cbb.core.brackets.NcbaMainBracketSnapshot
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository

class NcbaMainBracketRepository(
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): NcbaMainBracketSnapshot? =
        leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)?.ncbaMainBracket

    suspend fun buildAndPersist(dynastyId: String, seasonIndex: Int): NcbaMainBracketSnapshot? {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null
        val selection = standings.selectionSunday ?: return null
        val opening = standings.ncbaOpeningRound ?: return null
        if (!opening.completed) return null
        val index = packRepository.activeSession().index
        val conferences = index.allTeamIds().associateWith { id ->
            index.team(id)?.conferenceId ?: "unknown"
        }
        val built = NcbaMainBracketEngine.build(selection, opening, conferences)
        leagueWeekBatchRepository.persistStandings(dynastyId, standings.copy(ncbaMainBracket = built))
        return built
    }

    suspend fun simulateAndPersist(dynastyId: String, seasonIndex: Int, seed: Long): NcbaMainBracketSnapshot? {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null
        val snapshot = standings.ncbaMainBracket
            ?: buildAndPersist(dynastyId, seasonIndex)
            ?: return null
        val fresh = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null
        val done = NcbaMainBracketEngine.simulate(snapshot, seed)
        leagueWeekBatchRepository.persistStandings(dynastyId, fresh.copy(ncbaMainBracket = done))
        return done
    }
}

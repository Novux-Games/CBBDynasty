package dev.novux.dynasty.cbb.data.brackets

import dev.novux.dynasty.cbb.core.brackets.NcbaOpeningRoundEngine
import dev.novux.dynasty.cbb.core.brackets.NcbaOpeningRoundSnapshot
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.selection.SelectionSundayRepository

class NcbaOpeningRoundRepository(
    private val selectionSundayRepository: SelectionSundayRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): NcbaOpeningRoundSnapshot? =
        leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)?.ncbaOpeningRound

    suspend fun planFromPersistedSelection(dynastyId: String, seasonIndex: Int): NcbaOpeningRoundSnapshot? {
        val selection = selectionSundayRepository.load(dynastyId, seasonIndex) ?: return null
        return NcbaOpeningRoundEngine.planFromSelection(selection)
    }

    suspend fun simulateAndPersist(dynastyId: String, seasonIndex: Int, seed: Long): NcbaOpeningRoundSnapshot? {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null
        val selection = standings.selectionSunday ?: return null
        val plan = standings.ncbaOpeningRound ?: NcbaOpeningRoundEngine.planFromSelection(selection)
        val done = NcbaOpeningRoundEngine.simulate(plan, seed)
        leagueWeekBatchRepository.persistStandings(dynastyId, standings.copy(ncbaOpeningRound = done))
        return done
    }
}

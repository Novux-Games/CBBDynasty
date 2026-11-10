package dev.novux.dynasty.cbb.data.brackets

import dev.novux.dynasty.cbb.core.brackets.NationalBracketSnapshot
import dev.novux.dynasty.cbb.core.brackets.NciHeritageBracketEngine
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository

class ParallelNationalBracketRepository(
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
) {
    suspend fun loadNci(dynastyId: String, seasonIndex: Int): NationalBracketSnapshot? =
        leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)?.nciBracket

    suspend fun loadHeritage(dynastyId: String, seasonIndex: Int): NationalBracketSnapshot? =
        leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex)?.heritageBracket

    suspend fun planFromSelection(dynastyId: String, seasonIndex: Int): Pair<NationalBracketSnapshot?, NationalBracketSnapshot?> {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null to null
        val selection = standings.selectionSunday ?: return null to null
        val nci = NciHeritageBracketEngine.planNci(selection)
        val heritage = NciHeritageBracketEngine.planHeritage(selection)
        leagueWeekBatchRepository.persistStandings(
            dynastyId,
            standings.copy(nciBracket = nci, heritageBracket = heritage),
        )
        return nci to heritage
    }

    suspend fun simulateAndPersist(dynastyId: String, seasonIndex: Int, seed: Long): Pair<NationalBracketSnapshot?, NationalBracketSnapshot?> {
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return null to null
        val selection = standings.selectionSunday ?: return null to null
        val nciPlan = standings.nciBracket ?: NciHeritageBracketEngine.planNci(selection)
        val heritagePlan = standings.heritageBracket ?: NciHeritageBracketEngine.planHeritage(selection)
        val nciDone = NciHeritageBracketEngine.simulateNci(nciPlan, seed)
        val heritageDone = NciHeritageBracketEngine.simulateHeritage(heritagePlan, seed)
        leagueWeekBatchRepository.persistStandings(
            dynastyId,
            standings.copy(nciBracket = nciDone, heritageBracket = heritageDone),
        )
        return nciDone to heritageDone
    }
}

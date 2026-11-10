package dev.novux.dynasty.cbb.core.selection

import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord

/** Selection Sunday field + seeds — [planning/selection-engine.md#selection-sunday--who-gets-in]. */
object SelectionSundayEngine {
    private const val AUTO_BIDS = 32
    private const val DIRECT_AT_LARGE = 20
    private const val OPENING_AT_LARGE = 24
    private const val NCI_SIZE = 32
    private const val HERITAGE_SIZE = 16

    fun run(input: SelectionSundayInput): SelectionSundaySnapshot {
        val seedIndex = SeedIndexEngine.compute(
            input.netRankByTeam.keys,
            input.programPrestige,
            input.records,
            input.results,
            input.tuning,
        )
        val autoBids = input.conferenceChampions
            .filter { input.postseasonEligible[it] == true }
            .toSet()
        val eligibleNonChamps = input.netRankByTeam.keys
            .filter { input.postseasonEligible[it] == true && it !in autoBids }
            .sortedBy { input.netRankByTeam.getValue(it) }
        val direct = eligibleNonChamps.take(DIRECT_AT_LARGE).toSet()
        val opening = eligibleNonChamps.drop(DIRECT_AT_LARGE).take(OPENING_AT_LARGE).toSet()
        val ncbaIds = autoBids + direct + opening
        val mainPool = autoBids + direct
        val mainSeeds = assignSeeds(mainPool, seedIndex, startSeed = 1)
        val openingSeeds = assignSeeds(opening, seedIndex, startSeed = 53)
        val ncbaField = ncbaIds.map { teamId ->
            val bid = when {
                teamId in autoBids -> NcbaBidType.ConferenceChampion
                teamId in direct -> NcbaBidType.DirectAtLarge
                else -> NcbaBidType.OpeningRound
            }
            NcbaSelectionEntry(
                teamId = teamId,
                bidType = bid,
                netRank = input.netRankByTeam.getValue(teamId),
                seedIndex = seedIndex.getValue(teamId),
                ncbaSeed = mainSeeds[teamId] ?: openingSeeds.getValue(teamId),
            )
        }.sortedBy { it.ncbaSeed }
        val ncbaSet = ncbaIds.toSet()
        val nciPool = input.netRankByTeam.keys
            .filter { input.postseasonEligible[it] == true && it !in ncbaSet }
            .sortedBy { input.netRankByTeam.getValue(it) }
            .take(NCI_SIZE)
        val nciField = assignTournament(nciPool, seedIndex, tournamentId = "NCI")
        val heritagePool = input.netRankByTeam.keys
            .filter {
                input.heritageEligible[it] == true &&
                    it !in ncbaSet &&
                    it !in nciPool.toSet()
            }
            .sortedBy { input.netRankByTeam.getValue(it) }
            .take(HERITAGE_SIZE)
        val heritageField = assignTournament(heritagePool, seedIndex, tournamentId = "HC")
        return SelectionSundaySnapshot(
            seasonIndex = input.seasonIndex,
            ncbaField = ncbaField,
            nciField = nciField,
            heritageField = heritageField,
        )
    }

    private fun assignSeeds(
        teamIds: Set<String>,
        seedIndex: Map<String, Double>,
        startSeed: Int,
    ): Map<String, Int> =
        teamIds
            .sortedWith(
                compareByDescending<String> { seedIndex[it] ?: 0.0 }
                    .thenBy { it },
            )
            .mapIndexed { index, teamId -> teamId to startSeed + index }
            .toMap()

    private fun assignTournament(
        teamIds: List<String>,
        seedIndex: Map<String, Double>,
        tournamentId: String,
    ): List<NationalTournamentInvite> =
        teamIds
            .sortedWith(
                compareByDescending<String> { seedIndex[it] ?: 0.0 }
                    .thenBy { it },
            )
            .mapIndexed { index, teamId ->
                NationalTournamentInvite(
                    teamId = teamId,
                    tournamentId = tournamentId,
                    seed = index + 1,
                )
            }
}

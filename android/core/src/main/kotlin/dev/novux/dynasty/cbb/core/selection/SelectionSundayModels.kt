package dev.novux.dynasty.cbb.core.selection

import dev.novux.dynasty.cbb.core.balance.BalanceSelectionTuning
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import kotlinx.serialization.Serializable

@Serializable
enum class NcbaBidType {
    ConferenceChampion,
    DirectAtLarge,
    OpeningRound,
}

@Serializable
data class NcbaSelectionEntry(
    val teamId: String,
    val bidType: NcbaBidType,
    val netRank: Int,
    val seedIndex: Double,
    val ncbaSeed: Int,
)

@Serializable
data class NationalTournamentInvite(
    val teamId: String,
    val tournamentId: String,
    val seed: Int,
)

@Serializable
data class SelectionSundaySnapshot(
    val seasonIndex: Int,
    val ncbaField: List<NcbaSelectionEntry>,
    val nciField: List<NationalTournamentInvite>,
    val heritageField: List<NationalTournamentInvite>,
) {
    val ncbaMainBracketTeamIds: Set<String>
        get() = ncbaField
            .filter { it.bidType != NcbaBidType.OpeningRound }
            .map { it.teamId }
            .toSet()
}

data class SelectionSundayInput(
    val seasonIndex: Int,
    val netRankByTeam: Map<String, Int>,
    val postseasonEligible: Map<String, Boolean>,
    val heritageEligible: Map<String, Boolean>,
    val conferenceChampions: Set<String>,
    val programPrestige: Map<String, Int>,
    val records: Map<String, LeagueTeamRecord>,
    val results: List<LeagueGameResult>,
    val tuning: BalanceSelectionTuning = BalanceSelectionTuning(),
)

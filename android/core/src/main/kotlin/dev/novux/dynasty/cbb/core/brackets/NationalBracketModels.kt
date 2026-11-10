package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.selection.NationalTournamentInvite
import kotlinx.serialization.Serializable

@Serializable
data class NationalBracketSnapshot(
    val tournamentId: String,
    val seasonIndex: Int,
    val invites: List<NationalTournamentInvite>,
    val gameResults: List<LeagueGameResult> = emptyList(),
    val championTeamId: String? = null,
    val completed: Boolean = false,
)

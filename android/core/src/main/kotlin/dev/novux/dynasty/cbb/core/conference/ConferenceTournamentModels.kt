package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import kotlinx.serialization.Serializable

data class ConferenceTournamentInput(
    val conferenceId: String,
    val template: TournamentTemplatePack,
    val teamIds: List<String>,
    val records: Map<String, LeagueTeamRecord>,
    val postseasonEligible: Map<String, Boolean> = emptyMap(),
    val seasonIndex: Int,
    val baseWeekIndex: Int = 1,
    val seed: Long,
)

@Serializable
data class ConferenceTournamentResult(
    val conferenceId: String,
    val templateId: String,
    val qualifiedTeamIds: List<String>,
    val championTeamId: String,
    val runnerUpTeamId: String?,
    val finishOrder: List<String> = emptyList(),
    val ncbaAutoBidTeamId: String,
    val gameResults: List<LeagueGameResult>,
)

@Serializable
data class ConferenceTournamentSeason(
    val seasonIndex: Int,
    val resultsByConference: Map<String, ConferenceTournamentResult> = emptyMap(),
) {
    fun champion(conferenceId: String): String? = resultsByConference[conferenceId]?.championTeamId
}

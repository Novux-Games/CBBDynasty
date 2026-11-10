package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import kotlinx.serialization.Serializable

@Serializable
data class NcbaOpeningRoundMatchup(
    val homeTeamId: String,
    val awayTeamId: String,
    /** NET order within the 24-team bubble (1 = best). */
    val bubbleRankHome: Int,
    val bubbleRankAway: Int,
    val ncbaSeedHome: Int,
    val ncbaSeedAway: Int,
)

@Serializable
data class NcbaOpeningRoundSnapshot(
    val seasonIndex: Int,
    val matchups: List<NcbaOpeningRoundMatchup>,
    val gameResults: List<LeagueGameResult> = emptyList(),
    /** Winners in game order — each keeps [Selection Sunday] `ncbaSeed` when advancing. */
    val winnerTeamIds: List<String> = emptyList(),
    val completed: Boolean = false,
) {
    val advancingTeamIds: Set<String>
        get() = winnerTeamIds.toSet()
}

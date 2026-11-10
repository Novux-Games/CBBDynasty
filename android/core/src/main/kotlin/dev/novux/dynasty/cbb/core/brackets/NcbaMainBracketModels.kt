package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import kotlinx.serialization.Serializable

@Serializable
data class NcbaMainBracketEntrant(
    val teamId: String,
    val ncbaSeed: Int,
    val regionId: NcbaRegionId,
    /** 1 = best overall seed within the region. */
    val regionalSeed: Int,
)

@Serializable
data class NcbaMainBracketSnapshot(
    val seasonIndex: Int,
    val entrants: List<NcbaMainBracketEntrant>,
    val gameResults: List<LeagueGameResult> = emptyList(),
    val regionalChampions: Map<NcbaRegionId, String> = emptyMap(),
    val championTeamId: String? = null,
    val completed: Boolean = false,
)

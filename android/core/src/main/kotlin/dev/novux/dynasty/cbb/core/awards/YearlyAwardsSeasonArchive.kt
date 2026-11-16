package dev.novux.dynasty.cbb.core.awards

import kotlinx.serialization.Serializable

@Serializable
data class YearlyAwardsSeasonArchive(
    val seasonIndex: Int,
    val ncbaOverview: List<AwardHonorLine> = emptyList(),
    val ncbaMvp: List<AwardHonorLine> = emptyList(),
    val ncbaCoaches: List<AwardHonorLine> = emptyList(),
    val ncbaByPosition: Map<String, List<AwardHonorLine>> = emptyMap(),
    val conferenceAwards: Map<String, List<AwardHonorLine>> = emptyMap(),
    val ncbaAllAmerica: List<AwardHonorLine> = emptyList(),
    val ncbaMvpRunners: List<AwardHonorLine> = emptyList(),
    val userTeamMvp: AwardHonorLine? = null,
)

package dev.novux.dynasty.cbb.core.resume

data class NcbaNetTeamRow(
    val teamId: String,
    val netRank: Int,
    val netScore: Double,
    val nem: Double,
    val sosRating: Int,
    val quadRecordScore: Double,
)

data class NcbaNetRankingTable(
    val rows: List<NcbaNetTeamRow>,
    val byTeamId: Map<String, NcbaNetTeamRow>,
    val rankingsWeek: Int = 0,
)

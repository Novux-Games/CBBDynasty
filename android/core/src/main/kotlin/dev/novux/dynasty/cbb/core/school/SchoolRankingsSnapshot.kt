package dev.novux.dynasty.cbb.core.school

/** NCBA NET / polls — [planning/game-frame.md#school-hub]. */
data class SchoolRankingsSnapshot(
    val netRank: Int?,
    val mediaPollRank: Int?,
    val coachesPollRank: Int?,
    val mediaTop25: Boolean = false,
    val coachesTop25: Boolean = false,
    val conferenceStandingLabel: String?,
) {
    val rankingsPublished: Boolean = netRank != null
}

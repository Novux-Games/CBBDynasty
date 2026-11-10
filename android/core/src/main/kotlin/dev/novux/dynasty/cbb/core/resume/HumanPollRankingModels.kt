package dev.novux.dynasty.cbb.core.resume

data class HumanPollRankingTable(
    val mediaRankByTeam: Map<String, Int>,
    val coachesRankByTeam: Map<String, Int>,
    val mediaTop25TeamIds: List<String>,
    val coachesTop25TeamIds: List<String>,
) {
    fun mediaTop25(teamId: String): Boolean = mediaRankByTeam[teamId]?.let { it <= TOP_25_CUTOFF } == true

    fun coachesTop25(teamId: String): Boolean = coachesRankByTeam[teamId]?.let { it <= TOP_25_CUTOFF } == true

    companion object {
        const val TOP_25_CUTOFF = 25
    }
}

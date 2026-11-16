package dev.novux.dynasty.cbb.core.awards

import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex

/** All-America 1st/2nd/3rd teams — 45 players ([MEGA-POST-071], [MEGA-POST-206]). */
object AllAmericaSelectionEngine {
    const val MEGA_ID: String = "MEGA-POST-071"
    const val UI_ID: String = "MEGA-POST-206"

    private val tiers = listOf("1st Team", "2nd Team", "3rd Team")
    private val positionsPerTeam = listOf("PG", "SG", "SF", "PF", "C")

    fun buildLines(
        packIndex: IdentityPackIndex,
        winSharesByPlayer: Map<String, Double>,
        topTeamIds: List<String>,
    ): List<AwardHonorLine> {
        val rankedTeams = topTeamIds.take(15)
        val lines = mutableListOf<AwardHonorLine>()
        var teamCursor = 0
        for (tier in tiers) {
            for (pos in positionsPerTeam) {
                val teamId = rankedTeams.getOrElse(teamCursor % rankedTeams.size) { topTeamIds.first() }
                teamCursor++
                val school = packIndex.team(teamId)?.displayName ?: teamId
                val playerName = AwardPlaceholderNames.leadingScorer("$teamId-$pos", school)
                lines.add(
                    AwardHonorLine(
                        awardLabel = "All-America $tier — $pos",
                        winnerName = playerName,
                        schoolName = school,
                    ),
                )
            }
        }
        return lines.take(45)
    }
}
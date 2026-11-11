package dev.novux.dynasty.cbb.core.player.stats

import kotlinx.serialization.Serializable

@Serializable
data class PlayerCountingStats(
    val points: Int = 0,
    val assists: Int = 0,
    val rebounds: Int = 0,
    val steals: Int = 0,
    val blocks: Int = 0,
    val turnovers: Int = 0,
    val fieldGoalsMade: Int = 0,
    val fieldGoalsAttempted: Int = 0,
    val threePointsMade: Int = 0,
    val threePointsAttempted: Int = 0,
    val freeThrowsMade: Int = 0,
    val freeThrowsAttempted: Int = 0,
) {
    operator fun plus(other: PlayerCountingStats): PlayerCountingStats = PlayerCountingStats(
        points = points + other.points,
        assists = assists + other.assists,
        rebounds = rebounds + other.rebounds,
        steals = steals + other.steals,
        blocks = blocks + other.blocks,
        turnovers = turnovers + other.turnovers,
        fieldGoalsMade = fieldGoalsMade + other.fieldGoalsMade,
        fieldGoalsAttempted = fieldGoalsAttempted + other.fieldGoalsAttempted,
        threePointsMade = threePointsMade + other.threePointsMade,
        threePointsAttempted = threePointsAttempted + other.threePointsAttempted,
        freeThrowsMade = freeThrowsMade + other.freeThrowsMade,
        freeThrowsAttempted = freeThrowsAttempted + other.freeThrowsAttempted,
    )

    companion object {
        val ZERO = PlayerCountingStats()
    }
}

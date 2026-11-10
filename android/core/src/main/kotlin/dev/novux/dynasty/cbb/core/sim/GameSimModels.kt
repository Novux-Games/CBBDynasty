package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning

/** Regulation length — [planning/simulation-engine.md#possession-loop--clock]. */
object GameSimClock {
    const val REGULATION_SECONDS: Int = 40 * 60
    const val HALF_SECONDS: Int = 20 * 60
    const val MIN_TRIP_SECONDS: Int = 3
    const val MAX_TRIP_SECONDS: Int = 30
}

data class GameSimTeamInput(
    val teamId: String,
    val offensiveRating: Int,
    val defensiveRating: Int,
)

data class GameSimInput(
    val home: GameSimTeamInput,
    val away: GameSimTeamInput,
    val tuning: SimTuning,
    val seed: Long,
)

data class TeamBoxScore(
    val teamId: String,
    val points: Int = 0,
    val fieldGoalsMade: Int = 0,
    val fieldGoalsAttempted: Int = 0,
    val threePointsMade: Int = 0,
    val threePointsAttempted: Int = 0,
    val turnovers: Int = 0,
    val possessions: Int = 0,
)

data class GameSimResult(
    val homeScore: Int,
    val awayScore: Int,
    val homeBox: TeamBoxScore,
    val awayBox: TeamBoxScore,
    val totalPossessions: Int,
    val possessionLog: List<String>,
)

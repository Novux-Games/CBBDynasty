package dev.novux.dynasty.cbb.core.sim

/** Post-run sanity checks ([planning/simulation-engine.md#statistical-coherence]). */
object BoxScoreCoherence {
    fun assertValid(result: GameSimResult) {
        require(result.homeScore == result.homeBox.points) {
            "home score ${result.homeScore} != box ${result.homeBox.points}"
        }
        require(result.awayScore == result.awayBox.points) {
            "away score ${result.awayScore} != box ${result.awayBox.points}"
        }
        require(result.homeBox.fieldGoalsMade <= result.homeBox.fieldGoalsAttempted)
        require(result.awayBox.fieldGoalsMade <= result.awayBox.fieldGoalsAttempted)
        require(result.homeBox.threePointsMade <= result.homeBox.threePointsAttempted)
        require(result.awayBox.threePointsMade <= result.awayBox.threePointsAttempted)
        require(result.homeBox.threePointsMade <= result.homeBox.fieldGoalsMade)
        require(result.awayBox.threePointsMade <= result.awayBox.fieldGoalsMade)
        require(result.homeBox.possessions > 0 && result.awayBox.possessions > 0)
    }
}

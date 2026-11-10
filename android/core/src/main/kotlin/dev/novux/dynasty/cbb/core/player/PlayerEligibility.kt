package dev.novux.dynasty.cbb.core.player

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear

data class PlayerEligibility(
    val yearsUsed: Int,
    val yearsRemaining: Int,
    val medicalRedshirtSeason: Boolean = false,
) {
    init {
        require(yearsUsed >= 0)
        require(yearsRemaining >= 0)
    }

    companion object {
        fun forClassYear(classYear: PlayerClassYear): PlayerEligibility = when (classYear) {
            PlayerClassYear.Freshman -> PlayerEligibility(yearsUsed = 0, yearsRemaining = 4)
            PlayerClassYear.Sophomore -> PlayerEligibility(yearsUsed = 1, yearsRemaining = 3)
            PlayerClassYear.Junior -> PlayerEligibility(yearsUsed = 2, yearsRemaining = 2)
            PlayerClassYear.Senior -> PlayerEligibility(yearsUsed = 3, yearsRemaining = 1)
            PlayerClassYear.WalkOn -> PlayerEligibility(yearsUsed = 0, yearsRemaining = 4)
        }
    }
}

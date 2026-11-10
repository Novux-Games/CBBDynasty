package dev.novux.dynasty.cbb.core.coach.employment

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import kotlin.random.Random

object JobCenterEngine {
    const val OFFERS_PER_WINDOW: Int = 6

    fun generateOffers(
        teams: List<TeamDisplayOverlay>,
        seasonIndex: Int,
        dynastyId: String,
        coachReputation: Int = 55,
    ): List<JobOfferCard> {
        val prestigeCap = (coachReputation + 25).coerceIn(40, 99)
        val pool = teams.filter { it.programPrestige <= prestigeCap && it.programPrestige >= prestigeCap - 35 }
            .ifEmpty { teams.sortedByDescending { it.programPrestige }.take(OFFERS_PER_WINDOW) }
        val seed = dynastyId.hashCode() + seasonIndex * 997
        val shuffled = pool.shuffled(Random(seed))
        return shuffled.take(OFFERS_PER_WINDOW).map {
            JobOfferCard(
                teamId = it.teamId,
                displayName = it.displayName,
                conferenceId = it.conferenceId,
                programPrestige = it.programPrestige,
            )
        }
    }
}

object CoachFiringEngine {
    fun shouldEnterUnemployed(jobSecurityScore: Int, seasonIndex: Int, dynastyId: String): Boolean {
        if (jobSecurityScore > 25) return false
        val roll = (dynastyId.hashCode() + seasonIndex * 131).mod(100)
        return roll < 35
    }
}

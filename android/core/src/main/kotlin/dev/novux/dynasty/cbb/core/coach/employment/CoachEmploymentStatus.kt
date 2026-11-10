package dev.novux.dynasty.cbb.core.coach.employment

import kotlinx.serialization.Serializable

@Serializable
enum class CoachEmploymentStatus {
    Employed,
    Unemployed,
}

@Serializable
data class JobOfferCard(
    val teamId: String,
    val displayName: String,
    val conferenceId: String,
    val programPrestige: Int,
)

@Serializable
data class CoachEmploymentState(
    val status: CoachEmploymentStatus = CoachEmploymentStatus.Employed,
    val offersSeasonIndex: Int = 0,
    val pendingOffers: List<JobOfferCard> = emptyList(),
)

package dev.novux.dynasty.cbb.core.awards

import kotlinx.serialization.Serializable

@Serializable
data class AwardHonorLine(
    val awardLabel: String,
    val winnerName: String,
    val schoolName: String,
    val runnerUpName: String? = null,
)

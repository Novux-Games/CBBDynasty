package dev.novux.dynasty.cbb.core.coach.card

import dev.novux.dynasty.cbb.core.coach.CoachArchetype

data class HeadCoachCard(
    val dynastyId: String,
    val displayName: String,
    val age: Int,
    val schoolDisplayName: String,
    val seasonRecordLabel: String,
    val archetype: CoachArchetype,
    val offensiveSchemeLabel: String,
    val defensiveSchemeLabel: String,
    val contractYearsLeft: Int,
    val contractLengthYears: Int,
    val ratings: HeadCoachRatings,
    val almaMaterLabel: String?,
)

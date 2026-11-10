package dev.novux.dynasty.cbb.core.coach.card

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.CoachNameSuffix
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset

object HeadCoachCardFactory {
    fun ratingsFor(archetype: CoachArchetype): HeadCoachRatings = when (archetype) {
        CoachArchetype.Recruiter -> HeadCoachRatings(72, 70, 68, 88)
        CoachArchetype.SchemeManager -> HeadCoachRatings(86, 88, 70, 72)
        CoachArchetype.Developer -> HeadCoachRatings(74, 72, 90, 70)
        CoachArchetype.ProgramBuilder -> HeadCoachRatings(76, 76, 78, 82)
        CoachArchetype.Balanced -> HeadCoachRatings(78, 78, 78, 78)
    }

    fun build(
        dynastyId: String,
        displayName: String,
        age: Int,
        schoolDisplayName: String,
        archetype: CoachArchetype,
        offensiveScheme: OffensiveSchemePreset,
        defensiveScheme: DefensiveSchemePreset,
        almaMaterLabel: String?,
        contractLengthYears: Int = 4,
        contractYearsLeft: Int = 4,
        seasonRecordLabel: String = "0-0",
    ): HeadCoachCard = HeadCoachCard(
        dynastyId = dynastyId,
        displayName = displayName,
        age = age,
        schoolDisplayName = schoolDisplayName,
        seasonRecordLabel = seasonRecordLabel,
        archetype = archetype,
        offensiveSchemeLabel = offensiveScheme.label,
        defensiveSchemeLabel = defensiveScheme.label,
        contractYearsLeft = contractYearsLeft,
        contractLengthYears = contractLengthYears,
        ratings = ratingsFor(archetype),
        almaMaterLabel = almaMaterLabel,
    )

    fun displayName(
        firstName: String,
        lastName: String,
        suffixWire: String?,
    ): String {
        val suffix = suffixWire?.let { CoachNameSuffix.fromWire(it) }?.displayToken?.let { "$it " } ?: ""
        return "$firstName $suffix$lastName".trim().replace("  ", " ")
    }
}

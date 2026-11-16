package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset

/** Structured sign-step recap lines ([MEGA-COACH-027]). */
object NewGameSignRecapPolicy {
    const val MEGA_ID = "MEGA-COACH-027"

    fun recapLines(
        archetype: CoachArchetype,
        offensive: OffensiveSchemePreset,
        defensive: DefensiveSchemePreset,
        schoolDisplayName: String,
    ): List<String> = listOf(
        "Archetype: ${archetype.label}",
        "Offense: ${offensive.label}",
        "Defense: ${defensive.label}",
        "School: $schoolDisplayName",
    )
}
package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.CoachCreateProfile
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset

/** Wizard draft held in memory until sign contract. */
data class NewGameDraft(
    val coach: CoachCreateProfile = CoachCreateProfile(),
    val archetype: CoachArchetype = CoachArchetype.Balanced,
    val offensiveScheme: OffensiveSchemePreset = OffensiveSchemePreset.Balanced,
    val defensiveScheme: DefensiveSchemePreset = DefensiveSchemePreset.ManToMan,
    val dynastySettings: NewGameDynastySettings = NewGameDynastySettings(),
    val unlockAllSchools: Boolean = false,
    val selectedTeamId: String? = null,
    val schoolSummaryAcknowledged: Boolean = false,
)

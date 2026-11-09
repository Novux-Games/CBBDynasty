package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.runtime.Immutable
import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.CoachCreateProfile
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.save.NewGameDynastySettings
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset

/**
 * In-memory wizard draft — persisted to SQLite on sign contract (4.6).
 */
@Immutable
data class NewGameDraft(
    val coach: CoachCreateProfile = CoachCreateProfile(),
    val archetype: CoachArchetype = CoachArchetype.Balanced,
    val offensiveScheme: OffensiveSchemePreset = OffensiveSchemePreset.Balanced,
    val defensiveScheme: DefensiveSchemePreset = DefensiveSchemePreset.ManToMan,
    val dynastySettings: NewGameDynastySettings = NewGameDynastySettings(),
)

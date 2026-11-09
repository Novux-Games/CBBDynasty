package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.runtime.Immutable
import dev.novux.dynasty.cbb.core.coach.CoachCreateProfile

/**
 * In-memory wizard draft — persisted to SQLite on sign contract (4.6).
 */
@Immutable
data class NewGameDraft(
    val coach: CoachCreateProfile = CoachCreateProfile(),
)

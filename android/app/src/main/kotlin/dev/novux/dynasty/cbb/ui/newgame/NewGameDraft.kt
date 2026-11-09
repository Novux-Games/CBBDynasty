package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.runtime.Immutable

/**
 * In-memory wizard draft — fields filled in steps 4.2–4.7.
 */
@Immutable
data class NewGameDraft(
    val coachFirstName: String = "",
    val coachLastName: String = "",
)

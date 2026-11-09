package dev.novux.dynasty.cbb.core.save

/**
 * Per-dynasty rules chosen in the wizard — [planning/game-start.md#dynasty-toggles].
 */
data class NewGameDynastySettings(
    val autoUpgrade: Boolean = true,
    val unlimitedCoachAge: Boolean = false,
)

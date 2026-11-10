package dev.novux.dynasty.cbb.core.settings

/**
 * Per-dynasty automation toggles ([planning/game-design.md#automation-toggles-global]).
 */
data class DynastyAutomationSettings(
    /** AI maintains recruiting from Recommended on sim-week advance when player did not spend manually. */
    val recruitingAutoMaintain: Boolean = true,
    /** Head coach Sunday skill points auto-spend along archetype priority. */
    val coachAutoSpend: Boolean = false,
)

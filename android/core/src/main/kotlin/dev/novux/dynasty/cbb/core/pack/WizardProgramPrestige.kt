package dev.novux.dynasty.cbb.core.pack

/**
 * Program prestige for the new-game school picker until all pack teams carry
 * `programPrestige` in JSON ([planning/game-start.md#step-5--choose-school]).
 */
object WizardProgramPrestige {
    const val STARTER_SELECT_MAX = 40
    const val MIN = 0
    const val MAX = 99

    fun effective(team: TeamPack): Int {
        team.programPrestige?.let { return it.coerceIn(MIN, MAX) }
        return fallbackFromTeamId(team.id)
    }

    fun isSelectable(prestige: Int, unlockAll: Boolean): Boolean =
        unlockAll || prestige <= STARTER_SELECT_MAX

    /** Stable spread when pack omits prestige — not authoritative sim data. */
    private fun fallbackFromTeamId(teamId: String): Int {
        val hash = teamId.hashCode() and Int.MAX_VALUE
        return 18 + (hash % 62) // 18–79
    }
}

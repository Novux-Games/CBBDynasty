package dev.novux.dynasty.cbb.core.discipline

/** Suspension games decrement only when team played ([MEGA-SHELL-065]). */
object DisciplineByeWeekSuspensionPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-065"

    fun decrementSuspensionGames(teamPlayedGame: Boolean, gamesRemaining: Int): Int =
        if (teamPlayedGame) (gamesRemaining - 1).coerceAtLeast(0) else gamesRemaining
}
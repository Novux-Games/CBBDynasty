package dev.novux.dynasty.cbb.core.strategy

/** Halftime gameplan on scheduled games uses same prep as pregame until dedicated save column ships ([MEGA-SIM-053]). */
object HalftimeGameplanPersistPolicy {
    const val MEGA_ID = "MEGA-SIM-053"

    fun calendarBootstrapHalftime(pregame: GameplanInput): GameplanInput = pregame
}
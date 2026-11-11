package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.GameplanInput
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset

/** Regulation length — [planning/simulation-engine.md#possession-loop--clock]. */
object GameSimClock {
    const val REGULATION_SECONDS: Int = 40 * 60
    const val HALF_SECONDS: Int = 20 * 60
    const val MIN_TRIP_SECONDS: Int = 3
    const val MAX_TRIP_SECONDS: Int = 30
}

data class GameSimTeamInput(
    val teamId: String,
    val offensiveRating: Int,
    val defensiveRating: Int,
    val offensiveScheme: OffensiveSchemePreset = OffensiveSchemePreset.Balanced,
    val defensiveScheme: DefensiveSchemePreset = DefensiveSchemePreset.ManToMan,
)

data class GameSimInput(
    val home: GameSimTeamInput,
    val away: GameSimTeamInput,
    val tuning: SimTuning,
    val seed: Long,
    val homeRoster: GameSimRosterInput? = null,
    val awayRoster: GameSimRosterInput? = null,
    val homeGameplan: GameplanInput? = null,
    val awayGameplan: GameplanInput? = null,
    val homeHalftimeGameplan: GameplanInput? = null,
    val awayHalftimeGameplan: GameplanInput? = null,
) {
    fun activeHomeGameplan(clockSeconds: Int): GameplanInput? =
        if (clockSeconds <= GameSimClock.HALF_SECONDS) {
            homeHalftimeGameplan ?: homeGameplan
        } else {
            homeGameplan
        }

    fun activeAwayGameplan(clockSeconds: Int): GameplanInput? =
        if (clockSeconds <= GameSimClock.HALF_SECONDS) {
            awayHalftimeGameplan ?: awayGameplan
        } else {
            awayGameplan
        }
    fun homeOffensive(): Int = homeRoster?.weightedOffensive() ?: home.offensiveRating
    fun homeDefensive(): Int = homeRoster?.weightedDefensive() ?: home.defensiveRating
    fun awayOffensive(): Int = awayRoster?.weightedOffensive() ?: away.offensiveRating
    fun awayDefensive(): Int = awayRoster?.weightedDefensive() ?: away.defensiveRating
}

data class TeamBoxScore(
    val teamId: String,
    val points: Int = 0,
    val fieldGoalsMade: Int = 0,
    val fieldGoalsAttempted: Int = 0,
    val threePointsMade: Int = 0,
    val threePointsAttempted: Int = 0,
    val turnovers: Int = 0,
    val possessions: Int = 0,
    val personalFouls: Int = 0,
)

data class GameSimResult(
    val homeScore: Int,
    val awayScore: Int,
    val homeBox: TeamBoxScore,
    val awayBox: TeamBoxScore,
    val totalPossessions: Int,
    val possessionLog: List<String>,
    val isComplete: Boolean = true,
    val midGame: MidGameState? = null,
    val homeInjuries: List<GameSimInjuryEvent> = emptyList(),
    val awayInjuries: List<GameSimInjuryEvent> = emptyList(),
    val homePersonalFoulsByPlayer: Map<String, Int> = emptyMap(),
    val awayPersonalFoulsByPlayer: Map<String, Int> = emptyMap(),
    val homeFloorSecondsByPlayer: Map<String, Int> = emptyMap(),
    val awayFloorSecondsByPlayer: Map<String, Int> = emptyMap(),
) {
    fun userTeamInjuries(userTeamIsHome: Boolean): List<GameSimInjuryEvent> =
        if (userTeamIsHome) homeInjuries else awayInjuries
}

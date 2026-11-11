package dev.novux.dynasty.cbb.core.sim

import kotlin.random.Random

/** Carried across halftime when watching a game. */
data class MidGameState(
    val random: Random,
    val gameClockSeconds: Int,
    val offenseHome: Boolean,
    val homeBox: TeamBoxScore,
    val awayBox: TeamBoxScore,
    val possessionLog: List<String>,
    val totalPossessions: Int,
    /** Seconds left on the offensive shot clock for the current trip (MEGA-SIM-086). */
    val shotClockSecondsRemaining: Int = 30,
    val homePersonalFoulsByPlayer: Map<String, Int> = emptyMap(),
    val awayPersonalFoulsByPlayer: Map<String, Int> = emptyMap(),
    val homeFloorSecondsByPlayer: Map<String, Int> = emptyMap(),
    val awayFloorSecondsByPlayer: Map<String, Int> = emptyMap(),
    val homePlayerBox: Map<String, PlayerBoxScore> = emptyMap(),
    val awayPlayerBox: Map<String, PlayerBoxScore> = emptyMap(),
)

data class SimulateOptions(
    val resume: MidGameState? = null,
    val stopAtHalftime: Boolean = false,
)

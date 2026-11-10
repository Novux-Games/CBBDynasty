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
)

data class SimulateOptions(
    val resume: MidGameState? = null,
    val stopAtHalftime: Boolean = false,
)

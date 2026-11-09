package dev.novux.dynasty.cbb.core.sim.tuning

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SimTuning(
    val simTuningId: String,
    val schemaVersion: Int,
    val possessionsPerGameTarget: IntMinMax,
    val shotClock: SimShotClock,
    val timeUsedSeconds: SimTimeUsedSeconds,
    val eventWeights: JsonElement,
    val homeNeutralModifiers: SimVenueModifiers,
)

@Serializable
data class IntMinMax(
    val min: Int,
    val max: Int,
)

@Serializable
data class SimShotClock(
    val full: Int,
    val offensiveRebound: Int,
)

@Serializable
data class SimTimeUsedSeconds(
    val quickTurnover: List<Int>,
    val quickShot: List<Int>,
    val foulStoppage: List<Int>,
)

@Serializable
data class SimVenueModifiers(
    val home: Double,
    val neutral: Double,
    val road: Double,
)

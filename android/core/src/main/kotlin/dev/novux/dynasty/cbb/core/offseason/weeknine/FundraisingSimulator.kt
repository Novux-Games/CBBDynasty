package dev.novux.dynasty.cbb.core.offseason.weeknine

import kotlin.math.roundToInt

object FundraisingSimulator {
    fun simulate(
        event: FundraisingEventType,
        programPrestige: Int,
    ): FundraisingOutcome {
        val prestigeFactor = 0.75 + programPrestige.coerceIn(1, 99) / 200.0
        val (cashBase, nilBase, seedBase) = when (event) {
            FundraisingEventType.Gala -> Triple(120_000, 80_000, 40_000)
            FundraisingEventType.AlumniGame -> Triple(90_000, 50_000, 55_000)
            FundraisingEventType.DonorDrive -> Triple(75_000, 120_000, 35_000)
        }
        return FundraisingOutcome(
            cashUsd = (cashBase * prestigeFactor).roundToInt(),
            nilBoostUsd = (nilBase * prestigeFactor).roundToInt(),
            facilitySeedUsd = (seedBase * prestigeFactor).roundToInt(),
        )
    }
}

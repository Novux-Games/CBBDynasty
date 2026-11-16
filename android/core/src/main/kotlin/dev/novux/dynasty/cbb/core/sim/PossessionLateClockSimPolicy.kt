package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.strategy.ResolvedSchemeEffects

/**
 * Late-clock (22–30s) possession stress in live sim ([MEGA-SIM-006]).
 */
object PossessionLateClockSimPolicy {
    const val MEGA_ID: String = "MEGA-SIM-006"

    fun isLateClockTrip(usedSeconds: Int, shotClockFull: Int = 30): Boolean =
        PossessionLateClockPolicy.isLateClockTrip(usedSeconds, shotClockFull)

    fun applyToEffects(effects: ResolvedSchemeEffects): ResolvedSchemeEffects =
        PossessionLateClockPolicy.applyToEffects(effects)
}
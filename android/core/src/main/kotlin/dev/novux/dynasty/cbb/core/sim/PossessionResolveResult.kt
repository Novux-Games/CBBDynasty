package dev.novux.dynasty.cbb.core.sim

enum class PossessionChange {
    DefenseTakesBall,
    OffenseRetainsOffensiveRebound,
}

/** Distinguishes turnover flavors in logs and future stat splits (MEGA-SIM-087). */
enum class TurnoverTerminal {
    Generic,
    ShotClockViolation,
}

data class PossessionResolveResult(
    val box: TeamBoxScore,
    val change: PossessionChange,
    val turnoverTerminal: TurnoverTerminal? = null,
)

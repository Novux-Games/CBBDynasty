package dev.novux.dynasty.cbb.core.sim

enum class PossessionChange {
    DefenseTakesBall,
    OffenseRetainsOffensiveRebound,
}

data class PossessionResolveResult(
    val box: TeamBoxScore,
    val change: PossessionChange,
)

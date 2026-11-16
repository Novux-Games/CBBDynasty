package dev.novux.dynasty.cbb.core.roster

/** Shell roster row compact vs comfortable layout ([MEGA-COACH-177], [MEGA-COACH-178]). */
enum class RosterRowDensity {
    Compact,
    Comfortable,
}

object RosterRowDensityPolicy {
    const val MEGA_COMPACT: String = "MEGA-COACH-177"
    const val MEGA_TOGGLE: String = "MEGA-COACH-178"

    fun showLoyaltyChipsOnRoster(density: RosterRowDensity): Boolean =
        density == RosterRowDensity.Comfortable
}
package dev.novux.dynasty.cbb.core.portal

data class PortalCycleState(
    val membershipSeasonIndex: Int,
    val consideringPoolSize: Int,
    val entrants: List<PortalEntrant>,
    val top40Locked: Boolean,
) {
    fun onOffseasonWeekStarted(weekIndex: Int): PortalCycleState {
        if (!PortalCyclePolicy.shouldLockTop40AtWeekStart(weekIndex) || top40Locked) return this
        val withStars = PortalStarBands.applyToEntrants(entrants)
        val locked = PortalTop40.lock(withStars)
        return copy(entrants = locked, top40Locked = true)
    }
}

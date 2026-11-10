package dev.novux.dynasty.cbb.core.portal

/**
 * 2026-era immediate transfer eligibility when time remains in the five-year window
 * ([planning/transfer-portal.md#eligibility--roster-law-2026]).
 */
data class PortalEligibilityClock(
    val clockStartSeasonIndex: Int,
    val seasonsOfCompetitionUsed: Int,
) {
    companion object {
        const val ELIGIBILITY_WINDOW_SEASONS = 5
        const val MAX_COMPETITION_SEASONS = 4
    }
}

object PortalEligibility {
    /** Transfers may sign and play immediately when the clock has room. */
    fun immediateEligibilityAtSign(
        clock: PortalEligibilityClock,
        signingMembershipSeasonIndex: Int,
    ): Boolean {
        val elapsed = (signingMembershipSeasonIndex - clock.clockStartSeasonIndex).coerceAtLeast(0)
        if (elapsed >= PortalEligibilityClock.ELIGIBILITY_WINDOW_SEASONS) return false
        if (clock.seasonsOfCompetitionUsed >= PortalEligibilityClock.MAX_COMPETITION_SEASONS) return false
        return true
    }
}

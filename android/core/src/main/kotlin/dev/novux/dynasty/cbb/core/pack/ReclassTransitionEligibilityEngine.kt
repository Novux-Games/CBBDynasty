package dev.novux.dynasty.cbb.core.pack

/**
 * Dynasty-season rollover for reclass certification ([MEGA-WORLD-046], [MEGA-WORLD-047]).
 * Pack baseline marks programs ineligible; eligibility flips after the four-year transition window.
 */
object ReclassTransitionEligibilityEngine {
    const val MEGA_ID = "MEGA-WORLD-046"

    fun effectivePostseasonEligible(
        staticEligible: Boolean,
        reclassTransitionYear: Int?,
        dynastySeasonIndex: Int,
    ): Boolean {
        if (staticEligible) return true
        val start = reclassTransitionYear ?: return false
        val transitionYear = start + dynastySeasonIndex - 1
        return transitionYear > ReclassTransitionUxPolicy.MAX_TRANSITION_YEARS
    }

    fun effectivePostseasonEligible(team: TeamDisplayOverlay, dynastySeasonIndex: Int): Boolean =
        effectivePostseasonEligible(
            staticEligible = team.postseasonEligible,
            reclassTransitionYear = team.reclassTransitionYear,
            dynastySeasonIndex = dynastySeasonIndex,
        )
}
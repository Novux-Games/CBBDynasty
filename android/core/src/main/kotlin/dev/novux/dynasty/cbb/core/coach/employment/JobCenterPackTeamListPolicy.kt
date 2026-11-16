package dev.novux.dynasty.cbb.core.coach.employment

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay

/** Job center offers draw from active pack team overlays ([MEGA-WORLD-152]). */
object JobCenterPackTeamListPolicy {
    const val MEGA_ID = "MEGA-WORLD-152"

    fun offerPoolFromPack(teams: List<TeamDisplayOverlay>): List<TeamDisplayOverlay> = teams

    fun generateFromPack(
        teams: List<TeamDisplayOverlay>,
        seasonIndex: Int,
        dynastyId: String,
        coachReputation: Int = 55,
    ): List<JobOfferCard> =
        JobCenterEngine.generateOffers(
            offerPoolFromPack(teams),
            seasonIndex,
            dynastyId,
            coachReputation,
        )
}
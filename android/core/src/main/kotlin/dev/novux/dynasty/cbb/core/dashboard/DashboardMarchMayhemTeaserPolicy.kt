package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** Dashboard postseason seed + March Mayhem deep link ([MEGA-POST-051], [MEGA-POST-056], [MEGA-POST-111]). */
object DashboardMarchMayhemTeaserPolicy {
    const val SEED_ROW_ID: String = "MEGA-POST-056"
    const val TEASER_ID: String = "MEGA-POST-051"
    const val DEEP_LINK_ID: String = "MEGA-POST-111"

    fun preferredTournamentTab(snapshot: SelectionSundaySnapshot, userTeamId: String): String =
        when {
            snapshot.ncbaField.any { it.teamId == userTeamId } -> "NCBA"
            snapshot.nciField.any { it.teamId == userTeamId } -> "NCI"
            snapshot.heritageField.any { it.teamId == userTeamId } -> "HC"
            else -> "Overview"
        }

    fun seedLabel(userTeamId: String, snapshot: SelectionSundaySnapshot?): String? =
        DashboardPostseasonSeedResolver.label(userTeamId, snapshot)
}
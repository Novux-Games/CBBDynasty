package dev.novux.dynasty.cbb.core.selection

/**
 * Heritage Classic pool includes reclass programs; NCBA/NCI gate on [postseasonEligible] only
 * ([planning/selection-engine.md], [MEGA-POST-029], [MEGA-POST-144]).
 */
object HeritageSelectionEligibilityPolicy {
    const val MEGA_ID: String = "MEGA-POST-029"
    const val RECLASS_AWARE_ID: String = "MEGA-POST-144"

    fun mapForNetTeams(netRankByTeam: Map<String, Int>): Map<String, Boolean> =
        netRankByTeam.keys.associateWith { true }
}
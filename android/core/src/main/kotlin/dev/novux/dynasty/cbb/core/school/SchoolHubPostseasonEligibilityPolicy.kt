package dev.novux.dynasty.cbb.core.school

/** Ineligibility / reclass copy on school hub ([MEGA-WORLD-114], [MEGA-WORLD-091]). */
object SchoolHubPostseasonEligibilityPolicy {
    const val MEGA_ID = "MEGA-WORLD-114"
    const val CENSUS_MEGA_ID = "MEGA-WORLD-091"

    fun statusLine(overview: SchoolHubOverview): String? {
        if (overview.nationalPostseasonEligible) return null
        return when {
            overview.reclassTransitionYear != null ->
                "Provisional reclass — NCBA/NCI selection unlocks after transition"
            else -> "Postseason ineligible per pack membership"
        }
    }

    const val DIRECTORY_CENSUS_NOTE: String =
        "361 postseason-certified + 4 provisional reclass (365 total)"
}
package dev.novux.dynasty.cbb.core.recruiting

/**
 * Visit distance bands for official visit costs ([planning/recruiting-engine.md#official-visit-distance]).
 */
object OfficialVisitDistanceResolver {
    fun resolve(prospectHomeState: String, school: SchoolRecruitingContext): OfficialVisitDistanceBand {
        val prospect = prospectHomeState.trim().uppercase()
        val home = school.schoolHomeState.trim().uppercase()
        if (prospect == home) {
            return OfficialVisitDistanceBand.Local
        }
        val pipeline = school.pipelineStates.map { it.trim().uppercase() }.toSet()
        if (prospect in pipeline) {
            return OfficialVisitDistanceBand.Local
        }
        val prospectRegion = regionFor(prospect)
        val homeRegion = regionFor(home)
        return if (prospectRegion != null && prospectRegion == homeRegion) {
            OfficialVisitDistanceBand.Regional
        } else {
            OfficialVisitDistanceBand.Remote
        }
    }

    private fun regionFor(state: String): String? = US_REGION[state]

    private val US_REGION: Map<String, String> = mapOf(
        "AL" to "south", "AR" to "south", "FL" to "south", "GA" to "south", "KY" to "south",
        "LA" to "south", "MS" to "south", "NC" to "south", "SC" to "south", "TN" to "south",
        "TX" to "south", "VA" to "south", "WV" to "south",
        "AZ" to "west", "CA" to "west", "CO" to "west", "ID" to "west", "MT" to "west",
        "NM" to "west", "NV" to "west", "OR" to "west", "UT" to "west", "WA" to "west", "WY" to "west",
        "CT" to "northeast", "DE" to "northeast", "MA" to "northeast", "MD" to "northeast",
        "ME" to "northeast", "NH" to "northeast", "NJ" to "northeast", "NY" to "northeast",
        "PA" to "northeast", "RI" to "northeast", "VT" to "northeast",
        "IA" to "midwest", "IL" to "midwest", "IN" to "midwest", "KS" to "midwest",
        "MI" to "midwest", "MN" to "midwest", "MO" to "midwest", "ND" to "midwest",
        "NE" to "midwest", "OH" to "midwest", "SD" to "midwest", "WI" to "midwest",
        "AK" to "pacific", "HI" to "pacific",
        "DC" to "northeast",
    )
}

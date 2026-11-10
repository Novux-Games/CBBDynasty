package dev.novux.dynasty.cbb.core.recruiting

/**
 * Once-per-recruit-per-season limits for scout game and official visit.
 */
data class RecruitingSeasonConsumption(
    val cycleSeasonIndex: Int,
    val scoutedProspectIds: Set<String> = emptySet(),
    val officialVisitProspectIds: Set<String> = emptySet(),
) {
    fun consumedScout(prospectId: String): Boolean = prospectId in scoutedProspectIds

    fun consumedOfficialVisit(prospectId: String): Boolean = prospectId in officialVisitProspectIds

    fun recordScout(prospectId: String): RecruitingSeasonConsumption =
        copy(scoutedProspectIds = scoutedProspectIds + prospectId)

    fun recordOfficialVisit(prospectId: String): RecruitingSeasonConsumption =
        copy(officialVisitProspectIds = officialVisitProspectIds + prospectId)
}

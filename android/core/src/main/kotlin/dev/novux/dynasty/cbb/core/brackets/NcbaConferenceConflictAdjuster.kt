package dev.novux.dynasty.cbb.core.brackets

/**
 * Swap regional 16-seeds across regions when an R1 same-conference clash would occur.
 */
object NcbaConferenceConflictAdjuster {
    fun adjust(
        entrants: List<NcbaMainBracketEntrant>,
        conferenceIdByTeam: Map<String, String>,
    ): List<NcbaMainBracketEntrant> {
        val mutable = entrants.toMutableList()
        for (region in NcbaRegionId.serpentineOrder) {
            val inRegion = mutable.filter { it.regionId == region }.sortedBy { it.regionalSeed }
            if (inRegion.size < 16) continue
            val one = inRegion.first()
            val sixteen = inRegion.last()
            val conf = conferenceIdByTeam[one.teamId] ?: continue
            if (conferenceIdByTeam[sixteen.teamId] != conf) continue
            val swap = mutable.firstOrNull {
                it.regionId != region &&
                    it.regionalSeed == 16 &&
                    conferenceIdByTeam[it.teamId] != conf
            } ?: continue
            swapPair(mutable, sixteen.teamId, swap.teamId)
        }
        return mutable
    }

    private fun swapPair(mutable: MutableList<NcbaMainBracketEntrant>, teamA: String, teamB: String) {
        val idxA = mutable.indexOfFirst { it.teamId == teamA }
        val idxB = mutable.indexOfFirst { it.teamId == teamB }
        if (idxA < 0 || idxB < 0) return
        val a = mutable[idxA]
        val b = mutable[idxB]
        mutable[idxA] = b.copy(regionId = a.regionId, regionalSeed = a.regionalSeed)
        mutable[idxB] = a.copy(regionId = b.regionId, regionalSeed = b.regionalSeed)
    }
}

package dev.novux.dynasty.cbb.core.portal

/**
 * Portal Top 40 — locked among final entrants at Offseason Week 2 start
 * ([planning/transfer-portal.md#player-presentation]).
 */
object PortalTop40 {
    fun lock(entrants: List<PortalEntrant>): List<PortalEntrant> {
        val sorted = entrants.sortedByDescending { it.hiddenRating }
        return sorted.mapIndexed { index, entrant ->
            val rank = index + 1
            val topRank = if (rank <= PortalConstants.TOP_ENTRANTS_RANKED) rank else null
            entrant.copy(nationalTop40Rank = topRank)
        }
    }

    fun rankedList(entrants: List<PortalEntrant>): List<PortalEntrant> =
        entrants
            .filter { it.nationalTop40Rank != null }
            .sortedBy { it.nationalTop40Rank!! }
            .take(PortalConstants.TOP_ENTRANTS_RANKED)
}

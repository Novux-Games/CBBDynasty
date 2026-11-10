package dev.novux.dynasty.cbb.core.portal

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear

data class PortalEntrant(
    val entrantId: String,
    val priorProgramId: String,
    val classYear: PlayerClassYear,
    val hiddenRating: Int,
    val stars: Int? = null,
    /** National portal rank — set only after Top 40 lock (1..40) or null. */
    val nationalTop40Rank: Int? = null,
    val eligibilityClock: PortalEligibilityClock,
)

package dev.novux.dynasty.cbb.core.player.draft

/** Draft tier sublabels Lottery / First round / … ([MEGA-COACH-078], [MEGA-COACH-164]). */
object PlayerDraftTierPolicy {
    const val MEGA_ID_LABELS: String = "MEGA-COACH-078"
    const val MEGA_ID_MAPPER: String = "MEGA-COACH-164"

    const val ENGINE: String = "PlayerDraftProjectionEngine"

    fun tierLabelForRank(rank: Int): String = PlayerDraftProjectionEngine.tierForRank(rank)
}
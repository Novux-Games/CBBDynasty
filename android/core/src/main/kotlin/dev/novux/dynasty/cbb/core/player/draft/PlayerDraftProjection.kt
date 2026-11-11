package dev.novux.dynasty.cbb.core.player.draft

import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear

/**
 * Projected draft slot on player Overview ([MEGA-COACH-065], [planning/player-archetypes.md]).
 */
data class PlayerDraftProjection(
    val rank: Int?,
    val tierLabel: String?,
) {
    val isAvailable: Boolean get() = rank != null

    companion object {
        val NotAvailable = PlayerDraftProjection(rank = null, tierLabel = null)
    }
}

object PlayerDraftProjectionEngine {
    const val QA_ID: String = "MEGA-COACH-065"

    private const val MIN_STOCK_FOR_TOP_60 = 40

    fun resolve(player: DynastyPlayer): PlayerDraftProjection {
        if (!isDraftEligible(player)) return PlayerDraftProjection.NotAvailable
        val stock = player.role.draftStockPoints
        if (stock < MIN_STOCK_FOR_TOP_60) return PlayerDraftProjection.NotAvailable
        val rank = stockToRank(stock)
        return PlayerDraftProjection(rank = rank, tierLabel = tierForRank(rank))
    }

    fun isDraftEligible(player: DynastyPlayer): Boolean {
        if (player.role.inTransferPortal) return false
        return player.role.declaredForDraft ||
            player.classYear == PlayerClassYear.Senior ||
            (
                player.classYear == PlayerClassYear.Junior &&
                    player.role.draftStockPoints >= MIN_STOCK_FOR_TOP_60
                )
    }

    fun stockToRank(stockPoints: Int): Int {
        val clamped = stockPoints.coerceIn(MIN_STOCK_FOR_TOP_60, 100)
        val span = 100 - MIN_STOCK_FOR_TOP_60
        val rank = 60 - ((clamped - MIN_STOCK_FOR_TOP_60) * 59 / span)
        return rank.coerceIn(1, 60)
    }

    fun tierForRank(rank: Int): String = when (rank) {
        in 1..14 -> "Lottery"
        in 15..29 -> "First round"
        in 30..44 -> "Early second"
        in 45..60 -> "Late second"
        else -> ""
    }
}

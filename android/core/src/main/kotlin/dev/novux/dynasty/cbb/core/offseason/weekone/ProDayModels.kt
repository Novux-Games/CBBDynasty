package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

object ProDayEligibility {
    fun eligible(player: RosterPlayer): Boolean =
        !player.inTransferPortal &&
            (player.classYear == PlayerClassYear.Senior || player.declaredForDraft)
}

data class DraftStockLine(
    val playerId: String,
    val displayLabel: String,
    val stockPoints: Int,
    val mockTier: String,
)

object DraftStockTiers {
    fun mockTier(stockPoints: Int): String =
        when {
            stockPoints >= 85 -> "Lottery range"
            stockPoints >= 70 -> "First round fringe"
            stockPoints >= 55 -> "Second round"
            stockPoints >= 40 -> "Summer league"
            else -> "Undrafted"
        }
}

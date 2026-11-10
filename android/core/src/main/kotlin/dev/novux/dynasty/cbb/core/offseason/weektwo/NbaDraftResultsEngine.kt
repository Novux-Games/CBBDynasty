package dev.novux.dynasty.cbb.core.offseason.weektwo

import dev.novux.dynasty.cbb.core.offseason.weekone.DraftStockTiers
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

enum class NbaDraftOutcome {
    Drafted,
    UndraftedGraduate,
    ReturnedToRoster,
}

data class NbaDraftResultLine(
    val playerId: String,
    val displayLabel: String,
    val stockPoints: Int,
    val mockTier: String,
    val outcome: NbaDraftOutcome,
    val pickSummary: String?,
)

data class NbaDraftResolution(
    val resultLines: List<NbaDraftResultLine>,
    val playersToRemove: Set<String>,
    val playersToUpdate: List<RosterPlayer>,
)

/**
 * Offseason week 2 NBA draft results from week 1 pro day stock ([planning/offseason.md]).
 */
object NbaDraftResultsEngine {
    private const val DRAFTED_STOCK_THRESHOLD = 62

    fun resolve(players: List<RosterPlayer>): NbaDraftResolution {
        val candidates = players.filter { it.proDayCompleted }
        val remove = mutableSetOf<String>()
        val updates = mutableListOf<RosterPlayer>()
        val lines = candidates.map { player ->
            val tier = DraftStockTiers.mockTier(player.draftStockPoints)
            val drafted = player.draftStockPoints >= DRAFTED_STOCK_THRESHOLD
            when {
                drafted -> {
                    remove.add(player.playerId)
                    NbaDraftResultLine(
                        playerId = player.playerId,
                        displayLabel = player.displayLabel(),
                        stockPoints = player.draftStockPoints,
                        mockTier = tier,
                        outcome = NbaDraftOutcome.Drafted,
                        pickSummary = pickLabel(player.draftStockPoints),
                    )
                }
                player.classYear == PlayerClassYear.Senior -> {
                    remove.add(player.playerId)
                    NbaDraftResultLine(
                        playerId = player.playerId,
                        displayLabel = player.displayLabel(),
                        stockPoints = player.draftStockPoints,
                        mockTier = tier,
                        outcome = NbaDraftOutcome.UndraftedGraduate,
                        pickSummary = null,
                    )
                }
                else -> {
                    updates.add(
                        player.copy(
                            declaredForDraft = false,
                            proDayCompleted = false,
                            draftStockPoints = 0,
                        ),
                    )
                    NbaDraftResultLine(
                        playerId = player.playerId,
                        displayLabel = player.displayLabel(),
                        stockPoints = player.draftStockPoints,
                        mockTier = tier,
                        outcome = NbaDraftOutcome.ReturnedToRoster,
                        pickSummary = null,
                    )
                }
            }
        }
        return NbaDraftResolution(lines, remove, updates)
    }

    private fun pickLabel(stock: Int): String =
        when {
            stock >= 85 -> "Lottery pick"
            stock >= 75 -> "Mid-first round"
            stock >= 68 -> "Late first round"
            else -> "Second round"
        }
}

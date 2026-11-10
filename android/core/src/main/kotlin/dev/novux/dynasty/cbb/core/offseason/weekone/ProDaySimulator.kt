package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import kotlin.random.Random

/**
 * Week 1 pro days — stock locks for Week 2 draft results ([planning/offseason.md#pro-days]).
 */
object ProDaySimulator {
    private const val DEFAULT_STOCK = 42

    fun baselineStock(player: RosterPlayer): Int =
        if (player.draftStockPoints > 0) player.draftStockPoints else DEFAULT_STOCK

    fun run(player: RosterPlayer, random: Random = Random.Default): RosterPlayer {
        require(ProDayEligibility.eligible(player)) { "player not eligible for pro day" }
        val base = baselineStock(player)
        val bump = random.nextInt(3, 9)
        return player.copy(
            draftStockPoints = (base + bump).coerceAtMost(100),
            proDayCompleted = true,
        )
    }

    fun simAll(eligible: List<RosterPlayer>, random: Random = Random.Default): List<RosterPlayer> =
        eligible.map { player ->
            if (player.proDayCompleted) player else run(player, random)
        }
}

object WeekTwoDraftResults {
    /** Posted in Offseason Week 2 from Week 1 pro day stock. */
    fun fromRoster(players: List<RosterPlayer>): List<DraftStockLine> =
        players
            .filter { it.proDayCompleted }
            .sortedByDescending { it.draftStockPoints }
            .map { player ->
                DraftStockLine(
                    playerId = player.playerId,
                    displayLabel = player.displayLabel(),
                    stockPoints = player.draftStockPoints,
                    mockTier = DraftStockTiers.mockTier(player.draftStockPoints),
                )
            }
}

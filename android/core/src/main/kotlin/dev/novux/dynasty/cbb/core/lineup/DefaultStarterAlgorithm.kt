package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.discipline.DisciplineResolver
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/**
 * Default starting five by **listed position** OVR — [planning/positions-and-lineup.md#default-starters-algorithm].
 */
object DefaultStarterAlgorithm {
    fun pickStarters(players: List<RosterPlayer>): Map<PlayerPosition, String> {
        val healthy = players.filter {
            !it.isInjuredOut() && !it.medicalRedshirtSeason && !DisciplineResolver.isIneligibleForLineup(it)
        }
        val result = linkedMapOf<PlayerPosition, String>()
        PlayerPosition.STARTER_ORDER.forEach { slot ->
            val best = healthy
                .filter { it.position == slot }
                .sortedWith(compareByDescending<RosterPlayer> { it.ovr }.thenBy { it.playerId })
                .firstOrNull()
            if (best != null) {
                result[slot] = best.playerId
            }
        }
        return result
    }

    fun applyToRoster(
        players: List<RosterPlayer>,
        starters: Map<PlayerPosition, String>,
    ): List<RosterPlayer> {
        val starterIds = starters.values.toSet()
        var benchRank = 6
        return players.map { player ->
            val slot = starters.entries.firstOrNull { it.value == player.playerId }?.key
            when {
                player.isInjuredOut() || player.medicalRedshirtSeason -> player.copy(
                    isStarter = false,
                    starterSlotWire = null,
                    depthOrder = 20 + player.depthOrder,
                )
                slot != null -> player.copy(
                    isStarter = true,
                    starterSlotWire = slot.wire,
                    depthOrder = PlayerPosition.STARTER_ORDER.indexOf(slot) + 1,
                )
                starterIds.contains(player.playerId) -> player
                else -> {
                    val next = player.copy(
                        isStarter = false,
                        starterSlotWire = null,
                        depthOrder = benchRank,
                    )
                    benchRank++
                    next
                }
            }
        }
    }

    private fun RosterPlayer.isInjuredOut(): Boolean = injuryActive && injuryWeeksRemaining > 0
}

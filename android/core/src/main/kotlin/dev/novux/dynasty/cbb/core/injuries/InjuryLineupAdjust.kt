package dev.novux.dynasty.cbb.core.injuries

import dev.novux.dynasty.cbb.core.lineup.DefaultStarterAlgorithm
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Auto lineup on injury / return — [planning/injuries.md#lineup--rotation]. */
object InjuryLineupAdjust {
    fun applyInjuryLineupAdjust(
        players: List<RosterPlayer>,
        injuredPlayerIds: Set<String>,
    ): List<RosterPlayer> {
        if (injuredPlayerIds.isEmpty()) return players
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        val withSnapshots = players.map { player ->
            if (player.playerId !in injuredPlayerIds) return@map player
            val slot = player.starterSlotWire?.let(PlayerPosition::fromWire)
            player.copy(
                isStarter = false,
                starterSlotWire = null,
                depthOrder = 30 + player.depthOrder,
                injuryWasStarterSlotWire = slot?.wire,
                injuryPriorRotationBand = player.depthOrder,
            )
        }
        val promoted = DefaultStarterAlgorithm.applyToRoster(withSnapshots, starters)
        return promoted.map { player ->
            if (player.playerId in injuredPlayerIds) {
                player.copy(isStarter = false, starterSlotWire = null)
            } else {
                player
            }
        }
    }

    fun applyInjuryReturnLineupAdjust(
        players: List<RosterPlayer>,
        returnedPlayerId: String,
    ): List<RosterPlayer> {
        val returning = players.firstOrNull { it.playerId == returnedPlayerId } ?: return players
        if (returning.injuryActive || returning.medicalRedshirtSeason) return players
        val slotWire = returning.injuryWasStarterSlotWire
        if (slotWire != null) {
            val slot = PlayerPosition.fromWire(slotWire) ?: return players
            val displaced = players.firstOrNull { it.starterSlotWire == slotWire }
            var updated = players.map { player ->
                when (player.playerId) {
                    returnedPlayerId -> player.copy(
                        isStarter = true,
                        starterSlotWire = slotWire,
                        depthOrder = PlayerPosition.STARTER_ORDER.indexOf(slot) + 1,
                        injuryWasStarterSlotWire = null,
                        injuryPriorRotationBand = null,
                    )
                    displaced?.playerId -> player.copy(
                        isStarter = false,
                        starterSlotWire = null,
                        depthOrder = returning.injuryPriorRotationBand ?: 12,
                    )
                    else -> player
                }
            }
            val starters = DefaultStarterAlgorithm.pickStarters(updated).toMutableMap()
            starters[slot] = returnedPlayerId
            return DefaultStarterAlgorithm.applyToRoster(updated, starters)
        }
        val band = returning.injuryPriorRotationBand ?: 6
        return players.map { player ->
            if (player.playerId == returnedPlayerId) {
                player.copy(
                    isStarter = false,
                    starterSlotWire = null,
                    depthOrder = band.coerceAtMost(6),
                    injuryWasStarterSlotWire = null,
                    injuryPriorRotationBand = null,
                )
            } else if (!player.isStarter && !player.injuryActive && player.depthOrder >= band) {
                player.copy(depthOrder = player.depthOrder + 1)
            } else {
                player
            }
        }
    }
}

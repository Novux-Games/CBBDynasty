package dev.novux.dynasty.cbb.core.preview

import dev.novux.dynasty.cbb.core.balance.BalanceFatigue
import dev.novux.dynasty.cbb.core.league.AiLeagueSyntheticRoster
import dev.novux.dynasty.cbb.core.lineup.DefaultStarterAlgorithm
import dev.novux.dynasty.cbb.core.lineup.RotationMinutesBalancer
import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Scouting rotation table for non-user teams ([planning/player-cards.md], MEGA-GDD-009). */
object OpponentRotationPreviewBuilder {
    const val MEGA_ID: String = "MEGA-GDD-009"

    fun build(
        teamId: String,
        displayName: String,
        programPrestige: Int,
        fatigue: BalanceFatigue = BalanceFatigue.DEFAULT,
    ): GamePreviewRotationSnapshot {
        val players = AiLeagueSyntheticRoster.playersForTeam(teamId, programPrestige)
        val starters = DefaultStarterAlgorithm.pickStarters(players, fatigue.starterBlockThreshold)
        val minutes = RotationMinutesBalancer.baseline(players, starters)
        val ordered = buildList {
            PlayerPosition.STARTER_ORDER.forEach { slot ->
                starters[slot]?.let { id ->
                    players.firstOrNull { it.playerId == id }?.let { add(slot to it) }
                }
            }
            players
                .filter { it.playerId !in starters.values }
                .sortedBy { it.depthOrder }
                .forEach { add(null to it) }
        }
        val lines = ordered.map { (slot, player) ->
            GamePreviewRotationLine(
                slotLabel = slot?.label ?: player.position.label,
                playerLabel = player.displayLabel(),
                targetMinutes = minutes[player.playerId] ?: 0,
                starter = slot != null,
            )
        }
        return GamePreviewRotationSnapshot(teamDisplayName = displayName, lines = lines)
    }
}
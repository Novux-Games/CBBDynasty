package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.discipline.DisciplineResolver
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Minutes bands before possession sim — [planning/positions-and-lineup.md#rotation--bench]. */
object RotationMinutesAllocator {
    const val TEAM_MINUTES_REGULATION: Int = 200
    const val STARTER_MINUTES: Int = 32
    const val RESERVE_MINUTES: Int = 12

    fun allocate(
        players: List<RosterPlayer>,
        starters: Map<PlayerPosition, String>,
    ): Map<String, Int> {
        val eligible = players.filter { isLineupEligible(it) }
        val minutes = linkedMapOf<String, Int>()
        starters.values.forEach { id ->
            if (eligible.any { it.playerId == id }) {
                minutes[id] = STARTER_MINUTES
            }
        }
        var used = minutes.values.sum()
        val reserves = eligible
            .filter { it.playerId !in starters.values }
            .sortedBy { it.depthOrder }
        for (player in reserves) {
            if (used >= TEAM_MINUTES_REGULATION) break
            val grant = minOf(RESERVE_MINUTES, TEAM_MINUTES_REGULATION - used)
            minutes[player.playerId] = grant
            used += grant
        }
        if (used < TEAM_MINUTES_REGULATION && minutes.isNotEmpty()) {
            val keys = minutes.keys.toList()
            var remaining = TEAM_MINUTES_REGULATION - used
            var index = 0
            while (remaining > 0) {
                val key = keys[index % keys.size]
                minutes[key] = minutes.getValue(key) + 1
                remaining--
                index++
            }
        }
        return minutes
    }

    fun isLineupEligible(player: RosterPlayer): Boolean =
        !player.isInjuredOut() &&
            !player.medicalRedshirtSeason &&
            !DisciplineResolver.isIneligibleForLineup(player)

    private fun RosterPlayer.isInjuredOut(): Boolean = injuryActive && injuryWeeksRemaining > 0
}

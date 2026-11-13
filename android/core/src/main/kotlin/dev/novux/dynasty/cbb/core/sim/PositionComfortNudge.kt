package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.lineup.LineupSlotPolicy
import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/**
 * Out-of-position comfort nudge — does not reorder players; adjusts rating only
 * ([planning/positions-and-lineup.md#game-slots-vs-listed-position]).
 */
object PositionComfortNudge {
    fun offensiveRating(participant: GameSimParticipant): Int =
        (participant.offensiveRating + comfortDelta(participant)).coerceIn(1, 99)

    fun defensiveRating(participant: GameSimParticipant): Int =
        (participant.defensiveRating + comfortDelta(participant)).coerceIn(1, 99)

    private fun comfortDelta(participant: GameSimParticipant): Int {
        if (participant.listedPosition == participant.slotPlayed) return 0
        return if (LineupSlotPolicy.slotEligible(participant.listedPosition, participant.slotPlayed)) {
            -1
        } else {
            -3
        }
    }
}

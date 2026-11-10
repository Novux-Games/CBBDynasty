package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Slot eligibility — [planning/positions-and-lineup.md#game-slots-vs-listed-position]. */
object LineupSlotPolicy {
    fun slotEligible(listed: PlayerPosition, slot: PlayerPosition): Boolean = when (slot) {
        PlayerPosition.PointGuard, PlayerPosition.ShootingGuard ->
            listed == PlayerPosition.PointGuard || listed == PlayerPosition.ShootingGuard
        PlayerPosition.SmallForward, PlayerPosition.PowerForward ->
            listed == PlayerPosition.SmallForward || listed == PlayerPosition.PowerForward
        PlayerPosition.Center -> listed == PlayerPosition.Center
    }
}

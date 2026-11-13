package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Per-possession team rating from the five **slot played** positions on the floor (MEGA-SIM-167). */
object SlotPlayedFloorRating {
    fun offensiveFromFloor(
        floor: List<GameSimParticipant>,
        fallback: Int,
    ): Int = fromFloor(floor, fallback, PositionComfortNudge::offensiveRating)

    fun defensiveFromFloor(
        floor: List<GameSimParticipant>,
        fallback: Int,
    ): Int = fromFloor(floor, fallback, PositionComfortNudge::defensiveRating)

    private fun fromFloor(
        floor: List<GameSimParticipant>,
        fallback: Int,
        pick: (GameSimParticipant) -> Int,
    ): Int {
        if (floor.isEmpty()) return fallback
        val bySlot = floor.groupBy { it.slotPlayed }
        val slotRatings = PlayerPosition.STARTER_ORDER.map { slot ->
            val candidates = bySlot[slot].orEmpty()
            when {
                candidates.isEmpty() -> fallback
                candidates.size == 1 -> pick(candidates.first())
                else -> {
                    val best = candidates.maxByOrNull { pick(it) } ?: candidates.first()
                    pick(best)
                }
            }
        }
        return slotRatings.average().toInt().coerceIn(1, 99)
    }
}

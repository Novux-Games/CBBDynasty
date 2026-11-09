package dev.novux.dynasty.cbb.core.roster

/**
 * Generated roster row at dynasty create — full attrs ship in later phases.
 */
data class RosterPlayerSeed(
    val playerId: String,
    val dynastyId: String,
    val position: PlayerPosition,
    val classYear: PlayerClassYear,
    val isStarter: Boolean,
    val depthOrder: Int,
)

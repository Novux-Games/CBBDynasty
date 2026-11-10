package dev.novux.dynasty.cbb.core.player

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Reconstruct full attribute block from headline ratings when subs are not persisted. */
object RosterRatingSpread {
    fun fromHeadline(ovr: Int, off: Int, def: Int, position: PlayerPosition): PlayerRatings =
        PlayerBootstrapFactorySpread.fromHeadline(ovr, off, def, position)
}

/** Visible for [RosterRatingSpread]; implementation lives with bootstrap skew tables. */
internal object PlayerBootstrapFactorySpread {
    fun fromHeadline(ovr: Int, off: Int, def: Int, position: PlayerPosition): PlayerRatings {
        val threeBias = when (position) {
            PlayerPosition.ShootingGuard, PlayerPosition.SmallForward -> 6
            PlayerPosition.Center -> -8
            else -> 0
        }
        val handleBias = when (position) {
            PlayerPosition.PointGuard -> 8
            PlayerPosition.Center -> -6
            else -> 0
        }
        val rebBias = when (position) {
            PlayerPosition.Center, PlayerPosition.PowerForward -> 8
            PlayerPosition.PointGuard -> -6
            else -> 0
        }
        return PlayerRatings(
            ovr = ovr,
            off = off,
            def = def,
            shootingInside = (ovr + when (position) {
                PlayerPosition.Center, PlayerPosition.PowerForward -> 4
                else -> 0
            }).coerceIn(0, 99),
            shootingMid = (ovr + 2).coerceIn(0, 99),
            shootingThree = (ovr + threeBias).coerceIn(0, 99),
            handling = (ovr + handleBias).coerceIn(0, 99),
            passing = (off + handleBias / 2).coerceIn(0, 99),
            rebounding = (ovr + rebBias).coerceIn(0, 99),
            perimeterDefense = (def + when (position) {
                PlayerPosition.PointGuard, PlayerPosition.ShootingGuard -> 4
                else -> 0
            }).coerceIn(0, 99),
            interiorDefense = (def + when (position) {
                PlayerPosition.Center, PlayerPosition.PowerForward -> 4
                else -> 0
            }).coerceIn(0, 99),
            stealing = (def - 2).coerceIn(0, 99),
            blocking = (def + when (position) {
                PlayerPosition.Center -> 6
                else -> -2
            }).coerceIn(0, 99),
            potentialOverall = (ovr + 5).coerceIn(0, 99),
            proPotential = (ovr + 3).coerceIn(0, 99),
        )
    }
}

package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/**
 * Listed-position assignments follow lineup interchange rules before roster lock ([MEGA-RECR-107]).
 */
object SigningDayPositionFlexPolicy {
    const val MEGA_ID: String = "MEGA-RECR-107"

    fun allowedListedPositions(scoutedPosition: PlayerPosition): List<PlayerPosition> = when (scoutedPosition) {
        PlayerPosition.PointGuard -> listOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)
        PlayerPosition.ShootingGuard -> listOf(PlayerPosition.ShootingGuard, PlayerPosition.PointGuard)
        PlayerPosition.SmallForward -> listOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)
        PlayerPosition.PowerForward -> listOf(PlayerPosition.PowerForward, PlayerPosition.SmallForward)
        PlayerPosition.Center -> listOf(PlayerPosition.Center)
    }

    fun isValidAssignment(scoutedPosition: PlayerPosition, listedPosition: PlayerPosition): Boolean =
        listedPosition in allowedListedPositions(scoutedPosition)

    fun boardValid(
        signees: List<IncomingSignee>,
        scoutedByProspectId: Map<String, PlayerPosition>,
    ): Boolean = signees.all { signee ->
        val scouted = scoutedByProspectId[signee.prospectId] ?: signee.position
        isValidAssignment(scouted, signee.position)
    }
}
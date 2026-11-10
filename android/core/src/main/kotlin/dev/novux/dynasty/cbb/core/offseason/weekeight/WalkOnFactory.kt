package dev.novux.dynasty.cbb.core.offseason.weekeight

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

object WalkOnFactory {
    fun create(
        dynastyId: String,
        seasonIndex: Int,
        walkOnIndex: Int,
        position: PlayerPosition,
        depthOrder: Int,
    ): RosterPlayer =
        RosterPlayer(
            playerId = "walkon-$dynastyId-$seasonIndex-$walkOnIndex",
            dynastyId = dynastyId,
            position = position,
            classYear = PlayerClassYear.WalkOn,
            isStarter = false,
            depthOrder = depthOrder,
            inTransferPortal = false,
        )
}

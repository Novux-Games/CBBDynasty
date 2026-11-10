package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/**
 * Week 1 mutual exclusion — [planning/transfer-portal.md#week-1--roster-vs-transfer-tabs].
 */
object PortalWeekOnePartition {
    fun onRosterTab(players: List<RosterPlayer>): List<RosterPlayer> =
        players.filter { !it.inTransferPortal }

    fun onTransferTab(players: List<RosterPlayer>): List<RosterPlayer> =
        players.filter { it.inTransferPortal }

    fun playerVisibleOnRosterTab(player: RosterPlayer): Boolean = !player.inTransferPortal

    fun playerVisibleOnTransferTab(player: RosterPlayer): Boolean = player.inTransferPortal
}

object WeekOneLeavingActions {
    fun encourageTransfer(player: RosterPlayer): RosterPlayer {
        require(!player.inTransferPortal) { "player already in portal" }
        return player.copy(inTransferPortal = true)
    }

    fun convinceStay(player: RosterPlayer, stayed: Boolean): RosterPlayer {
        require(player.inTransferPortal) { "player not in portal" }
        return if (stayed) player.copy(inTransferPortal = false) else player
    }
}

object WeekOnePortalBootstrap {
    /**
     * Design lock: at least one user player in portal intent unless coach convinces all back.
     */
    fun ensureOnePortalEntrantIfEmpty(players: List<RosterPlayer>): List<RosterPlayer> {
        if (players.any { it.inTransferPortal }) return players
        val candidate = players.firstOrNull { !it.isStarter } ?: players.firstOrNull() ?: return players
        return players.map {
            if (it.playerId == candidate.playerId) it.copy(inTransferPortal = true) else it
        }
    }
}

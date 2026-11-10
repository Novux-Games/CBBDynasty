package dev.novux.dynasty.cbb.core.offseason.weekone

import dev.novux.dynasty.cbb.core.balance.BalanceLoyaltyTuning
import dev.novux.dynasty.cbb.core.player.loyalty.DeclareIntentRoll
import dev.novux.dynasty.cbb.core.player.loyalty.PromiseStatus
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
    fun ensureOnePortalEntrantIfEmpty(
        players: List<RosterPlayer>,
        tuning: BalanceLoyaltyTuning = BalanceLoyaltyTuning(),
    ): List<RosterPlayer> {
        if (players.any { it.inTransferPortal }) return players
        val candidate = pickPortalCandidate(players, tuning) ?: return players
        return players.map {
            if (it.playerId == candidate.playerId) it.copy(inTransferPortal = true) else it
        }
    }

    fun pickPortalCandidate(
        players: List<RosterPlayer>,
        tuning: BalanceLoyaltyTuning = BalanceLoyaltyTuning(),
    ): RosterPlayer? {
        val scored = players.map { player ->
            val dynasty = player.toDynastyPlayer()
            var weight = 0.0
            if (player.promiseStatusWire == PromiseStatus.Broken.wire) {
                weight += 2.0 + tuning.portalBrokenPromiseWeight
            }
            if (DeclareIntentRoll.shouldEnterPortalFromPromise(dynasty, tuning)) {
                weight += 1.0
            }
            weight += (50 - player.loyaltyRating).coerceAtLeast(0) / 100.0
            if (!player.isStarter) weight += 0.35
            player to weight
        }
        val best = scored.maxByOrNull { it.second }
        if (best != null && best.second > 0.4) return best.first
        return players.firstOrNull { !it.isStarter } ?: players.firstOrNull()
    }
}

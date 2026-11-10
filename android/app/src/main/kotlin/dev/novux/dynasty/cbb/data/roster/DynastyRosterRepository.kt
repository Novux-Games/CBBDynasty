package dev.novux.dynasty.cbb.data.roster

import dev.novux.dynasty.cbb.core.offseason.weekone.PortalWeekOnePartition
import dev.novux.dynasty.cbb.core.offseason.weekone.WeekOneLeavingActions
import dev.novux.dynasty.cbb.core.offseason.weekone.WeekOnePortalBootstrap
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.database.DynastyDatabase
import dev.novux.dynasty.cbb.database.roster.toDomain
import dev.novux.dynasty.cbb.database.roster.toEntity

class DynastyRosterRepository(
    database: DynastyDatabase,
) {
    private val dao = database.dynastyRosterDao()

    suspend fun listPlayers(dynastyId: String): List<RosterPlayer> =
        dao.listForDynasty(dynastyId).map { it.toDomain() }

    suspend fun prepareWeekOneLeaving(dynastyId: String): List<RosterPlayer> {
        var players = listPlayers(dynastyId)
        val bootstrapped = WeekOnePortalBootstrap.ensureOnePortalEntrantIfEmpty(players)
        if (bootstrapped != players) {
            bootstrapped.forEach { dao.upsert(it.toEntity()) }
            players = bootstrapped
        }
        return players
    }

    suspend fun encourageTransfer(dynastyId: String, playerId: String): List<RosterPlayer> {
        val player = listPlayers(dynastyId).first { it.playerId == playerId }
        dao.upsert(WeekOneLeavingActions.encourageTransfer(player).toEntity())
        return listPlayers(dynastyId)
    }

    suspend fun convinceStay(dynastyId: String, playerId: String, stayed: Boolean): List<RosterPlayer> {
        val player = listPlayers(dynastyId).first { it.playerId == playerId }
        dao.upsert(WeekOneLeavingActions.convinceStay(player, stayed).toEntity())
        return listPlayers(dynastyId)
    }

    fun rosterTab(players: List<RosterPlayer>): List<RosterPlayer> =
        PortalWeekOnePartition.onRosterTab(players)

    fun transferTab(players: List<RosterPlayer>): List<RosterPlayer> =
        PortalWeekOnePartition.onTransferTab(players)
}

package dev.novux.dynasty.cbb.data.roster

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.injuries.InjuryApplyContext
import dev.novux.dynasty.cbb.core.injuries.InjuryRosterOps
import dev.novux.dynasty.cbb.core.injuries.MedicalRedshirtPolicy
import dev.novux.dynasty.cbb.core.lineup.DefaultStarterAlgorithm
import dev.novux.dynasty.cbb.core.offseason.weekone.DraftStockLine
import dev.novux.dynasty.cbb.core.offseason.weekone.PortalWeekOnePartition
import dev.novux.dynasty.cbb.core.offseason.weekone.ProDayEligibility
import dev.novux.dynasty.cbb.core.offseason.weekone.ProDaySimulator
import dev.novux.dynasty.cbb.core.offseason.weekone.WeekOneLeavingActions
import dev.novux.dynasty.cbb.core.offseason.weekone.WeekOnePortalBootstrap
import dev.novux.dynasty.cbb.core.offseason.weekone.WeekTwoDraftResults
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResolution
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
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

    suspend fun listDynastyPlayers(dynastyId: String): List<DynastyPlayer> =
        listPlayers(dynastyId).map { it.toDynastyPlayer() }

    suspend fun prepareWeekOneLeaving(dynastyId: String): List<RosterPlayer> {
        var players = listPlayers(dynastyId)
        val bootstrapped = WeekOnePortalBootstrap.ensureOnePortalEntrantIfEmpty(players)
        if (bootstrapped != players) {
            bootstrapped.forEach { dao.upsert(it.toEntity()) }
            players = bootstrapped
        }
        val withDraft = ensureSeniorProDayEligibility(players)
        if (withDraft != players) {
            withDraft.filter { it.classYear == PlayerClassYear.Senior }.forEach { dao.upsert(it.toEntity()) }
            players = withDraft
        }
        return players
    }

    private fun ensureSeniorProDayEligibility(players: List<RosterPlayer>): List<RosterPlayer> =
        players.map { player ->
            if (player.classYear == PlayerClassYear.Senior && !player.inTransferPortal) {
                player.copy(declaredForDraft = true)
            } else {
                player
            }
        }

    suspend fun runProDay(dynastyId: String, playerId: String): List<RosterPlayer> {
        val player = listPlayers(dynastyId).first { it.playerId == playerId }
        dao.upsert(ProDaySimulator.run(player).toEntity())
        return listPlayers(dynastyId)
    }

    suspend fun simAllProDays(dynastyId: String): List<RosterPlayer> {
        val players = listPlayers(dynastyId)
        val eligible = players.filter { ProDayEligibility.eligible(it) && !it.proDayCompleted }
        ProDaySimulator.simAll(eligible).forEach { dao.upsert(it.toEntity()) }
        return listPlayers(dynastyId)
    }

    fun weekTwoDraftLines(players: List<RosterPlayer>): List<DraftStockLine> =
        WeekTwoDraftResults.fromRoster(players)

    suspend fun cutPlayer(dynastyId: String, playerId: String) {
        dao.deletePlayer(dynastyId, playerId)
    }

    suspend fun upsertPlayer(player: RosterPlayer) {
        dao.upsert(player.toEntity())
    }

    suspend fun applyNbaDraftResolution(dynastyId: String, resolution: NbaDraftResolution) {
        resolution.playersToRemove.forEach { playerId ->
            dao.deletePlayer(dynastyId, playerId)
        }
        resolution.playersToUpdate.forEach { dao.upsert(it.toEntity()) }
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

    suspend fun normalizeDefaultLineup(dynastyId: String): List<RosterPlayer> {
        val players = listPlayers(dynastyId)
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        val adjusted = DefaultStarterAlgorithm.applyToRoster(players, starters)
        adjusted.forEach { dao.upsert(it.toEntity()) }
        return adjusted
    }

    suspend fun tickInjuriesForWeekAdvance(dynastyId: String, phaseCommitted: CalendarPhase): List<RosterPlayer> {
        val ticked = InjuryRosterOps.tickWeekAdvance(phaseCommitted, listPlayers(dynastyId))
        ticked.players.forEach { dao.upsert(it.toEntity()) }
        return ticked.players
    }

    suspend fun applyGameInjuries(
        dynastyId: String,
        events: List<GameSimInjuryEvent>,
        context: InjuryApplyContext,
    ): List<RosterPlayer> {
        val updated = InjuryRosterOps.applyGameInjuries(listPlayers(dynastyId), events, context)
        updated.forEach { dao.upsert(it.toEntity()) }
        return updated
    }

    suspend fun applyMedicalRedshirt(dynastyId: String, playerId: String): List<RosterPlayer> {
        val player = listPlayers(dynastyId).first { it.playerId == playerId }
        val next = MedicalRedshirtPolicy.apply(player)
        dao.upsert(next.toEntity())
        return listPlayers(dynastyId)
    }
}

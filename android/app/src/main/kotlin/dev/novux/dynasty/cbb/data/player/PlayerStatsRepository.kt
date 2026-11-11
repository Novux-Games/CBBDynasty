package dev.novux.dynasty.cbb.data.player

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameMark
import dev.novux.dynasty.cbb.core.player.games.PlayerGameLogArchiveEngine
import dev.novux.dynasty.cbb.core.player.games.PlayerGameLogEntry
import dev.novux.dynasty.cbb.core.player.games.PlayerGameLogMerge
import dev.novux.dynasty.cbb.core.player.games.PlayerGameLogSynth
import dev.novux.dynasty.cbb.core.player.stats.PlayerCountingStats
import dev.novux.dynasty.cbb.core.player.history.PlayerPriorSeasonSnapshot
import dev.novux.dynasty.cbb.core.player.stats.PlayerStatsEngine
import dev.novux.dynasty.cbb.core.player.stats.PlayerStatsSummary
import dev.novux.dynasty.cbb.core.player.stats.PlayerStatsSynth
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.playerStatsStore: DataStore<Preferences> by preferencesDataStore(name = "player_stats")

@Serializable
data class PlayerSeasonStatRecord(
    val playerId: String,
    val seasonIndex: Int,
    val gamesPlayed: Int,
    val totals: PlayerCountingStats,
    val schoolDisplayName: String? = null,
)

@Serializable
data class PlayerGameLogRecord(
    val playerId: String,
    val seasonIndex: Int,
    val gamesPlayed: Int,
    val entries: List<PlayerGameLogEntry> = emptyList(),
)

@Serializable
data class DynastyPlayerStatsBlob(
    val records: List<PlayerSeasonStatRecord> = emptyList(),
    val gameLogs: List<PlayerGameLogRecord> = emptyList(),
)

class PlayerStatsRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun priorSeasonsForPlayer(
        dynastyId: String,
        playerId: String,
        currentSeasonIndex: Int,
        currentSchoolDisplayName: String? = null,
    ): List<PlayerPriorSeasonSnapshot> {
        val blob = loadBlob(dynastyId)
        return blob.records
            .asSequence()
            .filter { it.playerId == playerId && it.seasonIndex < currentSeasonIndex && it.gamesPlayed > 0 }
            .sortedByDescending { it.seasonIndex }
            .map {
                PlayerPriorSeasonSnapshot(
                    seasonIndex = it.seasonIndex,
                    gamesPlayed = it.gamesPlayed,
                    totals = it.totals,
                    schoolDisplayName = it.schoolDisplayName?.takeIf { name -> name.isNotBlank() }
                        ?: currentSchoolDisplayName,
                )
            }
            .toList()
    }

    suspend fun gameLogFor(
        dynastyId: String,
        seasonIndex: Int,
        player: DynastyPlayer,
    ): List<PlayerGameLogEntry> {
        val gp = player.role.rsGamesPlayedSeason.coerceAtLeast(0)
        ensureSeasonRecord(dynastyId, player, seasonIndex, gp, schoolDisplayName = null)
        ensureGameLogRecord(dynastyId, player, seasonIndex, gp)
        val blob = loadBlob(dynastyId)
        return blob.gameLogs.first {
            it.playerId == player.playerId && it.seasonIndex == seasonIndex
        }.entries
    }

    /** Persists GP and counting totals after week checkpoint commits official games ([MEGA-COACH-149]). */
    suspend fun ingestCommittedSimMarks(
        dynastyId: String,
        seasonIndex: Int,
        entriesByPlayerId: Map<String, List<PlayerGameLogEntry>>,
    ) {
        if (entriesByPlayerId.isEmpty()) return
        val blob = loadBlob(dynastyId)
        var gameLogs = blob.gameLogs
        entriesByPlayerId.forEach { (playerId, incoming) ->
            if (incoming.isEmpty()) return@forEach
            val prior = gameLogs.firstOrNull { it.playerId == playerId && it.seasonIndex == seasonIndex }
            val kept = prior?.entries?.filter { existing ->
                incoming.none { it.gameKey == existing.gameKey }
            }.orEmpty()
            val combined = (kept + incoming).sortedByDescending { it.rsWeekIndex }
            val record = PlayerGameLogRecord(
                playerId = playerId,
                seasonIndex = seasonIndex,
                gamesPlayed = maxOf(prior?.gamesPlayed ?: 0, combined.size),
                entries = combined,
            )
            gameLogs = gameLogs.filterNot { it.playerId == playerId && it.seasonIndex == seasonIndex } + record
        }
        persist(dynastyId, blob.copy(gameLogs = gameLogs))
    }

    suspend fun persistSeasonStatsForRoster(
        dynastyId: String,
        seasonIndex: Int,
        players: List<DynastyPlayer>,
        schoolDisplayName: String,
    ) {
        players.forEach { player ->
            val gp = player.role.rsGamesPlayedSeason.coerceAtLeast(0)
            ensureSeasonRecord(dynastyId, player, seasonIndex, gp, schoolDisplayName)
        }
    }

    suspend fun seasonTotalsByPlayer(
        dynastyId: String,
        seasonIndex: Int,
    ): Map<String, PlayerCountingStats> {
        val blob = loadBlob(dynastyId)
        return blob.records
            .filter { it.seasonIndex == seasonIndex }
            .associate { it.playerId to it.totals }
    }

    suspend fun summaryFor(
        dynastyId: String,
        seasonIndex: Int,
        player: DynastyPlayer,
    ): PlayerStatsSummary {
        val gp = player.role.rsGamesPlayedSeason.coerceAtLeast(0)
        ensureSeasonRecord(dynastyId, player, seasonIndex, gp, schoolDisplayName = null)
        val blob = loadBlob(dynastyId)
        val currentTotals = blob.records.first {
            it.playerId == player.playerId && it.seasonIndex == seasonIndex
        }.totals
        val playerRows = blob.records.filter { it.playerId == player.playerId }.sortedBy { it.seasonIndex }
        val prior = playerRows.filter { it.seasonIndex < seasonIndex }
        return PlayerStatsEngine.seasonSummary(
            gamesPlayed = gp,
            seasonTotals = currentTotals,
            priorSeasonTotals = prior.map { it.totals },
            priorGamesPlayed = prior.map { it.gamesPlayed },
        )
    }

    private suspend fun ensureSeasonRecord(
        dynastyId: String,
        player: DynastyPlayer,
        seasonIndex: Int,
        gamesPlayed: Int,
        schoolDisplayName: String?,
    ) {
        val blob = loadBlob(dynastyId)
        val existing = blob.records.firstOrNull {
            it.playerId == player.playerId && it.seasonIndex == seasonIndex
        }
        val totals = when {
            existing != null && existing.gamesPlayed == gamesPlayed -> existing.totals
            gamesPlayed <= 0 -> PlayerCountingStats.ZERO
            else -> PlayerStatsSynth.totalsForSeason(player, gamesPlayed)
        }
        val school = schoolDisplayName?.takeIf { it.isNotBlank() } ?: existing?.schoolDisplayName
        val record = PlayerSeasonStatRecord(
            playerId = player.playerId,
            seasonIndex = seasonIndex,
            gamesPlayed = gamesPlayed,
            totals = totals,
            schoolDisplayName = school,
        )
        if (existing == record) return
        val next = blob.copy(
            records = blob.records
                .filterNot { it.playerId == player.playerId && it.seasonIndex == seasonIndex } + record,
        )
        persist(dynastyId, next)
    }

    private suspend fun ensureGameLogRecord(
        dynastyId: String,
        player: DynastyPlayer,
        seasonIndex: Int,
        gamesPlayed: Int,
    ) {
        val blob = loadBlob(dynastyId)
        val totals = blob.records.first {
            it.playerId == player.playerId && it.seasonIndex == seasonIndex
        }.totals
        val existing = blob.gameLogs.firstOrNull {
            it.playerId == player.playerId && it.seasonIndex == seasonIndex
        }
        val archived = existing?.entries.orEmpty()
        val entries = when {
            existing != null && existing.gamesPlayed == gamesPlayed && archived.isNotEmpty() ->
                PlayerGameLogMerge.mergeForSeason(
                    player = player,
                    seasonIndex = seasonIndex,
                    gamesPlayed = gamesPlayed,
                    seasonTotals = totals,
                    archived = archived,
                )
            gamesPlayed <= 0 -> emptyList()
            archived.any { it.fromSimArchive } ->
                PlayerGameLogMerge.mergeForSeason(
                    player = player,
                    seasonIndex = seasonIndex,
                    gamesPlayed = gamesPlayed,
                    seasonTotals = totals,
                    archived = archived,
                )
            else -> PlayerGameLogSynth.buildLog(player, seasonIndex, gamesPlayed, totals)
        }
        val record = PlayerGameLogRecord(
            playerId = player.playerId,
            seasonIndex = seasonIndex,
            gamesPlayed = gamesPlayed,
            entries = entries,
        )
        if (existing == record) return
        val next = blob.copy(
            gameLogs = blob.gameLogs
                .filterNot { it.playerId == player.playerId && it.seasonIndex == seasonIndex } + record,
        )
        persist(dynastyId, next)
    }

    private suspend fun loadBlob(dynastyId: String): DynastyPlayerStatsBlob {
        val key = blobKey(dynastyId)
        val prefs = context.playerStatsStore.data.first()
        val raw = prefs[key] ?: return DynastyPlayerStatsBlob()
        return runCatching { json.decodeFromString<DynastyPlayerStatsBlob>(raw) }
            .getOrElse { DynastyPlayerStatsBlob() }
    }

    private suspend fun persist(dynastyId: String, blob: DynastyPlayerStatsBlob) {
        val key = blobKey(dynastyId)
        context.playerStatsStore.edit { prefs ->
            prefs[key] = json.encodeToString(blob)
        }
    }

    private fun blobKey(dynastyId: String) = stringPreferencesKey("blob_$dynastyId")
}

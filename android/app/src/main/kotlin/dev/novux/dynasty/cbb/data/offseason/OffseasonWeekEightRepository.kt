package dev.novux.dynasty.cbb.data.offseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.offseason.weekeight.RosterLockPolicy
import dev.novux.dynasty.cbb.core.offseason.weekeight.WalkOnFactory
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import kotlinx.coroutines.flow.first

private val Context.offseasonWeekEightStore: DataStore<Preferences> by preferencesDataStore(
    name = "offseason_week_eight",
)

class OffseasonWeekEightRepository(
    private val context: Context,
    private val rosterRepository: DynastyRosterRepository,
) {
    suspend fun walkOnsSigned(dynastyId: String, seasonIndex: Int): Int {
        val key = walkOnKey(dynastyId, seasonIndex)
        return context.offseasonWeekEightStore.data.first()[key] ?: 0
    }

    suspend fun isRosterLocked(dynastyId: String, seasonIndex: Int): Boolean =
        context.offseasonWeekEightStore.data.first()[lockKey(dynastyId, seasonIndex)] == true

    suspend fun cutPlayer(dynastyId: String, playerId: String) {
        rosterRepository.cutPlayer(dynastyId, playerId)
    }

    suspend fun offerWalkOn(
        dynastyId: String,
        seasonIndex: Int,
        position: PlayerPosition = PlayerPosition.PointGuard,
    ): Boolean {
        val players = rosterRepository.listPlayers(dynastyId)
        val signed = walkOnsSigned(dynastyId, seasonIndex)
        if (!RosterLockPolicy.canOfferWalkOn(players, signed)) return false
        val nextIndex = signed + 1
        val depth = (players.maxOfOrNull { it.depthOrder } ?: 0) + 1
        val player = WalkOnFactory.create(dynastyId, seasonIndex, nextIndex, position, depth)
        rosterRepository.upsertPlayer(player)
        context.offseasonWeekEightStore.edit { prefs ->
            prefs[walkOnKey(dynastyId, seasonIndex)] = nextIndex
        }
        return true
    }

    suspend fun markRosterLocked(dynastyId: String, seasonIndex: Int) {
        context.offseasonWeekEightStore.edit { prefs ->
            prefs[lockKey(dynastyId, seasonIndex)] = true
        }
    }

    private fun walkOnKey(dynastyId: String, seasonIndex: Int) =
        intPreferencesKey("walkons_${dynastyId}_$seasonIndex")

    private fun lockKey(dynastyId: String, seasonIndex: Int) =
        booleanPreferencesKey("roster_lock_${dynastyId}_$seasonIndex")
}

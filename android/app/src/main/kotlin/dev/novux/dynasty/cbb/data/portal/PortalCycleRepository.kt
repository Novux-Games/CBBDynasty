package dev.novux.dynasty.cbb.data.portal

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.portal.PortalCycleState
import dev.novux.dynasty.cbb.core.portal.PortalSimulationEngine
import kotlinx.coroutines.flow.first
import kotlin.random.Random

private val Context.portalCycleStore: DataStore<Preferences> by preferencesDataStore(
    name = "portal_cycle",
)

class PortalCycleRepository(
    private val context: Context,
) {
    suspend fun loadOrSimulate(dynastyId: String, membershipSeasonIndex: Int): PortalCycleState {
        val prefs = context.portalCycleStore.data.first()
        val raw = prefs[prefKey(dynastyId)]
        if (raw != null) {
            val snap = decode(raw)
            if (snap.seasonIndex == membershipSeasonIndex) {
                return restore(snap)
            }
        }
        val seed = kotlin.random.Random.nextLong()
        val state = PortalSimulationEngine.simulateOffseasonCycle(membershipSeasonIndex, Random(seed))
        persist(dynastyId, Snap(membershipSeasonIndex, seed, top40Locked = false))
        return state
    }

    suspend fun onOffseasonWeekStarted(
        dynastyId: String,
        membershipSeasonIndex: Int,
        weekIndex: Int,
    ): PortalCycleState {
        val state = loadOrSimulate(dynastyId, membershipSeasonIndex)
        val next = state.onOffseasonWeekStarted(weekIndex)
        if (next.top40Locked && !state.top40Locked) {
            val snap = decode(
                context.portalCycleStore.data.first()[prefKey(dynastyId)]
                    ?: error("portal snapshot missing"),
            )
            persist(dynastyId, snap.copy(top40Locked = true))
        }
        return next
    }

    private fun restore(snap: Snap): PortalCycleState {
        val base = PortalSimulationEngine.simulateOffseasonCycle(snap.seasonIndex, Random(snap.seed))
        return if (snap.top40Locked) base.onOffseasonWeekStarted(2) else base
    }

    private suspend fun persist(dynastyId: String, snap: Snap) {
        context.portalCycleStore.edit { prefs ->
            prefs[prefKey(dynastyId)] = encode(snap)
        }
    }

    private fun prefKey(dynastyId: String) = stringPreferencesKey("portal_$dynastyId")

    private fun encode(snap: Snap): String =
        "${snap.seasonIndex}|${snap.seed}|${snap.top40Locked}"

    private fun decode(raw: String): Snap {
        val parts = raw.split("|")
        require(parts.size == 3)
        return Snap(
            seasonIndex = parts[0].toInt(),
            seed = parts[1].toLong(),
            top40Locked = parts[2].toBooleanStrict(),
        )
    }

    private data class Snap(
        val seasonIndex: Int,
        val seed: Long,
        val top40Locked: Boolean,
    )
}

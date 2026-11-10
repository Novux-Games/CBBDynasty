package dev.novux.dynasty.cbb.data.league

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.league.LeagueScheduleGenerator
import dev.novux.dynasty.cbb.core.league.LeagueScheduleSeason
import dev.novux.dynasty.cbb.data.pack.PackRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.leagueScheduleStore: DataStore<Preferences> by preferencesDataStore(
    name = "league_schedules",
)

class LeagueScheduleRepository(
    private val context: Context,
    private val packRepository: PackRepository,
) {
    suspend fun load(dynastyId: String, seasonIndex: Int): LeagueScheduleSeason? {
        val raw = context.leagueScheduleStore.data.first()[key(dynastyId, seasonIndex)] ?: return null
        return Json.decodeFromString(raw)
    }

    suspend fun generateAndPersist(
        dynastyId: String,
        seasonIndex: Int,
    ): LeagueScheduleSeason {
        val existing = load(dynastyId, seasonIndex)
        if (existing != null) return existing
        val session = packRepository.activeSession()
        val teams = session.index.allTeamDisplays()
        val conferences = session.index.allConferenceDisplays().associateBy { it.conferenceId }
        val season = LeagueScheduleGenerator.generate(
            teams = teams,
            conferences = conferences,
            seasonIndex = seasonIndex,
            dynastySeed = dynastyId.hashCode().toLong(),
        )
        persist(dynastyId, season)
        return season
    }

    private suspend fun persist(dynastyId: String, season: LeagueScheduleSeason) {
        context.leagueScheduleStore.edit {
            it[key(dynastyId, season.seasonIndex)] = Json.encodeToString(season)
        }
    }

    private fun key(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("league_${dynastyId}_$seasonIndex")
}

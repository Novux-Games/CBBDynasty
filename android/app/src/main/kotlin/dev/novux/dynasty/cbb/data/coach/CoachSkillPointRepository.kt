package dev.novux.dynasty.cbb.data.coach

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointState
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointStateReducer
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.coachSkillPointStore: DataStore<Preferences> by preferencesDataStore(
    name = "coach_skill_points",
)

class CoachSkillPointRepository(private val context: Context) {
    suspend fun loadState(dynastyId: String): CoachSkillPointState {
        val raw = context.coachSkillPointStore.data.first()[key(dynastyId)] ?: return CoachSkillPointState()
        return Json.decodeFromString(raw)
    }

    suspend fun ensureSundayGrant(dynastyId: String, position: CalendarPosition): CoachSkillPointState {
        val current = loadState(dynastyId)
        val next = CoachSkillPointStateReducer.ensureSundayGrant(current, position)
        if (next != current) {
            persist(dynastyId, next)
        }
        return next
    }

    private suspend fun persist(dynastyId: String, state: CoachSkillPointState) {
        context.coachSkillPointStore.edit {
            it[key(dynastyId)] = Json.encodeToString(state)
        }
    }

    private fun key(dynastyId: String) = stringPreferencesKey("coach_skill_$dynastyId")
}

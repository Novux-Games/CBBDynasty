package dev.novux.dynasty.cbb.data.coach

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.skill.CoachAutoSpendEngine
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillBranch
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointState
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillPointStateReducer
import dev.novux.dynasty.cbb.core.coach.skill.CoachSkillTreeEngine
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

    suspend fun ensureSundayGrant(
        dynastyId: String,
        position: CalendarPosition,
        archetype: CoachArchetype = CoachArchetype.Balanced,
    ): CoachSkillPointState {
        val current = loadState(dynastyId)
        val next = CoachSkillPointStateReducer.ensureSundayGrant(current, position, archetype)
        if (next != current) {
            persist(dynastyId, next)
        }
        return next
    }

    suspend fun spendOnBranch(dynastyId: String, branch: CoachSkillBranch): CoachSkillPointState {
        val current = loadState(dynastyId)
        val next = CoachSkillTreeEngine.spend(current, branch)
        persist(dynastyId, next)
        return next
    }

    suspend fun setAutoSpend(dynastyId: String, enabled: Boolean, archetype: CoachArchetype): CoachSkillPointState {
        var next = loadState(dynastyId).copy(autoSpendEnabled = enabled)
        if (enabled && next.unspentBank > 0) {
            next = CoachAutoSpendEngine.applyAll(next, archetype)
        }
        persist(dynastyId, next)
        return next
    }

    private suspend fun persist(dynastyId: String, state: CoachSkillPointState) {
        context.coachSkillPointStore.edit {
            it[key(dynastyId)] = Json.encodeToString(state)
        }
    }

    private fun key(dynastyId: String) = stringPreferencesKey("coach_skill_$dynastyId")
}

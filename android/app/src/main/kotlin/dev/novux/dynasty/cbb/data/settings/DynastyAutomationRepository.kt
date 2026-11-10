package dev.novux.dynasty.cbb.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.settings.AppSettings
import dev.novux.dynasty.cbb.core.settings.DynastyAutomationSettings
import kotlinx.coroutines.flow.first

private val Context.dynastyAutomationStore: DataStore<Preferences> by preferencesDataStore(
    name = "dynasty_automation",
)

class DynastyAutomationRepository(private val context: Context) {
    suspend fun load(dynastyId: String): DynastyAutomationSettings {
        val prefs = context.dynastyAutomationStore.data.first()
        val recruit = prefs[recruitKey(dynastyId)] ?: DynastyAutomationSettings().recruitingAutoMaintain
        val coach = prefs[coachKey(dynastyId)] ?: DynastyAutomationSettings().coachAutoSpend
        return DynastyAutomationSettings(recruitingAutoMaintain = recruit, coachAutoSpend = coach)
    }

    suspend fun save(dynastyId: String, settings: DynastyAutomationSettings) {
        context.dynastyAutomationStore.edit { prefs ->
            prefs[recruitKey(dynastyId)] = settings.recruitingAutoMaintain
            prefs[coachKey(dynastyId)] = settings.coachAutoSpend
        }
    }

    suspend fun seedFromAppDefaults(dynastyId: String, appSettings: AppSettings) {
        save(
            dynastyId,
            DynastyAutomationSettings(
                recruitingAutoMaintain = appSettings.defaultRecruitingAutoMaintain,
                coachAutoSpend = appSettings.defaultCoachAutoSpend,
            ),
        )
    }

    private fun recruitKey(dynastyId: String) = booleanPreferencesKey("auto_recruit_$dynastyId")
    private fun coachKey(dynastyId: String) = booleanPreferencesKey("auto_coach_$dynastyId")
}

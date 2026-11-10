package dev.novux.dynasty.cbb.data.preseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskProgress
import kotlinx.coroutines.flow.first

private val Context.preseasonProgressStore: DataStore<Preferences> by preferencesDataStore(
    name = "preseason_progress",
)

class PreseasonProgressRepository(private val context: Context) {
    suspend fun load(dynastyId: String, seasonIndex: Int): PreseasonTaskProgress {
        val raw = context.preseasonProgressStore.data.first()[key(dynastyId, seasonIndex)]
        return decode(raw)
    }

    suspend fun completeTask(
        dynastyId: String,
        seasonIndex: Int,
        taskId: PreseasonTaskId,
    ): PreseasonTaskProgress {
        val current = load(dynastyId, seasonIndex)
        if (!current.canComplete(taskId)) return current
        val next = current.withComplete(taskId)
        persist(dynastyId, seasonIndex, next)
        return next
    }

    private suspend fun persist(dynastyId: String, seasonIndex: Int, progress: PreseasonTaskProgress) {
        context.preseasonProgressStore.edit { prefs ->
            prefs[key(dynastyId, seasonIndex)] = encode(progress)
        }
    }

    private fun key(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("preseason_${dynastyId}_$seasonIndex")

    private fun encode(progress: PreseasonTaskProgress): String =
        progress.completed.joinToString(",") { it.name }

    private fun decode(raw: String?): PreseasonTaskProgress {
        if (raw.isNullOrBlank()) return PreseasonTaskProgress()
        val set = raw.split(",").mapNotNull { name ->
            runCatching { PreseasonTaskId.valueOf(name) }.getOrNull()
        }.toSet()
        return PreseasonTaskProgress(set)
    }
}

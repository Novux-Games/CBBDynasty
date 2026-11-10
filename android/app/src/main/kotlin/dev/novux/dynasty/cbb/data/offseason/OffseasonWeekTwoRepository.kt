package dev.novux.dynasty.cbb.data.offseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftOutcome
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResultLine
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResultsEngine
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import kotlinx.coroutines.flow.first

private val Context.offseasonWeekTwoStore: DataStore<Preferences> by preferencesDataStore(
    name = "offseason_week_two",
)

class OffseasonWeekTwoRepository(
    private val context: Context,
    private val rosterRepository: DynastyRosterRepository,
) {
    suspend fun loadPublishedResults(dynastyId: String, seasonIndex: Int): List<NbaDraftResultLine> {
        val raw = context.offseasonWeekTwoStore.data.first()[resultsKey(dynastyId, seasonIndex)] ?: return emptyList()
        return decodeResults(raw)
    }

    suspend fun ensureDraftResultsPublished(dynastyId: String, seasonIndex: Int): List<NbaDraftResultLine> {
        val existing = loadPublishedResults(dynastyId, seasonIndex)
        if (existing.isNotEmpty()) return existing
        val players = rosterRepository.listPlayers(dynastyId)
        val resolution = NbaDraftResultsEngine.resolve(players)
        rosterRepository.applyNbaDraftResolution(dynastyId, resolution)
        val lines = resolution.resultLines
        context.offseasonWeekTwoStore.edit { prefs ->
            prefs[resultsKey(dynastyId, seasonIndex)] = encodeResults(lines)
            prefs[scholarshipsKey(dynastyId, seasonIndex)] = true
        }
        return lines
    }

    suspend fun scholarshipsOpenRecorded(dynastyId: String, seasonIndex: Int): Boolean =
        context.offseasonWeekTwoStore.data.first()[scholarshipsKey(dynastyId, seasonIndex)] == true

    private fun resultsKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("draft_results_${dynastyId}_$seasonIndex")

    private fun scholarshipsKey(dynastyId: String, seasonIndex: Int) =
        booleanPreferencesKey("scholarships_open_${dynastyId}_$seasonIndex")

    private fun encodeResults(lines: List<NbaDraftResultLine>): String =
        lines.joinToString(";") { line ->
            listOf(
                line.playerId,
                line.displayLabel,
                line.stockPoints,
                line.mockTier,
                line.outcome.name,
                line.pickSummary.orEmpty(),
            ).joinToString("|")
        }

    private fun decodeResults(raw: String): List<NbaDraftResultLine> =
        raw.split(";").filter { it.isNotBlank() }.map { token ->
            val p = token.split("|")
            require(p.size == 6)
            NbaDraftResultLine(
                playerId = p[0],
                displayLabel = p[1],
                stockPoints = p[2].toInt(),
                mockTier = p[3],
                outcome = NbaDraftOutcome.valueOf(p[4]),
                pickSummary = p[5].takeIf { it.isNotEmpty() },
            )
        }
}

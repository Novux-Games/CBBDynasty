package dev.novux.dynasty.cbb.data.offseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.offseason.weekseven.SummerWorkoutReport
import dev.novux.dynasty.cbb.core.offseason.weekseven.SummerWorkoutReportLine
import dev.novux.dynasty.cbb.core.offseason.weekseven.SummerWorkoutSimulator
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import kotlinx.coroutines.flow.first

private val Context.summerWorkoutStore: DataStore<Preferences> by preferencesDataStore(
    name = "offseason_summer_workouts",
)

class OffseasonSummerWorkoutRepository(
    private val context: Context,
    private val rosterRepository: DynastyRosterRepository,
) {
    suspend fun loadReport(dynastyId: String, seasonIndex: Int): SummerWorkoutReport? {
        val raw = context.summerWorkoutStore.data.first()[reportKey(dynastyId, seasonIndex)] ?: return null
        return decodeReport(raw)
    }

    suspend fun ensureSimulated(dynastyId: String, seasonIndex: Int): SummerWorkoutReport {
        loadReport(dynastyId, seasonIndex)?.let { return it }
        val players = rosterRepository.listPlayers(dynastyId)
        val report = SummerWorkoutSimulator.simulate(
            players = players,
            dynastySeed = dynastyId.hashCode().toLong() + seasonIndex,
        )
        context.summerWorkoutStore.edit { prefs ->
            prefs[reportKey(dynastyId, seasonIndex)] = encodeReport(report)
        }
        return report
    }

    private fun reportKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("summer_workout_${dynastyId}_$seasonIndex")

    private fun encodeReport(report: SummerWorkoutReport): String {
        val header = "${report.coachTrainingGrade}|${report.teamAverageGain}"
        val body = report.lines.joinToString(";") { line ->
            listOf(line.playerId, line.displayLabel, line.gainPoints, line.summary).joinToString("|")
        }
        return "$header:::$body"
    }

    private fun decodeReport(raw: String): SummerWorkoutReport {
        val parts = raw.split(":::", limit = 2)
        require(parts.size == 2)
        val header = parts[0].split("|")
        val coach = header[0].toInt()
        val avg = header[1].toInt()
        val lines = parts[1].split(";").filter { it.isNotBlank() }.map { token ->
            val p = token.split("|")
            require(p.size == 4)
            SummerWorkoutReportLine(
                playerId = p[0],
                displayLabel = p[1],
                gainPoints = p[2].toInt(),
                summary = p[3],
            )
        }
        return SummerWorkoutReport(coach, lines, avg)
    }
}

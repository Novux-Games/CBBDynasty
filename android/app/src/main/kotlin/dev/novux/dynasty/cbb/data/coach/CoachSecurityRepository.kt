package dev.novux.dynasty.cbb.data.coach

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.coach.security.CoachSecurityEvaluator
import dev.novux.dynasty.cbb.core.coach.security.CoachSecuritySnapshot
import dev.novux.dynasty.cbb.core.coach.security.JobSecurityTrend
import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first

private val Context.coachSecurityStore: DataStore<Preferences> by preferencesDataStore(
    name = "coach_security",
)

class CoachSecurityRepository(
    private val context: Context,
    private val database: DynastyDatabase,
) {
    suspend fun loadOrBootstrap(
        dynastyId: String,
        contractYearsLeft: Int = 4,
        record: SchoolSeasonRecord = SchoolSeasonRecord.Zero,
    ): CoachSecuritySnapshot {
        val jobKey = jobScoreKey(dynastyId)
        val adKey = adScoreKey(dynastyId)
        val trendKey = trendKey(dynastyId)
        val prefs = context.coachSecurityStore.data.first()
        if (prefs[jobKey] == null) {
            val boot = CoachSecurityEvaluator.bootstrap(contractYearsLeft)
            context.coachSecurityStore.edit {
                it[jobKey] = boot.jobSecurity.score
                it[adKey] = boot.adTrust.score
                it[trendKey] = boot.jobSecurity.trend.wire
            }
            return boot
        }
        return CoachSecurityEvaluator.build(
            jobSecurityScore = prefs[jobKey] ?: 72,
            adTrustScore = prefs[adKey] ?: 68,
            trend = JobSecurityTrend.fromWire(prefs[trendKey] ?: JobSecurityTrend.Flat.wire),
            contractYearsLeft = contractYearsLeft,
            record = record,
        )
    }

    suspend fun loadForUserDynasty(
        dynastyId: String,
        record: SchoolSeasonRecord = SchoolSeasonRecord.Zero,
    ): CoachSecuritySnapshot? {
        database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        return loadOrBootstrap(dynastyId, contractYearsLeft = 4, record = record)
    }

    private fun jobScoreKey(dynastyId: String) = intPreferencesKey("job_$dynastyId")
    private fun adScoreKey(dynastyId: String) = intPreferencesKey("ad_$dynastyId")
    private fun trendKey(dynastyId: String) = stringPreferencesKey("trend_$dynastyId")
}

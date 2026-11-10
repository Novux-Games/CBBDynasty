package dev.novux.dynasty.cbb.data.coach

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.coach.employment.CoachEmploymentState
import dev.novux.dynasty.cbb.core.coach.employment.CoachEmploymentStatus
import dev.novux.dynasty.cbb.core.coach.employment.JobCenterEngine
import dev.novux.dynasty.cbb.core.coach.employment.JobOfferCard
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.coachEmploymentStore: DataStore<Preferences> by preferencesDataStore(
    name = "coach_employment",
)

class CoachEmploymentRepository(
    private val context: Context,
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadState(dynastyId: String): CoachEmploymentState {
        val raw = context.coachEmploymentStore.data.first()[key(dynastyId)] ?: return CoachEmploymentState()
        return decode(raw)
    }

    suspend fun enterUnemployed(dynastyId: String, seasonIndex: Int, coachReputation: Int = 55): CoachEmploymentState {
        val offers = refreshOffers(dynastyId, seasonIndex, coachReputation)
        val state = CoachEmploymentState(
            status = CoachEmploymentStatus.Unemployed,
            offersSeasonIndex = seasonIndex,
            pendingOffers = offers,
        )
        persist(dynastyId, state)
        return state
    }

    suspend fun refreshOffersIfNeeded(
        dynastyId: String,
        seasonIndex: Int,
        coachReputation: Int = 55,
    ): CoachEmploymentState {
        val current = loadState(dynastyId)
        if (current.status != CoachEmploymentStatus.Unemployed) return current
        if (current.offersSeasonIndex == seasonIndex && current.pendingOffers.isNotEmpty()) return current
        val next = current.copy(
            offersSeasonIndex = seasonIndex,
            pendingOffers = refreshOffers(dynastyId, seasonIndex, coachReputation),
        )
        persist(dynastyId, next)
        return next
    }

    suspend fun acceptOffer(dynastyId: String, offer: JobOfferCard): CoachEmploymentState {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId)
            ?: error("coach missing")
        database.dynastyCoachDao().upsert(coach.copy(userTeamId = offer.teamId))
        val employed = CoachEmploymentState(status = CoachEmploymentStatus.Employed)
        persist(dynastyId, employed)
        return employed
    }

    private suspend fun refreshOffers(
        dynastyId: String,
        seasonIndex: Int,
        coachReputation: Int,
    ): List<JobOfferCard> {
        val teams = packRepository.activeSession().index.allTeamDisplays()
        return JobCenterEngine.generateOffers(teams, seasonIndex, dynastyId, coachReputation)
    }

    private suspend fun persist(dynastyId: String, state: CoachEmploymentState) {
        context.coachEmploymentStore.edit { prefs ->
            prefs[key(dynastyId)] = encode(state)
        }
    }

    private fun key(dynastyId: String) = stringPreferencesKey("employment_$dynastyId")

    private fun encode(state: CoachEmploymentState): String = json.encodeToString(state)

    private fun decode(raw: String): CoachEmploymentState = json.decodeFromString(raw)
}

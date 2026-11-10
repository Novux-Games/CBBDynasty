package dev.novux.dynasty.cbb.data.recruiting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.recruiting.RecruitingRefundableAction
import dev.novux.dynasty.cbb.core.recruiting.RecruitingWeekReservation
import kotlinx.coroutines.flow.first

private val Context.recruitingReservationStore: DataStore<Preferences> by preferencesDataStore(
    name = "recruiting_reservations",
)

class RecruitingReservationRepository(
    private val context: Context,
) {
    suspend fun loadActive(dynastyId: String): List<RecruitingWeekReservation> {
        val raw = context.recruitingReservationStore.data.first()[prefKey(dynastyId)] ?: return emptyList()
        return decode(raw)
    }

    suspend fun saveActive(dynastyId: String, reservations: List<RecruitingWeekReservation>) {
        context.recruitingReservationStore.edit { prefs ->
            prefs[prefKey(dynastyId)] = encode(reservations)
        }
    }

    suspend fun addReservation(dynastyId: String, reservation: RecruitingWeekReservation) {
        val next = loadActive(dynastyId) + reservation
        saveActive(dynastyId, next)
    }

    private fun prefKey(dynastyId: String) = stringPreferencesKey("reservations_$dynastyId")

    private fun encode(reservations: List<RecruitingWeekReservation>): String =
        reservations.joinToString(";") { r ->
            "${r.reservationId}|${r.prospectId}|${r.action.name}|${r.pointsReserved}"
        }

    private fun decode(raw: String): List<RecruitingWeekReservation> =
        raw.split(";").filter { it.isNotBlank() }.map { token ->
            val parts = token.split("|")
            require(parts.size == 4)
            RecruitingWeekReservation(
                reservationId = parts[0],
                prospectId = parts[1],
                action = RecruitingRefundableAction.valueOf(parts[2]),
                pointsReserved = parts[3].toInt(),
            )
        }
}

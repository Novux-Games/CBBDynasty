package dev.novux.dynasty.cbb.data.recruiting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.recruiting.RecruitingRefundableAction
import dev.novux.dynasty.cbb.core.recruiting.RecruitingSeasonConsumption
import dev.novux.dynasty.cbb.core.recruiting.RecruitingWeekReservation
import dev.novux.dynasty.cbb.core.recruiting.RecruitingWeekReservationClose
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

    suspend fun loadSeasonConsumption(dynastyId: String, cycleSeasonIndex: Int): RecruitingSeasonConsumption {
        val raw = context.recruitingReservationStore.data.first()[consumptionKey(dynastyId)] ?: return RecruitingSeasonConsumption(cycleSeasonIndex)
        return decodeConsumption(raw, cycleSeasonIndex)
    }

    suspend fun saveSeasonConsumption(dynastyId: String, consumption: RecruitingSeasonConsumption) {
        context.recruitingReservationStore.edit { prefs ->
            prefs[consumptionKey(dynastyId)] = encodeConsumption(consumption)
        }
    }

    suspend fun closeWeekReservations(dynastyId: String, cycleSeasonIndex: Int) {
        val active = loadActive(dynastyId)
        val consumption = loadSeasonConsumption(dynastyId, cycleSeasonIndex)
        val (stillActive, nextConsumption) = RecruitingWeekReservationClose.afterWeekAdvance(active, consumption)
        saveActive(dynastyId, stillActive)
        saveSeasonConsumption(dynastyId, nextConsumption)
    }

    private fun prefKey(dynastyId: String) = stringPreferencesKey("reservations_$dynastyId")

    private fun consumptionKey(dynastyId: String) = stringPreferencesKey("consumption_$dynastyId")

    private fun encode(reservations: List<RecruitingWeekReservation>): String =
        reservations.joinToString(";") { r ->
            "${r.reservationId}|${r.prospectId}|${r.action.name}|${r.pointsReserved}"
        }

    private fun encodeConsumption(consumption: RecruitingSeasonConsumption): String =
        "${consumption.cycleSeasonIndex}|${consumption.scoutedProspectIds.joinToString(",")}|" +
            consumption.officialVisitProspectIds.joinToString(",")

    private fun decodeConsumption(raw: String, expectedSeason: Int): RecruitingSeasonConsumption {
        val parts = raw.split("|")
        require(parts.size == 3)
        val season = parts[0].toInt()
        val scout = parts[1].split(",").filter { it.isNotBlank() }.toSet()
        val visit = parts[2].split(",").filter { it.isNotBlank() }.toSet()
        return if (season == expectedSeason) {
            RecruitingSeasonConsumption(season, scout, visit)
        } else {
            RecruitingSeasonConsumption(expectedSeason)
        }
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

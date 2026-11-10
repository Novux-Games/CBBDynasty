package dev.novux.dynasty.cbb.data.offseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityGrades
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityUpgradePolicy
import dev.novux.dynasty.cbb.core.offseason.weeknine.FundraisingEventType
import dev.novux.dynasty.cbb.core.offseason.weeknine.FundraisingOutcome
import dev.novux.dynasty.cbb.core.offseason.weeknine.FundraisingSimulator
import dev.novux.dynasty.cbb.core.offseason.weeknine.ProgramEconomyState
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import kotlinx.coroutines.flow.first

private val Context.offseasonWeekNineStore: DataStore<Preferences> by preferencesDataStore(
    name = "offseason_week_nine",
)

class OffseasonWeekNineRepository(
    private val context: Context,
) {
    suspend fun loadEconomy(dynastyId: String, seasonIndex: Int): ProgramEconomyState {
        val prefs = context.offseasonWeekNineStore.data.first()
        val cash = prefs[cashKey(dynastyId, seasonIndex)] ?: ProgramEconomyState().cashUsd
        val nilBoost = prefs[nilBoostKey(dynastyId, seasonIndex)] ?: 0
        val grades = FacilityGrades.decode(prefs[gradesKey(dynastyId, seasonIndex)])
        val events = decodeEvents(prefs[eventsKey(dynastyId, seasonIndex)])
        val naming = prefs[namingKey(dynastyId, seasonIndex)] == true
        return ProgramEconomyState(cash, nilBoost, grades, events, naming)
    }

    suspend fun nilPoolBoostUsd(dynastyId: String, seasonIndex: Int): Int =
        loadEconomy(dynastyId, seasonIndex).nilBoostUsd

    suspend fun runFundraising(
        dynastyId: String,
        seasonIndex: Int,
        event: FundraisingEventType,
        programPrestige: Int = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
    ): FundraisingOutcome? {
        val state = loadEconomy(dynastyId, seasonIndex)
        if (event in state.completedEvents) return null
        val outcome = FundraisingSimulator.simulate(event, programPrestige)
        val next = state.copy(
            cashUsd = state.cashUsd + outcome.totalCashGain,
            nilBoostUsd = state.nilBoostUsd + outcome.nilBoostUsd,
            completedEvents = state.completedEvents + event,
        )
        persist(dynastyId, seasonIndex, next)
        return outcome
    }

    suspend fun purchaseFacilityUpgrade(
        dynastyId: String,
        seasonIndex: Int,
        category: FacilityCategory,
    ): Boolean {
        val state = loadEconomy(dynastyId, seasonIndex)
        if (!FacilityUpgradePolicy.canAfford(state.cashUsd, state.facilityGrades, category)) {
            return false
        }
        val cost = FacilityUpgradePolicy.upgradeCostUsd(state.facilityGrades.grade(category))
        val next = state.copy(
            cashUsd = state.cashUsd - cost,
            facilityGrades = state.facilityGrades.withUpgrade(category),
        )
        persist(dynastyId, seasonIndex, next)
        return true
    }

    suspend fun sellNamingRights(
        dynastyId: String,
        seasonIndex: Int,
        lumpSumUsd: Int = 400_000,
    ): Boolean {
        val state = loadEconomy(dynastyId, seasonIndex)
        if (state.namingRightsSold) return false
        val next = state.copy(
            cashUsd = state.cashUsd + lumpSumUsd,
            namingRightsSold = true,
        )
        persist(dynastyId, seasonIndex, next)
        return true
    }

    private suspend fun persist(dynastyId: String, seasonIndex: Int, state: ProgramEconomyState) {
        context.offseasonWeekNineStore.edit { prefs ->
            prefs[cashKey(dynastyId, seasonIndex)] = state.cashUsd
            prefs[nilBoostKey(dynastyId, seasonIndex)] = state.nilBoostUsd
            prefs[gradesKey(dynastyId, seasonIndex)] = state.facilityGrades.encode()
            prefs[eventsKey(dynastyId, seasonIndex)] = encodeEvents(state.completedEvents)
            prefs[namingKey(dynastyId, seasonIndex)] = state.namingRightsSold
        }
    }

    private fun cashKey(dynastyId: String, seasonIndex: Int) =
        intPreferencesKey("cash_${dynastyId}_$seasonIndex")

    private fun nilBoostKey(dynastyId: String, seasonIndex: Int) =
        intPreferencesKey("nil_boost_${dynastyId}_$seasonIndex")

    private fun gradesKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("grades_${dynastyId}_$seasonIndex")

    private fun eventsKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("events_${dynastyId}_$seasonIndex")

    private fun namingKey(dynastyId: String, seasonIndex: Int) =
        booleanPreferencesKey("naming_${dynastyId}_$seasonIndex")

    private fun encodeEvents(events: Set<FundraisingEventType>): String =
        events.joinToString(",") { it.key }

    private fun decodeEvents(raw: String?): Set<FundraisingEventType> {
        if (raw.isNullOrBlank()) return emptySet()
        return raw.split(",").mapNotNull { key ->
            FundraisingEventType.entries.firstOrNull { it.key == key }
        }.toSet()
    }
}

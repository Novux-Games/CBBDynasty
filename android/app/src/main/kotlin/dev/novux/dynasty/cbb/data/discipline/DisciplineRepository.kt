package dev.novux.dynasty.cbb.data.discipline

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.balance.disciplineTuning
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.discipline.DisciplineOccurrenceEngine
import dev.novux.dynasty.cbb.core.discipline.DisciplineResolutionChoice
import dev.novux.dynasty.cbb.core.discipline.DisciplineResolver
import dev.novux.dynasty.cbb.core.discipline.PendingDisciplineIncident
import dev.novux.dynasty.cbb.core.discipline.TeamDisciplineSeasonState
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import dev.novux.dynasty.cbb.database.roster.toDomain
import dev.novux.dynasty.cbb.database.roster.toEntity
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.disciplineStore: DataStore<Preferences> by preferencesDataStore(name = "discipline_state")

class DisciplineRepository(
    private val context: Context,
    private val database: DynastyDatabase,
    private val balanceRulesetRepository: BalanceRulesetRepository,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadState(dynastyId: String, seasonIndex: Int): TeamDisciplineSeasonState {
        val raw = context.disciplineStore.data.first()[stateKey(dynastyId)] ?: return TeamDisciplineSeasonState(seasonIndex)
        val stored = json.decodeFromString<TeamDisciplineSeasonState>(raw)
        if (stored.seasonIndex != seasonIndex) {
            return TeamDisciplineSeasonState(seasonIndex)
        }
        return stored
    }

    suspend fun hasPending(dynastyId: String, seasonIndex: Int): Boolean =
        loadState(dynastyId, seasonIndex).pending != null

    private suspend fun saveState(dynastyId: String, state: TeamDisciplineSeasonState) {
        context.disciplineStore.edit {
            it[stateKey(dynastyId)] = json.encodeToString(state)
        }
    }

    suspend fun afterWeekAdvance(
        dynastyId: String,
        seasonIndex: Int,
        phase: CalendarPhase,
        rsWeekIndex: Int,
        players: List<RosterPlayer>,
        teamPlayedGame: Boolean,
        rollSeed: Long,
    ): List<RosterPlayer> {
        val tuning = balanceRulesetRepository.activeRuleset().disciplineTuning()
        var roster = DisciplineResolver.tickAcademicSupport(players, tuning.academic.weeklyGpaGain)
        roster = DisciplineResolver.decrementGameSuspensions(roster, teamPlayedGame)
        roster.forEach { database.dynastyRosterDao().upsert(it.toEntity()) }

        var state = loadState(dynastyId, seasonIndex)
        if (state.pending == null) {
            val incident = DisciplineOccurrenceEngine.rollAfterWeekAdvance(
                phase = phase,
                rsWeekIndex = rsWeekIndex,
                players = roster,
                seasonState = state,
                tuning = tuning,
                rollSeed = rollSeed,
            )
            if (incident != null) {
                state = state.copy(pending = incident)
                saveState(dynastyId, state)
            }
        }
        return roster
    }

    suspend fun resolve(
        dynastyId: String,
        seasonIndex: Int,
        choice: DisciplineResolutionChoice,
        suspendGames: Int? = null,
    ): List<RosterPlayer> {
        val state = loadState(dynastyId, seasonIndex)
        val incident = state.pending ?: error("no pending discipline incident")
        val players = database.dynastyRosterDao().listForDynasty(dynastyId).map { it.toDomain() }
        val tuning = balanceRulesetRepository.activeRuleset().disciplineTuning()
        val result = DisciplineResolver.apply(players, incident, choice, suspendGames, tuning)
        result.players.forEach { database.dynastyRosterDao().upsert(it.toEntity()) }
        result.cutPlayerIds.forEach { database.dynastyRosterDao().deletePlayer(dynastyId, it) }
        val next = state.copy(
            pending = null,
            eventsResolved = state.eventsResolved + 1,
        )
        saveState(dynastyId, next)
        return database.dynastyRosterDao().listForDynasty(dynastyId).map { it.toDomain() }
    }

    private fun stateKey(dynastyId: String) = stringPreferencesKey("discipline_$dynastyId")
}

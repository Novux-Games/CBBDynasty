package dev.novux.dynasty.cbb.data.league

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameMark
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.league.LeagueWeekBatchSimEngine
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.leagueStandingsStore: DataStore<Preferences> by preferencesDataStore(
    name = "league_standings",
)

class LeagueWeekBatchRepository(
    private val context: Context,
    private val leagueScheduleRepository: LeagueScheduleRepository,
    private val preseasonScheduleRepository: PreseasonScheduleRepository,
    private val database: DynastyDatabase,
) {
    suspend fun loadStandings(dynastyId: String, seasonIndex: Int): LeagueStandingsState? {
        val raw = context.leagueStandingsStore.data.first()[key(dynastyId, seasonIndex)] ?: return null
        return Json.decodeFromString(raw)
    }

    suspend fun afterWeekAdvance(
        dynastyId: String,
        seasonIndex: Int,
        committedWeek: CalendarPosition,
        userGamesCommitted: Map<String, ProvisionalGameMark>,
    ) {
        if (!LeagueWeekBatchSimEngine.phaseSupportsBatch(committedWeek.phase)) return
        val schedule = leagueScheduleRepository.load(dynastyId, seasonIndex) ?: return
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return
        val userTeamId = coach.userTeamId
        var standings = loadStandings(dynastyId, seasonIndex)
            ?: LeagueWeekBatchSimEngine.bootstrap(
                seasonIndex,
                schedule.schedules.map { it.teamId },
            )
        val userResult = buildUserResult(
            userTeamId,
            committedWeek.weekIndex,
            preseasonScheduleRepository.loadPlan(dynastyId, seasonIndex),
            userGamesCommitted,
        )
        val seed = dynastyId.hashCode().toLong() + seasonIndex + committedWeek.weekIndex
        val batch = LeagueWeekBatchSimEngine.runRegularSeasonWeek(
            schedule = schedule,
            standings = standings,
            rsWeekIndex = committedWeek.weekIndex,
            userTeamId = userTeamId,
            userResult = userResult,
            seed = seed,
        )
        persist(dynastyId, batch.standings)
    }

    private fun buildUserResult(
        userTeamId: String,
        rsWeekIndex: Int,
        plan: PreseasonSchedulePlan?,
        committed: Map<String, ProvisionalGameMark>,
    ): LeagueGameResult? {
        val mark = committed.values.firstOrNull() ?: return null
        val game = plan?.regularSeason?.firstOrNull { it.rsWeekIndex == rsWeekIndex } ?: return null
        val homeTeam = if (game.homeGame) userTeamId else game.opponentTeamId
        val awayTeam = if (game.homeGame) game.opponentTeamId else userTeamId
        return LeagueGameResult(
            homeTeamId = homeTeam,
            awayTeamId = awayTeam,
            homeScore = mark.homeScore,
            awayScore = mark.awayScore,
            rsWeekIndex = rsWeekIndex,
        )
    }

    private suspend fun persist(dynastyId: String, state: LeagueStandingsState) {
        context.leagueStandingsStore.edit {
            it[key(dynastyId, state.seasonIndex)] = Json.encodeToString(state)
        }
    }

    private fun key(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("standings_${dynastyId}_$seasonIndex")
}

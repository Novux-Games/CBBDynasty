package dev.novux.dynasty.cbb.data.league

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameMark
import dev.novux.dynasty.cbb.core.balance.selectionTuning
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.league.LeagueWeekBatchSimEngine
import dev.novux.dynasty.cbb.core.resume.HumanPollRankingEngine
import dev.novux.dynasty.cbb.core.resume.NcbaNetRankingEngine
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketSeason
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.data.preseason.EarlySeasonBracketRepository
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
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
    private val earlySeasonBracketRepository: EarlySeasonBracketRepository,
    private val newsFeedRepository: NewsFeedRepository,
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
    private val balanceRulesetRepository: BalanceRulesetRepository,
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
        val plan = preseasonScheduleRepository.loadPlan(dynastyId, seasonIndex)
        val bracketSeason = earlySeasonBracketRepository.load(dynastyId, seasonIndex)
        val userResult = buildUserResult(
            userTeamId,
            committedWeek.weekIndex,
            plan,
            userGamesCommitted,
            bracketSeason,
        )
        val seed = dynastyId.hashCode().toLong() + seasonIndex + committedWeek.weekIndex
        if (bracketSeason != null && bracketSeason.brackets.isNotEmpty()) {
            val eventOutcome = EarlySeasonBracketEngine.simWeekForLeague(
                season = bracketSeason,
                standings = standings,
                rsWeekIndex = committedWeek.weekIndex,
                userTeamId = userTeamId,
                userResults = userResult?.let { listOf(it) }.orEmpty(),
                seed = seed,
            )
            standings = eventOutcome.standings
            earlySeasonBracketRepository.persist(dynastyId, eventOutcome.season)
        }
        val batch = LeagueWeekBatchSimEngine.runRegularSeasonWeek(
            schedule = schedule,
            standings = standings,
            rsWeekIndex = committedWeek.weekIndex,
            userTeamId = userTeamId,
            userResult = userResult,
            seed = seed,
        )
        val withNet = attachWeeklyNet(batch.standings, committedWeek.weekIndex)
        persist(dynastyId, withNet)
        newsFeedRepository.appendLeagueWeek(dynastyId, withNet)
    }

    private suspend fun attachWeeklyNet(
        standings: LeagueStandingsState,
        weekIndex: Int,
    ): LeagueStandingsState {
        if (standings.seasonResults.isEmpty()) return standings
        val prestige = packRepository.activeSession().index.allTeamDisplays()
            .associate { it.teamId to it.programPrestige }
        val tuning = balanceRulesetRepository.activeRuleset().selectionTuning()
        val table = NcbaNetRankingEngine.compute(
            teamIds = prestige.keys,
            programPrestige = prestige,
            records = standings.records,
            results = standings.seasonResults,
            tuning = tuning,
            rankingsWeek = weekIndex,
        )
        val polls = HumanPollRankingEngine.compute(
            netTable = table,
            programPrestige = prestige,
            results = standings.seasonResults,
            priorMediaRanks = standings.mediaPollRankByTeam,
            priorCoachesRanks = standings.coachesPollRankByTeam,
            lastWeekResults = standings.lastWeekResults,
            tuning = tuning,
        )
        return standings.copy(
            netRankByTeam = table.byTeamId.mapValues { it.value.netRank },
            mediaPollRankByTeam = polls.mediaRankByTeam,
            coachesPollRankByTeam = polls.coachesRankByTeam,
            netRankingsWeek = weekIndex,
        )
    }

    private fun buildUserResult(
        userTeamId: String,
        rsWeekIndex: Int,
        plan: PreseasonSchedulePlan?,
        committed: Map<String, ProvisionalGameMark>,
        bracketSeason: EarlySeasonBracketSeason?,
    ): LeagueGameResult? {
        val mark = committed.values.firstOrNull() ?: return null
        val game = plan?.regularSeason?.firstOrNull { it.rsWeekIndex == rsWeekIndex } ?: return null
        if (game.kind == ScheduleGameKind.EarlyEvent) {
            val snapshot = bracketSeason?.brackets?.values?.firstOrNull { it.isParticipant(userTeamId) }
                ?: return null
            val matchup = EarlySeasonBracketEngine.pairingsForWeek(snapshot, rsWeekIndex)
                .firstOrNull { userTeamId == it.homeTeamId || userTeamId == it.awayTeamId }
                ?: return null
            val (homeScore, awayScore) = if (userTeamId == matchup.homeTeamId) {
                mark.homeScore to mark.awayScore
            } else {
                mark.awayScore to mark.homeScore
            }
            return LeagueGameResult(
                homeTeamId = matchup.homeTeamId,
                awayTeamId = matchup.awayTeamId,
                homeScore = homeScore,
                awayScore = awayScore,
                rsWeekIndex = rsWeekIndex,
            )
        }
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

    suspend fun persistStandings(dynastyId: String, state: LeagueStandingsState) {
        persist(dynastyId, state)
    }

    private suspend fun persist(dynastyId: String, state: LeagueStandingsState) {
        context.leagueStandingsStore.edit {
            it[key(dynastyId, state.seasonIndex)] = Json.encodeToString(state)
        }
    }

    private fun key(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("standings_${dynastyId}_$seasonIndex")
}

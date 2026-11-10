package dev.novux.dynasty.cbb.data.preseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.preseason.tournament.RegularSeasonSlotBudget
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.PreseasonScheduleSolver
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub
import dev.novux.dynasty.cbb.core.schedule.PreseasonScheduleConstants
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first

private val Context.preseasonScheduleStore: DataStore<Preferences> by preferencesDataStore(
    name = "preseason_schedule",
)

class PreseasonScheduleRepository(
    private val context: Context,
    private val packRepository: PackRepository,
    private val database: DynastyDatabase,
    private val tournamentInvitesRepository: TournamentInvitesRepository,
) {
    suspend fun loadExhibitionCount(dynastyId: String, seasonIndex: Int): Int =
        context.preseasonScheduleStore.data.first()[exhibitionKey(dynastyId, seasonIndex)] ?: 0

    suspend fun loadPlan(dynastyId: String, seasonIndex: Int): PreseasonSchedulePlan? {
        val raw = context.preseasonScheduleStore.data.first()[planKey(dynastyId, seasonIndex)] ?: return null
        return decodePlan(raw)
    }

    suspend fun saveExhibitions(dynastyId: String, seasonIndex: Int, count: Int) {
        val capped = count.coerceIn(0, PreseasonScheduleConstants.MAX_EXHIBITION_GAMES)
        context.preseasonScheduleStore.edit { prefs ->
            prefs[exhibitionKey(dynastyId, seasonIndex)] = capped
        }
    }

    suspend fun solveAndSaveRegularSeason(dynastyId: String, seasonIndex: Int): PreseasonSchedulePlan? {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        val index = packRepository.activeSession().index
        val userTeam = index.team(coach.userTeamId) ?: return null
        val conference = index.conference(userTeam.conferenceId) ?: return null
        val confOpponents = index.allTeamDisplays()
            .filter { it.conferenceId == userTeam.conferenceId && it.teamId != userTeam.teamId }
        val nonConf = index.allTeamDisplays()
            .filter { it.conferenceId != userTeam.conferenceId }
        val inviteState = tournamentInvitesRepository.loadState(dynastyId, seasonIndex)
        val budget = inviteState.slotBudget
        val exhibitions = loadExhibitionCount(dynastyId, seasonIndex)
        val plan = PreseasonScheduleSolver.solve(
            userTeam = userTeam,
            conference = conference,
            conferenceOpponents = confOpponents,
            nonConferencePool = nonConf,
            budget = budget,
            tournamentResolution = inviteState.resolution,
            exhibitionCount = exhibitions,
            seed = dynastyId.hashCode().toLong() xor seasonIndex.toLong(),
        )
        if (!plan.isValidFor(budget)) return null
        context.preseasonScheduleStore.edit { prefs ->
            prefs[planKey(dynastyId, seasonIndex)] = encodePlan(plan)
        }
        return plan
    }

    private fun exhibitionKey(dynastyId: String, seasonIndex: Int) =
        intPreferencesKey("exhibitions_${dynastyId}_$seasonIndex")

    private fun planKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("plan_${dynastyId}_$seasonIndex")

    private fun encodePlan(plan: PreseasonSchedulePlan): String {
        val header = "${plan.conferenceGameCount}|${plan.nonConferenceGameCount}|${plan.earlyEventGameCount}"
        val lines = (plan.exhibitions + plan.regularSeason).joinToString("\n") { encodeGame(it) }
        return "$header\n$lines"
    }

    private fun encodeGame(game: ScheduledGameStub): String =
        listOf(
            game.kind.name,
            game.opponentTeamId,
            game.opponentDisplayName.escape(),
            if (game.homeGame) "H" else "A",
            game.rsWeekIndex?.toString().orEmpty(),
        ).joinToString("|")

    private fun decodePlan(raw: String): PreseasonSchedulePlan? {
        val split = raw.split("\n", limit = 2)
        if (split.size != 2) return null
        val header = split[0].split("|")
        if (header.size < 3) return null
        val games = split[1].lineSequence().mapNotNull { decodeGame(it) }.toList()
        val exhibitions = games.filter { it.kind == ScheduleGameKind.Exhibition }
        val regular = games.filter { it.kind != ScheduleGameKind.Exhibition }
        return PreseasonSchedulePlan(
            exhibitions = exhibitions,
            regularSeason = regular,
            conferenceGameCount = header[0].toIntOrNull() ?: 0,
            nonConferenceGameCount = header[1].toIntOrNull() ?: 0,
            earlyEventGameCount = header[2].toIntOrNull() ?: 0,
        )
    }

    private fun decodeGame(line: String): ScheduledGameStub? {
        val p = line.split("|")
        if (p.size < 5) return null
        val kind = runCatching { ScheduleGameKind.valueOf(p[0]) }.getOrNull() ?: return null
        return ScheduledGameStub(
            kind = kind,
            opponentTeamId = p[1],
            opponentDisplayName = p[2].unescape(),
            homeGame = p[3] == "H",
            rsWeekIndex = p[4].toIntOrNull(),
        )
    }

    private fun String.escape(): String = replace("|", "%7C")
    private fun String.unescape(): String = replace("%7C", "|")
}

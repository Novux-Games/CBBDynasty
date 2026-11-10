package dev.novux.dynasty.cbb.data.awards

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.awards.YearlyAwardsSeasonArchive
import dev.novux.dynasty.cbb.core.awards.YearlyAwardsWrapEngine
import dev.novux.dynasty.cbb.core.awards.YearlyAwardsWrapInput
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCardFactory
import dev.novux.dynasty.cbb.core.history.BalanceHistoryParser
import dev.novux.dynasty.cbb.core.history.WinContributionEngine
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.history.HistoryRepository
import dev.novux.dynasty.cbb.core.news.NewsHeadlinePublisher
import dev.novux.dynasty.cbb.data.league.LeagueWeekBatchRepository
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.yearlyAwardsStore: DataStore<Preferences> by preferencesDataStore(name = "yearly_awards")

class YearlyAwardsRepository(
    private val context: Context,
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
    private val leagueWeekBatchRepository: LeagueWeekBatchRepository,
    private val dynastyRosterRepository: DynastyRosterRepository,
    private val newsFeedRepository: NewsFeedRepository,
    private val balanceRulesetRepository: BalanceRulesetRepository,
    private val historyRepository: HistoryRepository,
) {
    suspend fun loadArchive(dynastyId: String, seasonIndex: Int): YearlyAwardsSeasonArchive? {
        val raw = context.yearlyAwardsStore.data.first()[key(dynastyId, seasonIndex)] ?: return null
        return Json.decodeFromString(raw)
    }

    suspend fun wrapSeasonIfNeeded(dynastyId: String, seasonIndex: Int) {
        if (loadArchive(dynastyId, seasonIndex) != null) return
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return
        val standings = leagueWeekBatchRepository.loadStandings(dynastyId, seasonIndex) ?: return
        val index = packRepository.activeSession().index
        val team = index.team(coach.userTeamId) ?: return
        val roster = dynastyRosterRepository.listPlayers(dynastyId)
        val coachName = HeadCoachCardFactory.displayName(coach.firstName, coach.lastName, coach.nameSuffixWire)
        val tuning = BalanceHistoryParser.parse(balanceRulesetRepository.activeRuleset().history)
        val winShares = WinContributionEngine.allocateTeamWins(
            roster,
            standings.recordFor(coach.userTeamId).wins,
            tuning.winContribution,
        )
        val archive = YearlyAwardsWrapEngine.compute(
            YearlyAwardsWrapInput(
                seasonIndex = seasonIndex,
                userTeamId = coach.userTeamId,
                userConferenceId = team.conferenceId,
                userSchoolName = team.displayName,
                userCoachDisplayName = coachName,
                userRoster = roster,
                standings = standings,
                packIndex = index,
                winSharesByPlayer = winShares,
            ),
        )
        persist(dynastyId, archive)
        historyRepository.finalizeSeason(dynastyId, seasonIndex, archive)
        val headlines = NewsHeadlinePublisher.awardsWrap(
            seasonIndex,
            archive.ncbaOverview + listOfNotNull(archive.userTeamMvp),
        )
        newsFeedRepository.appendAwardsHeadlines(dynastyId, seasonIndex, headlines)
    }

    private suspend fun persist(dynastyId: String, archive: YearlyAwardsSeasonArchive) {
        context.yearlyAwardsStore.edit {
            it[key(dynastyId, archive.seasonIndex)] = Json.encodeToString(archive)
        }
    }

    private fun key(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("awards_${dynastyId}_$seasonIndex")
}

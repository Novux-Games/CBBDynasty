package dev.novux.dynasty.cbb.data.recruiting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActions
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActionsParser
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingParser
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommendedParser
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.recruiting.RecruitingClassSeed
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCyclePolicy
import dev.novux.dynasty.cbb.core.recruiting.SchoolRecruitingContext
import dev.novux.dynasty.cbb.core.recruiting.ai.AiLeagueProgramGenerator
import dev.novux.dynasty.cbb.core.recruiting.ai.AiRecruitingWeekResult
import dev.novux.dynasty.cbb.core.recruiting.ai.AiRecruitingWeekRunner
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import kotlinx.coroutines.flow.first

private val Context.aiRecruitingStore: DataStore<Preferences> by preferencesDataStore(
    name = "ai_recruiting",
)

class AiRecruitingRepository(
    private val context: Context,
    private val nilAnnualPoolRepository: dev.novux.dynasty.cbb.data.nil.NilAnnualPoolRepository,
    private val dynastyAutomationRepository: dev.novux.dynasty.cbb.data.settings.DynastyAutomationRepository,
) {
    suspend fun runLeagueWeekIfActive(
        dynasty: LoadedDynasty,
        ledger: RecruitingCycleLedger,
        userManualPointsSpent: Int = 0,
    ): AiRecruitingWeekResult? {
        val position = dynasty.weekCheckpoint.activePosition
        if (position.phase == CalendarPhase.SeasonReview) return null
        val economy = economyFrom(dynasty)
        val recommended = BalanceRecruitingRecommendedParser.parse(dynasty.balanceRuleset.recruiting)
        val pool = RecruitingClassSeed.demoPool(dynasty.metadata.seasonIndex)
        val userProgramId = userProgramId(dynasty.dynastyId)
        val userSchool = nilAnnualPoolRepository.userSchoolRecruitingContext(dynasty)
            ?: SchoolRecruitingContext(
                programPrestige = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
                schoolHomeState = "OH",
                pipelineStates = setOf("KY", "IN", "MI", "PA", "WV"),
                nilPoolAnnualUsd = dev.novux.dynasty.cbb.data.nil.NilAnnualPoolRepository.DEFAULT_FALLBACK_USD.toInt(),
            )
        val offseasonWeek = position.weekIndex.takeIf { position.phase == CalendarPhase.Offseason }
        val hubOpen = RecruitingCyclePolicy.hubAccessible(position.phase, offseasonWeek)
        val automation = dynastyAutomationRepository.load(dynasty.dynastyId)
        val actionCosts = actionsFrom(dynasty)
        val result = AiRecruitingWeekRunner.runWeek(
            prospectPool = pool,
            economy = economy,
            recommended = recommended,
            userProgramId = userProgramId,
            userSchool = userSchool,
            userPointsAvailable = ledger.pointsAvailable,
            userPointsSpentByPlayer = userManualPointsSpent,
            autoMaintainUserSchool = hubOpen && automation.recruitingAutoMaintain,
            leaguePrograms = AiLeagueProgramGenerator.demoLeague(
                seasonIndex = dynasty.metadata.seasonIndex,
                excludeProgramId = userProgramId,
            ),
            actionCosts = actionCosts,
        )
        persistSummary(dynasty.dynastyId, result)
        return result
    }

    suspend fun loadLastSummaryLine(dynastyId: String): String? {
        val prefs = context.aiRecruitingStore.data.first()
        return prefs[prefKey(dynastyId)]
    }

    private suspend fun persistSummary(dynastyId: String, result: AiRecruitingWeekResult) {
        val line = "league=${result.leagueProgramsProcessed};userSpent=${result.userPointsSpent};" +
            "auto=${result.userAutoMaintained}"
        context.aiRecruitingStore.edit { prefs ->
            prefs[prefKey(dynastyId)] = line
        }
    }

    private fun economyFrom(dynasty: LoadedDynasty): BalanceRecruitingEconomy =
        BalanceRecruitingParser.parse(dynasty.balanceRuleset.recruiting)

    private fun actionsFrom(dynasty: LoadedDynasty): BalanceRecruitingActions =
        BalanceRecruitingActionsParser.parse(dynasty.balanceRuleset.recruiting)

    private fun userProgramId(dynastyId: String) = "user-$dynastyId"

    private fun prefKey(dynastyId: String) = stringPreferencesKey("ai_week_$dynastyId")
}

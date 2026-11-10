package dev.novux.dynasty.cbb.core.recruiting.ai

import dev.novux.dynasty.cbb.core.balance.BalanceLoadResult
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActionsParser
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingParser
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommendedParser
import dev.novux.dynasty.cbb.core.balance.BalanceRulesetCodec
import dev.novux.dynasty.cbb.core.recruiting.RecruitingClassSeed
import dev.novux.dynasty.cbb.core.recruiting.SchoolRecruitingContext
import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiRecruitingWeekRunnerTest {
    @Test
    fun leaguePass_usesRecommendedOrdering_capByPrestige() {
        val economy = loadEconomy()
        val recommended = loadRecommended()
        val actions = loadActions()
        val pool = RecruitingClassSeed.demoPool(1)
        val low = AiProgramSnapshot(
            programId = "low",
            programPrestige = 20,
            schoolHomeState = "TX",
            nilPoolAnnualUsd = 200_000,
        )
        val board = AiRecruitingWeekRunner.runWeek(
            prospectPool = pool,
            economy = economy,
            recommended = recommended,
            userProgramId = "user",
            userSchool = SchoolRecruitingContext(40, "OH", nilPoolAnnualUsd = 900_000),
            userPointsAvailable = 0,
            userPointsSpentByPlayer = 0,
            autoMaintainUserSchool = false,
            leaguePrograms = listOf(low),
            actionCosts = actions,
        ).leagueBoards.single()
        val cap = AiRecruitingCapacity.coreTargetCap(economy, 20)
        assertTrue(board.targetProspectIds.size <= cap)
        assertTrue(board.pointsSpentThisWeek > 0)
    }

    @Test
    fun userAutoMaintain_whenNoManualSpend() {
        val economy = loadEconomy()
        val recommended = loadRecommended()
        val actions = loadActions()
        val pool = RecruitingClassSeed.demoPool(1)
        val result = AiRecruitingWeekRunner.runWeek(
            prospectPool = pool,
            economy = economy,
            recommended = recommended,
            userProgramId = "user-dynasty",
            userSchool = SchoolRecruitingContext(40, "OH", nilPoolAnnualUsd = 900_000),
            userPointsAvailable = 1500,
            userPointsSpentByPlayer = 0,
            autoMaintainUserSchool = true,
            leaguePrograms = emptyList(),
            actionCosts = actions,
        )
        assertTrue(result.userAutoMaintained)
        assertTrue(result.userPointsSpent > 0)
        assertEquals("user-dynasty", result.userBoard?.programId)
    }

    private fun loadEconomy() = {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        val raw = dir!!.resolve("assets/balance/balance-2026.json").readText()
        val ruleset = (BalanceRulesetCodec.parseAndValidate(raw) as BalanceLoadResult.Success).ruleset
        BalanceRecruitingParser.parse(ruleset.recruiting)
    }.invoke()

    private fun loadActions() = {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        val raw = dir!!.resolve("assets/balance/balance-2026.json").readText()
        val ruleset = (BalanceRulesetCodec.parseAndValidate(raw) as BalanceLoadResult.Success).ruleset
        BalanceRecruitingActionsParser.parse(ruleset.recruiting)
    }.invoke()

    private fun loadRecommended() = {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        val raw = dir!!.resolve("assets/balance/balance-2026.json").readText()
        val ruleset = (BalanceRulesetCodec.parseAndValidate(raw) as BalanceLoadResult.Success).ruleset
        BalanceRecruitingRecommendedParser.parse(ruleset.recruiting)
    }.invoke()
}

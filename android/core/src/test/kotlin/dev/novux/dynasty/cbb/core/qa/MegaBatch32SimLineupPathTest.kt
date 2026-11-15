package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitInterestParser
import dev.novux.dynasty.cbb.core.coach.CoachCarouselOfferAcceptPolicy
import dev.novux.dynasty.cbb.core.coach.CoachSkillSundayGrantPolicy
import dev.novux.dynasty.cbb.core.coach.UnemployedSpectateGamePolicy
import dev.novux.dynasty.cbb.core.gdd.PreseasonNonConferenceCapPolicy
import dev.novux.dynasty.cbb.core.march.PostseasonBracketMonitorPolicy
import dev.novux.dynasty.cbb.core.pack.ReclassTransitionUxPolicy
import dev.novux.dynasty.cbb.core.preview.OpponentRotationPreviewBuilder
import dev.novux.dynasty.cbb.core.recruiting.NewGameWizardStepsPolicy
import dev.novux.dynasty.cbb.core.recruiting.PortalConsideringPoolUxPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitInterestConfig
import dev.novux.dynasty.cbb.core.recruiting.RecruitNextCyclePoolPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitPreseasonSeedPanelPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitPrioritySeedPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitTop60HubPolicy
import dev.novux.dynasty.cbb.core.recruiting.SeasonOneClassBriefPolicy
import dev.novux.dynasty.cbb.core.sim.GameSimClock
import dev.novux.dynasty.cbb.core.sim.GameSimInput
import dev.novux.dynasty.cbb.core.sim.GameSimTeamInput
import dev.novux.dynasty.cbb.core.sim.tuning.IntMinMax
import dev.novux.dynasty.cbb.core.sim.tuning.SimShotClock
import dev.novux.dynasty.cbb.core.sim.tuning.SimTimeUsedSeconds
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.sim.tuning.SimVenueModifiers
import dev.novux.dynasty.cbb.core.strategy.GameplanInput
import java.nio.file.Paths
import kotlin.io.path.readText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch32SimLineupPathTest {
    private val repoRoot = generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
        .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }

  private val tuning = SimTuning(
    simTuningId = "t",
    schemaVersion = 1,
    possessionsPerGameTarget = IntMinMax(60, 70),
    shotClock = SimShotClock(30, 20),
    timeUsedSeconds = SimTimeUsedSeconds(
      quickTurnover = listOf(3, 6),
      quickShot = listOf(8, 14),
      foulStoppage = listOf(12, 18),
    ),
    eventWeights = JsonObject(emptyMap()),
    homeNeutralModifiers = SimVenueModifiers(1.0, 1.0, 1.0),
  )

    @Test
    fun gddAndRecrMegaIdsRegistered() {
        assertEquals("MEGA-GDD-009", OpponentRotationPreviewBuilder.MEGA_ID)
        assertEquals(32, PreseasonNonConferenceCapPolicy.rsCap)
        assertEquals("MEGA-GDD-120", CoachCarouselOfferAcceptPolicy.MEGA_ID)
        assertTrue(UnemployedSpectateGamePolicy.spectateAllowed(employed = false))
        assertEquals("MEGA-GDD-125", CoachSkillSundayGrantPolicy.MEGA_ID)
        assertEquals(3, PostseasonBracketMonitorPolicy.monitoredEvents.size)
        assertEquals("Reclass Y3 of 4", ReclassTransitionUxPolicy.seasonLabel(2, 2))
        assertEquals("MEGA-RECR-007", RecruitTop60HubPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-026", RecruitInterestConfig.MEGA_ID)
        assertEquals("MEGA-RECR-071", NewGameWizardStepsPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-062", PortalConsideringPoolUxPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-116", SeasonOneClassBriefPolicy.MEGA_ID)
        assertEquals(2, RecruitPrioritySeedPolicy.MAX_PRIORITIES)
        assertEquals("MEGA-RECR-025", RecruitNextCyclePoolPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-052", RecruitPreseasonSeedPanelPolicy.MEGA_ID)
    }

    @Test
    fun balanceBundleDeclaresRecruitInterest() {
        val root = repoRoot.resolve("android/app/src/main/assets/balance/balance-2026.json").readText()
        val recruiting = Json.parseToJsonElement(root).jsonObject.getValue("recruiting")
        val parsed = BalanceRecruitInterestParser.parse(recruiting)
        assertNotNull(parsed)
        RecruitInterestConfig.bindFromBalance(parsed)
        assertEquals(2.0, RecruitInterestConfig.PRIORITY_MULTIPLIER, 0.001)
    }

    @Test
    fun simHalftimeGameplanSelectsAfterIntermission() {
        val pre = GameplanInput(denyPrimaryScorer = false)
        val half = GameplanInput(denyPrimaryScorer = true)
        val input = GameSimInput(
            home = GameSimTeamInput("h", 80, 75),
            away = GameSimTeamInput("a", 76, 74),
            tuning = tuning,
            seed = 1L,
            homeGameplan = pre,
            homeHalftimeGameplan = half,
        )
        assertEquals(pre, input.activeHomeGameplan(GameSimClock.REGULATION_SECONDS))
        assertEquals(half, input.activeHomeGameplan(GameSimClock.HALF_SECONDS))
    }

    @Test
    fun uiWiresOpponentRotationAndHalftimePreset() {
        val preview = repoRoot.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/preview/GamePreviewScreen.kt",
        ).readText()
        assertTrue(preview.contains("GamePreviewRotationPanel"))
        val launch = repoRoot.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/sim/GameWatchLaunch.kt",
        ).readText()
        assertTrue(launch.contains("homeHalftimeGameplan"))
        val strategy = repoRoot.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/strategy/StrategyScreen.kt",
        ).readText()
        assertTrue(strategy.contains("GameWatchLaunch.homeHalftimeGameplan"))
    }
}
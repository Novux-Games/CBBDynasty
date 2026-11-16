package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.discipline.AcademicCoverUpAuditPolicy
import dev.novux.dynasty.cbb.core.discipline.ConferenceDrawAnimatedSurfacePolicy
import dev.novux.dynasty.cbb.core.discipline.DisciplineByeWeekSuspensionPolicy
import dev.novux.dynasty.cbb.core.discipline.DisciplineAdTrustNewsPolicy
import dev.novux.dynasty.cbb.core.discipline.StrategySuspendedVisualPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyEmptySlotAssignPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyInjuredGreyPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyReservesDepthPolicy
import dev.novux.dynasty.cbb.core.news.NewsHeadlineTypeBreadthPolicy
import dev.novux.dynasty.cbb.core.news.NewsHubDraftInSeasonBoardPolicy
import dev.novux.dynasty.cbb.core.news.NewsHubShellChromeParityPolicy
import dev.novux.dynasty.cbb.core.news.NewsPortalHeadlineFeedSurfacePolicy
import dev.novux.dynasty.cbb.core.news.ShellStoryMiniArticleSurfacePolicy
import dev.novux.dynasty.cbb.core.notification.NotificationFeedBrandingStringsPolicy
import dev.novux.dynasty.cbb.core.notification.WeekAdvanceFeedSnapshotPolicy
import dev.novux.dynasty.cbb.core.pack.PackConferenceBracketSizePolicy
import dev.novux.dynasty.cbb.core.pack.PackDirectoryMembershipPolicy
import dev.novux.dynasty.cbb.core.pack.PackRegenSingleSourcePolicy
import dev.novux.dynasty.cbb.core.preview.GamePreviewWinPctSurfacePolicy
import dev.novux.dynasty.cbb.core.shell.LauncherForegroundCiPolicy
import dev.novux.dynasty.cbb.core.shell.PublicAssetsForegroundSyncPolicy
import dev.novux.dynasty.cbb.core.shell.ResumeSkipStudioLifecyclePolicy
import dev.novux.dynasty.cbb.core.shell.ShellChromeProductTitlePolicy
import dev.novux.dynasty.cbb.core.shell.SplashReadySpinnerPolicy
import dev.novux.dynasty.cbb.core.sim.SimBenchRotationBandPolicy
import dev.novux.dynasty.cbb.core.sim.SimOvertimeTieBreakPolicy
import dev.novux.dynasty.cbb.core.sim.SimUsageOffenseSharePolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch33PathTest {
    @Test
    fun batch33ShellSimWorldPolicies() {
        assertEquals("MEGA-SHELL-037", NewsHubDraftInSeasonBoardPolicy.DYNAMIC_BOARD_MEGA_ID)
        assertEquals("MEGA-SHELL-038", ShellStoryMiniArticleSurfacePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-039", NewsPortalHeadlineFeedSurfacePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-040", NewsHeadlineTypeBreadthPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-044", NotificationFeedBrandingStringsPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-048", WeekAdvanceFeedSnapshotPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-062", ConferenceDrawAnimatedSurfacePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-065", DisciplineByeWeekSuspensionPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-067", StrategySuspendedVisualPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-069", AcademicCoverUpAuditPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-070", DisciplineAdTrustNewsPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-087", NotificationComposerLintPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-161", ShellChromeProductTitlePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-169", LauncherForegroundCiPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-170", PublicAssetsForegroundSyncPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-176", ResumeSkipStudioLifecyclePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-177", NewsHubShellChromeParityPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-179", SplashReadySpinnerPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-047", SimUsageOffenseSharePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-052", GamePreviewWinPctSurfacePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-060", StrategyEmptySlotAssignPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-062", SimBenchRotationBandPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-063", StrategyReservesDepthPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-075", StrategyInjuredGreyPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-085", SimOvertimeTieBreakPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-006", PackDirectoryMembershipPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-010", PackRegenSingleSourcePolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-017", PackConferenceBracketSizePolicy.MEGA_ID)

        assertEquals(8, PackConferenceBracketSizePolicy.bracketSize(5))
        assertTrue(SimOvertimeTieBreakPolicy.requiresOvertime(70, 70, true))

        val root = repoRoot()
        assertTrue(
            root.resolve("android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/notification/NotificationFeedComposer.kt")
                .readText()
                .contains("NotificationFeedBrandingStringsPolicy"),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
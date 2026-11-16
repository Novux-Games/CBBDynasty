package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.GameConstants
import dev.novux.dynasty.cbb.core.news.NewsHubDraftInSeasonBoardPolicy
import dev.novux.dynasty.cbb.core.preseason.IncomingClassSeedCancelPolicy
import dev.novux.dynasty.cbb.core.rankings.RankingsHubRecruitsCrossLinkPolicy
import dev.novux.dynasty.cbb.core.reports.PreseasonReportGenerator
import dev.novux.dynasty.cbb.core.reports.PreseasonReportRecruitingOutlookPolicy
import dev.novux.dynasty.cbb.core.shell.DynastyShellBrandSurfacePolicy
import dev.novux.dynasty.cbb.core.shell.StudioSplashTapHintPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch30PathTest {
    @Test
    fun batch30ShellAndPreseasonWired() {
        assertEquals("MEGA-RECR-114", PreseasonReportRecruitingOutlookPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-115", IncomingClassSeedCancelPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-123", RankingsHubRecruitsCrossLinkPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-150", RankingsHubRecruitsCrossLinkPolicy.SHELL_ID)
        assertEquals("MEGA-SHELL-155", NewsHubDraftInSeasonBoardPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-159", StudioSplashTapHintPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-175", DynastyShellBrandSurfacePolicy.MEGA_ID)

        val outlook = PreseasonReportRecruitingOutlookPolicy.outlookParagraph("Top 60 headline", 60)
        assertTrue(outlook.contains("News hub Recruits"))

        val prospect = RecruitProspect(
            prospectId = "p1",
            displayName = "Test",
            stars = 5,
            position = PlayerPosition.PointGuard,
            homeState = "NC",
            nilAskUsd = 1,
            scoutLetterOverall = "A",
        )
        val fallback = NewsHubDraftInSeasonBoardPolicy.fallbackLines(
            publishedResults = emptyList(),
            topRecruits = listOf(NationalRankRow(1, prospect)),
            inRegularSeason = true,
        )
        assertEquals(1, fallback.size)

        val root = repoRoot()
        assertTrue(root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/dynasty/DynastyShellChrome.kt").readText()
            .contains("DynastyShellBrandSurfacePolicy"))
        assertTrue(root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/rankings/RankingsHubScreen.kt").readText()
            .contains("rankings_hub_open_news_recruits"))
        assertEquals(GameConstants.BRAND_BACKGROUND_HEX.toInt(), DynastyShellBrandSurfacePolicy.shellBackgroundArgb())
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
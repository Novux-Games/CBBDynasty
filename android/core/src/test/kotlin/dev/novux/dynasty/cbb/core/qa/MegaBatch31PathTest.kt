package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId
import dev.novux.dynasty.cbb.core.dashboard.OffseasonWalkOnOptionalDashboardTasks
import dev.novux.dynasty.cbb.core.news.NewsHubShellNewsTabHintPolicy
import dev.novux.dynasty.cbb.core.preseason.PreseasonWorkoutsRecruitingNudgePolicy
import dev.novux.dynasty.cbb.core.recruiting.WalkOnAfterSigningClosePolicy
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.core.shell.NotificationCategorySettingsSectionPolicy
import dev.novux.dynasty.cbb.core.shell.PhaseShellNewsFeedPolicy
import dev.novux.dynasty.cbb.core.signingday.SigningDayRankingsDisclaimerPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch31PathTest {
    @Test
    fun batch31PoliciesAndWiring() {
        assertEquals("MEGA-SHELL-180", PhaseShellNewsFeedPolicy.MEGA_ID)
        assertEquals(3, PhaseShellNewsFeedPolicy.offseasonWeekForFeed(CalendarPhase.Offseason, 3))
        assertEquals("MEGA-SHELL-182", NotificationCategorySettingsSectionPolicy.MEGA_ID)
        assertEquals(5, NotificationCategorySettingsSectionPolicy.configurableCategories.size)
        assertEquals("MEGA-SHELL-177", NewsHubShellNewsTabHintPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-122", SigningDayRankingsDisclaimerPolicy.MEGA_ID)
        assertTrue(SigningDayRankingsDisclaimerPolicy.disclaimerText().contains("Top 60"))
        assertEquals("MEGA-RECR-105", WalkOnAfterSigningClosePolicy.MEGA_ID)
        assertEquals("MEGA-RECR-128", WalkOnAfterSigningClosePolicy.CALENDAR_MEGA_ID)
        assertTrue(WalkOnAfterSigningClosePolicy.surfaceWalkOnNudge(true, 8))
        assertFalse(WalkOnAfterSigningClosePolicy.surfaceWalkOnNudge(false, 8))
        assertEquals("MEGA-RECR-118", PreseasonWorkoutsRecruitingNudgePolicy.MEGA_ID)

        val walkOnTasks = OffseasonWalkOnOptionalDashboardTasks.build(true, 8)
        assertEquals(DashboardTaskId.WalkOnTryouts, walkOnTasks.single().id)

        val freshman = RosterPlayer(
            playerId = "p1",
            dynastyId = "d1",
            position = PlayerPosition.SmallForward,
            classYear = PlayerClassYear.Freshman,
            isStarter = false,
            depthOrder = 12,
            inTransferPortal = false,
            displayName = "Commit",
        )
        assertTrue(PreseasonWorkoutsRecruitingNudgePolicy.showRecruitingNudge(listOf(freshman)))

        val root = repoRoot()
        val shellFeed = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/dynasty/ShellShallowNewsFeed.kt",
        ).readText()
        assertTrue(shellFeed.contains("calendarPhase"))
        assertTrue(shellFeed.contains("PhaseShellNewsFeedPolicy"))
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/signingday/SigningDayScreen.kt")
                .readText()
                .contains("SigningDayRankingsDisclaimerPolicy"),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
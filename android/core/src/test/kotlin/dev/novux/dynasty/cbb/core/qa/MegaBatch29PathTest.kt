package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId
import dev.novux.dynasty.cbb.core.dashboard.OffseasonDashboardContext
import dev.novux.dynasty.cbb.core.dashboard.OffseasonOptionalDashboardTasks
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.news.AwardsNewsPublishPolicy
import dev.novux.dynasty.cbb.core.news.NewsHeadline
import dev.novux.dynasty.cbb.core.news.NewsHeadlineKind
import dev.novux.dynasty.cbb.core.news.NewsHeadlinePublisher
import dev.novux.dynasty.cbb.core.news.PreseasonRankingsReviewNewsEngine
import dev.novux.dynasty.cbb.core.news.RecruitingSpotlightTop60Policy
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeed
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeedCapPolicy
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeedPriorityBoostPolicy
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeedSortContext
import dev.novux.dynasty.cbb.core.news.Top25NetMovementNewsEngine
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.signingday.IncomingSignee
import dev.novux.dynasty.cbb.core.signingday.SigningDayPositionFlexPolicy
import dev.novux.dynasty.cbb.core.signingday.SigningDayPrepBriefPolicy
import dev.novux.dynasty.cbb.core.signingday.SigningDayRankingsTop60ContextPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch29PathTest {
    @Test
    fun batch29GameplayAndNewsWired() {
        assertEquals("MEGA-RECR-107", SigningDayPositionFlexPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-108", SigningDayRankingsTop60ContextPolicy.MEGA_ID)
        assertEquals("MEGA-RECR-112", SigningDayPrepBriefPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-140", AwardsNewsPublishPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-142", RecruitingSpotlightTop60Policy.MEGA_ID)
        assertEquals("MEGA-SHELL-145", ShallowNewsFeedCapPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-146", ShallowNewsFeedPriorityBoostPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-148", Top25NetMovementNewsEngine.MEGA_ID)
        assertEquals("MEGA-SHELL-152", PreseasonRankingsReviewNewsEngine.MEGA_ID)

        assertTrue(
            SigningDayPositionFlexPolicy.isValidAssignment(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard),
        )
        assertTrue(
            SigningDayPositionFlexPolicy.isValidAssignment(PlayerPosition.Center, PlayerPosition.Center),
        )

        val signee = IncomingSignee(
            prospectId = "p1",
            displayName = "Test",
            stars = 4,
            position = PlayerPosition.ShootingGuard,
            homeState = "OH",
            nilAskUsd = 100_000,
            nilSignedUsd = 80_000,
            nationalHsRank = 12,
            scoutLetterOverall = "B+",
        )
        assertTrue(
            SigningDayPositionFlexPolicy.boardValid(
                listOf(signee),
                mapOf("p1" to PlayerPosition.PointGuard),
            ),
        )

        assertNotNull(SigningDayRankingsTop60ContextPolicy.contextLine("Top 60 spotlight", 12))

        val prepTasks = OffseasonOptionalDashboardTasks.build(
            OffseasonDashboardContext(
                offseasonWeekIndex = 5,
                recruitingPointsAvailable = 100,
                portalTop40Locked = false,
                openScholarshipSlots = 0,
                signingCrunchActive = true,
            ),
        )
        assertTrue(prepTasks.any { it.id == DashboardTaskId.SigningDayPrepBrief })

        val portalHeadline = NewsHeadline(
            id = "p1",
            seasonIndex = 1,
            kind = NewsHeadlineKind.PortalCommit,
            title = "Portal",
            priority = 40,
        )
        val boosted = ShallowNewsFeedPriorityBoostPolicy.effectivePriority(
            portalHeadline,
            ShallowNewsFeedSortContext(portalWindowOpen = true),
        )
        assertTrue(boosted > portalHeadline.priority)

        assertEquals(10, ShallowNewsFeedCapPolicy.effectiveCap(10))
        val snap = ShallowNewsFeed.snapshot(emptyList(), maxItems = NewsHeadlinePublisher.SHALLOW_FEED_CAP)
        assertEquals(10, snap.cap)

        val top25 = Top25NetMovementNewsEngine.headlineForWeek(
            LeagueStandingsState(
                seasonIndex = 1,
                netRankByTeam = mapOf("a" to 1, "b" to 2, "c" to 3),
                lastCommittedRsWeek = 3,
            ),
            rsWeekIndex = 3,
            teamDisplayName = { it.uppercase() },
        )
        assertNotNull(top25)

        assertEquals(
            NewsHeadlineKind.PollNetMovement,
            PreseasonRankingsReviewNewsEngine.headlineAfterPublication(1, "Duke", 5, 8).kind,
        )

        val root = repoRoot()
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/signingday/SigningDayPositionPickerSheet.kt")
                .readText()
                .contains("SigningDayPositionFlexPolicy"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/news/NewsFeedRepository.kt")
                .readText()
                .contains("Top25NetMovementNewsEngine"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/rankings/RankingsHubRepository.kt")
                .readText()
                .contains("PreseasonRankingsReviewNewsEngine"),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
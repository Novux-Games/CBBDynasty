package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.carousel.CarouselLeagueBackgroundSimEngine
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.news.CoachingCarouselLeagueNewsEngine
import dev.novux.dynasty.cbb.core.news.NewsHeadlineKind
import dev.novux.dynasty.cbb.core.news.PostseasonTournamentNewsEngine
import dev.novux.dynasty.cbb.core.notification.NotificationRankingsMoveFeedPolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubReadOnlyCyclePolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubTab
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.signingday.SigningDayUnsignedElsewhereEngine
import dev.novux.dynasty.cbb.core.signingday.SigningDayUnsignedElsewherePolicy
import dev.novux.dynasty.cbb.core.sim.PossessionLateClockSimPolicy
import dev.novux.dynasty.cbb.core.sim.PossessionLogFormatPolicy
import dev.novux.dynasty.cbb.core.statistics.StatisticsHubNationalLeadersPolicy
import dev.novux.dynasty.cbb.core.statistics.StatisticsHubSnapshot
import dev.novux.dynasty.cbb.core.statistics.StatisticsLeaderboards
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch28PathTest {
    @Test
    fun batch28GameplayAndShellWired() {
        assertEquals("MEGA-RECR-106", SigningDayUnsignedElsewherePolicy.MEGA_ID)
        assertEquals("MEGA-RECR-113", RecruitingHubReadOnlyCyclePolicy.MEGA_ID)
        assertEquals(listOf(RecruitingHubTab.Committed, RecruitingHubTab.Lost), RecruitingHubReadOnlyCyclePolicy.tabs)
        assertEquals("MEGA-SHELL-139", CoachingCarouselLeagueNewsEngine.MEGA_ID)
        assertEquals("MEGA-SHELL-143", PostseasonTournamentNewsEngine.MEGA_ID)
        assertEquals("MEGA-SHELL-151", StatisticsHubNationalLeadersPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-153", NotificationRankingsMoveFeedPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-006", PossessionLateClockSimPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-010", PossessionLogFormatPolicy.MEGA_ID)

        val pool = listOf(
            RecruitProspect(
                prospectId = "e1",
                displayName = "Elite One",
                stars = 5,
                position = PlayerPosition.PointGuard,
                homeState = "TX",
                nilAskUsd = 200_000,
                scoutLetterOverall = "A",
                nationalHsRank = 3,
            ),
            RecruitProspect(
                prospectId = "e2",
                displayName = "Elite Two",
                stars = 4,
                position = PlayerPosition.Center,
                homeState = "CA",
                nilAskUsd = 80_000,
                scoutLetterOverall = "B+",
            ),
        )
        val elsewhere = SigningDayUnsignedElsewhereEngine.resolve(
            pool = pool,
            signedProspectIds = emptySet(),
            aiSchoolNames = listOf("Kentucky", "Duke"),
            seed = 42L,
        )
        assertTrue(elsewhere.isNotEmpty())
        assertEquals("Elite One", elsewhere.first().displayName)

        assertNotNull(
            NotificationRankingsMoveFeedPolicy.feedLine(12, 6),
        )
        assertEquals(null, NotificationRankingsMoveFeedPolicy.feedLine(10, 12))

        val carouselHeadline = CoachingCarouselLeagueNewsEngine.headlineAfterBackgroundSim(
            seasonIndex = 1,
            summary = CarouselLeagueBackgroundSimEngine.CarouselBackgroundSimSummary(
                aiFired = 2,
                aiExtended = 1,
                aiHired = 3,
                userFired = false,
                userPoachImmunity = false,
            ),
        )
        assertNotNull(carouselHeadline)
        assertEquals(NewsHeadlineKind.CoachingCarousel, carouselHeadline!!.kind)

        val postseasonHeadline = PostseasonTournamentNewsEngine.headlineForPostseasonWeek(
            standings = LeagueStandingsState(seasonIndex = 1, netRankByTeam = mapOf("duke" to 4)),
            postseasonWeek = 2,
            userTeamId = "duke",
            teamDisplayName = { "Duke" },
        )
        assertNotNull(postseasonHeadline)
        assertEquals(NewsHeadlineKind.PostseasonTournament, postseasonHeadline!!.kind)

        val line = PossessionLogFormatPolicy.formatLine(
            clockLabel = "14:32",
            offenseLabel = "HOME",
            usedSeconds = 18,
            shotClockBefore = 30,
            shotClockAfter = 12,
            scoreLine = "miss",
            playerAttribution = "#12 Jordan Ellis 3PT miss",
        )
        assertTrue(line.startsWith("14:32 | HOME | #12 Jordan Ellis 3PT miss |"))

        val root = repoRoot()
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/recruiting/RecruitingHubScreen.kt")
                .readText()
                .contains("recruiting_hub_read_only_banner"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/league/LeagueWeekBatchRepository.kt")
                .readText()
                .contains("PostseasonTournamentNewsEngine"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/signingday/SigningDayScreen.kt")
                .readText()
                .contains("SigningDayUnsignedElsewhereEngine"),
        )
        assertTrue(
            StatisticsHubNationalLeadersPolicy.nationalLeadersLive(
                StatisticsHubSnapshot(
                    seasonIndex = 1,
                    userConferenceId = "acc",
                    hasStats = true,
                    conferences = emptyList(),
                    national = StatisticsLeaderboards(
                        points = emptyList(),
                        assists = emptyList(),
                        rebounds = emptyList(),
                        steals = emptyList(),
                        blocks = emptyList(),
                    ),
                    conferenceLeadersById = emptyMap(),
                ),
            ),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
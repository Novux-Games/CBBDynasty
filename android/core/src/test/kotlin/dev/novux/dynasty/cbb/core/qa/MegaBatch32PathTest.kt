package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.carousel.CarouselExtensionOfferSurfacePolicy
import dev.novux.dynasty.cbb.core.coach.employment.UnemployedSpectateGamePolicy
import dev.novux.dynasty.cbb.core.coach.skill.SundayInSeasonCoachSkillGrantPolicy
import dev.novux.dynasty.cbb.core.league.PostseasonBatchSelectionBootstrapPolicy
import dev.novux.dynasty.cbb.core.march.MarchMayhemHubMonitorPolicy
import dev.novux.dynasty.cbb.core.news.ShellNewsFeedDateChipPolicy
import dev.novux.dynasty.cbb.core.news.ShellNewsHeadlineTapPolicy
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeedDashboardMergePolicy
import dev.novux.dynasty.cbb.core.pack.IdentityPackUsStateCodePolicy
import dev.novux.dynasty.cbb.core.schedule.PreseasonNonConfCapPolicy
import dev.novux.dynasty.cbb.core.selection.SelectionSundayConfTourneyHandoffPolicy
import dev.novux.dynasty.cbb.core.shell.CreditsReplayStudioLinkPolicy
import dev.novux.dynasty.cbb.core.shell.HomeMenuBrandLogoPolicy
import dev.novux.dynasty.cbb.core.shell.PhaseShellNewsFeedPolicy
import dev.novux.dynasty.cbb.core.sim.GameWatchHalftimeRotationPolicy
import dev.novux.dynasty.cbb.core.school.ReclassTransitionClockUxPolicy
import dev.novux.dynasty.cbb.core.school.SchoolHubPackIdentityPolicy
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch32PathTest {
    @Test
    fun batch32GddShellWorldPostWired() {
        assertEquals("MEGA-GDD-009", GameWatchHalftimeRotationPolicy.MEGA_ID)
        assertEquals("MEGA-GDD-105", PreseasonNonConfCapPolicy.MEGA_ID)
        assertEquals(32, PreseasonNonConfCapPolicy.regularSeasonCap)
        assertEquals("MEGA-GDD-120", CarouselExtensionOfferSurfacePolicy.MEGA_ID)
        assertEquals("MEGA-GDD-122", UnemployedSpectateGamePolicy.MEGA_ID)
        assertEquals("MEGA-GDD-125", SundayInSeasonCoachSkillGrantPolicy.MEGA_ID)
        assertTrue(SundayInSeasonCoachSkillGrantPolicy.grantsInPhase(CalendarPhase.RegularSeason))
        assertEquals("MEGA-GDD-129", MarchMayhemHubMonitorPolicy.MEGA_ID)
        assertEquals("MEGA-POST-054", MarchMayhemHubMonitorPolicy.POST_SUBTITLE_ID)
        assertEquals("MEGA-GDD-145", ReclassTransitionClockUxPolicy.MEGA_ID)

        assertEquals("MEGA-SHELL-016", HomeMenuBrandLogoPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-018", CreditsReplayStudioLinkPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-021", ShallowNewsFeedDashboardMergePolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-180", PhaseShellNewsFeedPolicy.MEGA_ID)
        assertEquals("MEGA-SHELL-024", ShellNewsHeadlineTapPolicy.MEGA_ID)
        assertTrue(ShellNewsHeadlineTapPolicy.opensStoryDetail())
        assertEquals("MEGA-SHELL-025", ShellNewsFeedDateChipPolicy.MEGA_ID)
        assertEquals("W3", ShellNewsFeedDateChipPolicy.dateChip(0L, 3))

        assertEquals("MEGA-WORLD-041", IdentityPackUsStateCodePolicy.MEGA_ID)
        assertTrue(IdentityPackUsStateCodePolicy.isValid("NC"))
        assertFalse(IdentityPackUsStateCodePolicy.isValid("XX"))
        assertEquals("MEGA-WORLD-110", SchoolHubPackIdentityPolicy.GEOGRAPHY_MEGA_ID)
        assertEquals("Durham, NC", SchoolHubPackIdentityPolicy.geographyLine("Durham", "NC"))

        assertEquals("MEGA-POST-005", SelectionSundayConfTourneyHandoffPolicy.MEGA_ID)
        assertEquals("MEGA-POST-006", PostseasonBatchSelectionBootstrapPolicy.MEGA_ID)

        val root = repoRoot()
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/shell/HomeMenuScreen.kt")
                .readText()
                .contains("HomeMenuBrandLogoPolicy"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/conference/ConferenceTournamentRepository.kt")
                .readText()
                .contains("SelectionSundayConfTourneyHandoffPolicy"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/league/LeagueWeekBatchRepository.kt")
                .readText()
                .contains("bootstrapSelectionSunday"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/school/SchoolHubOverviewCard.kt")
                .readText()
                .contains("SchoolHubPackIdentityPolicy"),
        )
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/dynasty/DynastyShellTabContent.kt")
                .readText()
                .contains("ShellNewsFeedDateChipPolicy"),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
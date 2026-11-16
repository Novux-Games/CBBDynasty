package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.awards.AllAmericaSelectionEngine
import dev.novux.dynasty.cbb.core.awards.NationalAwardScoreEngine
import dev.novux.dynasty.cbb.core.awards.WeeklyPotwBatchEngine
import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendarGameRoundPolicy
import dev.novux.dynasty.cbb.core.dashboard.DashboardMarchMayhemTeaserPolicy
import dev.novux.dynasty.cbb.core.history.HistoryGoatByPositionSurfacePolicy
import dev.novux.dynasty.cbb.core.coach.employment.JobCenterPackTeamListPolicy
import dev.novux.dynasty.cbb.core.league.LeagueFastSimTuningPolicy
import dev.novux.dynasty.cbb.core.league.LeagueScheduleBootstrapParityPolicy
import dev.novux.dynasty.cbb.core.pack.ConferenceTournamentVenueManifestPolicy
import dev.novux.dynasty.cbb.core.pack.IdentityPackConferenceOverlayPolicy
import dev.novux.dynasty.cbb.core.march.MarchMayhemHubSurfacePolicy
import dev.novux.dynasty.cbb.core.pack.ConferenceShortCodePolicy
import dev.novux.dynasty.cbb.core.pack.DefaultPackConferenceSizeManifestPolicy
import dev.novux.dynasty.cbb.core.pack.IdentityPackCodec
import dev.novux.dynasty.cbb.core.pack.PackBrandingEmbeddedPolicy
import dev.novux.dynasty.cbb.core.pack.PackBrandingRoundResolver
import dev.novux.dynasty.cbb.core.pack.PackLoadResult
import dev.novux.dynasty.cbb.core.pack.PackWorldBatch34TracePolicy
import dev.novux.dynasty.cbb.core.pack.ReclassTransitionEligibilityEngine
import dev.novux.dynasty.cbb.core.rankings.RankingsHubPlaceholderPolicy
import dev.novux.dynasty.cbb.core.school.SchoolHubConferenceStandingPolicy
import dev.novux.dynasty.cbb.core.school.SchoolHubPostseasonEligibilityPolicy
import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord
import dev.novux.dynasty.cbb.core.selection.HeritageSelectionEligibilityPolicy
import dev.novux.dynasty.cbb.core.selection.ProjectedBracketForbiddenPolicy
import dev.novux.dynasty.cbb.core.selection.SelectionSundayBubbleEngine
import dev.novux.dynasty.cbb.core.selection.SelectionSundayNetFreezePolicy
import dev.novux.dynasty.cbb.core.selection.SelectionSundayRevealPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch34PathTest {
    @Test
    fun batch34WorldPackPolicies() {
        assertEquals("MEGA-WORLD-019", PackBrandingEmbeddedPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-035", PackBrandingRoundResolver.MEGA_ID)
        assertEquals("MEGA-WORLD-046", ReclassTransitionEligibilityEngine.MEGA_ID)
        assertEquals("MEGA-WORLD-037", ConferenceShortCodePolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-068", LeagueFastSimTuningPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-078", RankingsHubPlaceholderPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-113", SchoolHubConferenceStandingPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-114", SchoolHubPostseasonEligibilityPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-147", DefaultPackConferenceSizeManifestPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-144", ConferenceTournamentVenueManifestPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-151", IdentityPackConferenceOverlayPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-065", LeagueScheduleBootstrapParityPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-152", JobCenterPackTeamListPolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-025", PackWorldBatch34TracePolicy.PROGRAM_PRESTIGE_OPTIONAL_MEGA_ID)
        assertEquals("MEGA-WORLD-119", PackWorldBatch34TracePolicy.IMPORT_SETTINGS_MEGA_ID)

        assertEquals("Opening Round", PackBrandingRoundResolver.ncbaRoundLabel(1))
        assertEquals(
            "Postseason Week 4 — Super 16",
            PostseasonCalendar.displayWeekHeader(4),
        )
        assertEquals("PR12", ConferenceShortCodePolicy.shortCode("conf-pr12", "Pacific Rim Twelve"))
        assertTrue(LeagueFastSimTuningPolicy.usePossessionEngine(true))
        assertFalse(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 1, 1))
        assertTrue(ReclassTransitionEligibilityEngine.effectivePostseasonEligible(false, 1, 5))

        val label = SchoolHubConferenceStandingPolicy.label(
            record = SchoolSeasonRecord(10, 2, 8, 1),
            prestigeOrderIndex = 0,
            conferenceSize = 12,
        )
        assertEquals("8-1 conference", label)
    }

    @Test
    fun embeddedDefaultHasBrandingBlock() {
        val root = repoRoot()
        val raw = root.resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        assertTrue(PackBrandingEmbeddedPolicy.hasRoundKeys(pack.branding))
        assertTrue(pack.conferences.all { !it.shortCode.isNullOrBlank() })
    }

    @Test
    fun batch34PostseasonAwardsPoliciesWired() {
        assertEquals("MEGA-POST-029", HeritageSelectionEligibilityPolicy.MEGA_ID)
        assertEquals("MEGA-POST-144", HeritageSelectionEligibilityPolicy.RECLASS_AWARE_ID)
        assertEquals("MEGA-POST-021", SelectionSundayNetFreezePolicy.MEGA_ID)
        assertEquals("MEGA-POST-142", SelectionSundayNetFreezePolicy.WIRE_COMPUTE_ID)
        assertFalse(ProjectedBracketForbiddenPolicy.allowsInSeasonProjectedBracket())
        assertEquals("MEGA-POST-033", SelectionSundayRevealPolicy.MEGA_ID)
        assertEquals("MEGA-POST-034", SelectionSundayBubbleEngine.MEGA_ID)
        assertEquals("MEGA-POST-003", PostseasonCalendarGameRoundPolicy.MEGA_ID)
        assertEquals("MEGA-NOTES-018", PostseasonCalendarGameRoundPolicy.NOTES_CALENDAR_ID)
        assertEquals("MEGA-POST-046", MarchMayhemHubSurfacePolicy.OVERVIEW_ID)
        assertEquals("MEGA-POST-050", MarchMayhemHubSurfacePolicy.MY_TEAM_FILTER_ID)
        assertEquals("MEGA-POST-056", DashboardMarchMayhemTeaserPolicy.SEED_ROW_ID)
        assertEquals("MEGA-POST-071", AllAmericaSelectionEngine.MEGA_ID)
        assertEquals("MEGA-POST-070", NationalAwardScoreEngine.MEGA_ID)
        assertEquals("MEGA-POST-160", WeeklyPotwBatchEngine.CONF_POTW_ID)
        assertEquals("MEGA-POST-092", HistoryGoatByPositionSurfacePolicy.MEGA_ID)

        val root = repoRoot()
        assertTrue(
            root.resolve(
                "android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/conference/ConferenceTournamentRepository.kt",
            ).readText().contains("freezeFinalNetBeforeSelection"),
        )
        assertTrue(
            root.resolve(
                "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/history/HistoryHubScreen.kt",
            ).readText().contains("HistoryGoatByPositionSurfacePolicy"),
        )
        assertTrue(
            root.resolve(
                "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/selection/SelectionSundayRevealScreen.kt",
            ).readText().contains("SelectionSundayBubbleEngine"),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
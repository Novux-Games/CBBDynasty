package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.coach.MegaCoachBatch35TracePolicy
import dev.novux.dynasty.cbb.core.gdd.MegaGddBatch35TracePolicy
import dev.novux.dynasty.cbb.core.gdd.OfflineInternetPermissionPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyResetStartersPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyStarterWirePolicy
import dev.novux.dynasty.cbb.core.pack.ConferenceTournamentVenuePolicy
import dev.novux.dynasty.cbb.core.pack.DefaultPackConferenceSizeManifestPolicy
import dev.novux.dynasty.cbb.core.pack.TournamentRoundPack
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import dev.novux.dynasty.cbb.core.recruiting.MegaRecruitBatch35TracePolicy
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspectPortraitPolicy
import dev.novux.dynasty.cbb.core.sim.GameWatchPhaseBadgePolicy
import dev.novux.dynasty.cbb.core.sim.HalftimeOpeningFivePolicy
import dev.novux.dynasty.cbb.core.sim.PlayerBoxPlusMinusPolicy
import dev.novux.dynasty.cbb.core.sim.PossessionClutchLogPolicy
import dev.novux.dynasty.cbb.core.sim.PossessionRareEventPolicy
import dev.novux.dynasty.cbb.core.sim.WinSharesUiDocPolicy
import dev.novux.dynasty.cbb.core.strategy.HalftimeGameplanPersistPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch35PathTest {
    @Test
    fun batch35SimAndWorldPolicies() {
        assertEquals("MEGA-SIM-116", PossessionClutchLogPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-096", PossessionRareEventPolicy.MEGA_JUMP_BALL_ID)
        assertEquals("MEGA-SIM-107", PossessionRareEventPolicy.MEGA_TECHNICAL_ID)
        assertEquals("MEGA-SIM-110", HalftimeOpeningFivePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-128", GameWatchPhaseBadgePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-134", PlayerBoxPlusMinusPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-142", StrategyResetStartersPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-053", HalftimeGameplanPersistPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-058", StrategyStarterWirePolicy.MEGA_WIRE_058)
        assertEquals("MEGA-WORLD-144", ConferenceTournamentVenuePolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-147", DefaultPackConferenceSizeManifestPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-174", WinSharesUiDocPolicy.MEGA_ID)
        assertEquals("MEGA-GDD-092", OfflineInternetPermissionPolicy.MEGA_ID)
        assertTrue(WinSharesUiDocPolicy.HINT.contains("season end"))
        assertEquals(
            "Neutral site",
            ConferenceTournamentVenuePolicy.venueLabel("neutral-primary", "Template"),
        )
        val venue = ConferenceTournamentVenuePolicy.displayVenue(
            TournamentTemplatePack(
                id = "tpl-test",
                bracketSize = 8,
                seeds = 8,
                sitePolicy = "neutral-primary",
                qualificationPolicy = "top4",
                championshipVenueName = "Test Arena",
                rounds = listOf(TournamentRoundPack("Final", 1)),
            ),
        )
        assertEquals("Test Arena", venue)
    }

    @Test
    fun sim118LateHomeCrowdWired() {
        assertTrue(
            repoRoot().resolve(
                "android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/sim/PossessionSimSession.kt",
            ).readText().contains("lateGameHomeCrowdOffenseBoost"),
        )
    }

    @Test
    fun coach103RecruitPortrait() {
        assertEquals("MEGA-COACH-103", RecruitProspectPortraitPolicy.MEGA_ID)
        assertEquals("MEGA-COACH-103", MegaCoachBatch35TracePolicy.MEGA_103)
        val hub = repoRoot().resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/recruiting/RecruitingHubScreen.kt",
        ).readText()
        assertTrue(hub.contains("RecruitProspectPortraitSlot"))
    }

    @Test
    fun assistsAttributedInPossessionRecorder() {
        assertTrue(
            repoRoot().resolve(
                "android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/sim/PlayerPossessionStatRecorder.kt",
            ).readText().contains("addAssist"),
        )
    }

    @Test
    fun batch35QueueTraceIds() {
        assertEquals("MEGA-RECR-016", MegaRecruitBatch35TracePolicy.MEGA_016)
        assertEquals("MEGA-GDD-076", MegaGddBatch35TracePolicy.MEGA_076)
        val overlay = repoRoot().resolve(
            "android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/pack/TeamDisplayOverlay.kt",
        ).readText()
        assertTrue(overlay.contains("standingsTiebreakers"))
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
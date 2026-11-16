package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.pack.ConferenceTournamentVenuePolicy
import dev.novux.dynasty.cbb.core.pack.DefaultPackConferenceSizeManifestPolicy
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import dev.novux.dynasty.cbb.core.pack.TournamentRoundPack
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspectPortraitPolicy
import dev.novux.dynasty.cbb.core.sim.SimVenueRatingPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch35PathTest {
    @Test
    fun worldQueueVenueAndDirectory() {
        assertEquals("MEGA-WORLD-144", ConferenceTournamentVenuePolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-147", DefaultPackConferenceSizeManifestPolicy.MEGA_ID)
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
        val root = repoRoot()
        val overlay = root.resolve(
            "android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/pack/TeamDisplayOverlay.kt",
        ).readText()
        assertTrue(overlay.contains("standingsTiebreakers"))
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

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
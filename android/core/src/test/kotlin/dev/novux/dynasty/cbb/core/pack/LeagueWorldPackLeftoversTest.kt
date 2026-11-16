package dev.novux.dynasty.cbb.core.pack

import dev.novux.dynasty.cbb.core.coach.employment.JobCenterPackTeamListPolicy
import dev.novux.dynasty.cbb.core.league.LeagueScheduleBootstrapParityPolicy
import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Residual league-world-pack queue rows ([MEGA-WORLD-065], 144, 147, 151, 152). */
class LeagueWorldPackLeftoversTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/pack/embedded-default.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/pack/embedded-default.json from cwd")
    }

    @Test
    fun conferenceOverlaysExposeTiebreakers() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        val index = IdentityPackIndex.from(pack)
        val overlays = index.allConferenceDisplays()
        assertEquals(32, overlays.size)
        assertTrue(overlays.all { IdentityPackConferenceOverlayPolicy.exposesTiebreakers(it) })
    }

    @Test
    fun tournamentTemplatesCarryChampionshipVenueLabels() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        assertEquals(32, pack.tournamentTemplates.size)
        assertTrue(
            pack.tournamentTemplates.all { ConferenceTournamentVenueManifestPolicy.templateHasVenueLabel(it) },
        )
    }

    @Test
    fun leagueBootstrapUsesPackConferenceCaps() {
        val overlay = ConferenceDisplayOverlay(
            conferenceId = "conf-g10",
            displayName = "Great Ten",
            leagueGameCount = 20,
            tournamentTemplateId = "tpl-conf-g10",
            standingsTiebreakers = listOf("h2h"),
        )
        assertEquals(20, LeagueScheduleBootstrapParityPolicy.sharedLeagueGameCount(overlay))
    }

    @Test
    fun jobCenterDrawsFromPackTeamList() {
        val teams = listOf(
            TeamDisplayOverlay(
                teamId = "team-a",
                conferenceId = "conf-a",
                displayName = "Alpha",
                shortName = "ALP",
                homeCity = "Town",
                homeState = "ST",
                primaryColorHex = "#112233",
                secondaryColorHex = "#445566",
                logoUri = null,
                postseasonEligible = true,
                programPrestige = 70,
            ),
        )
        val offers = JobCenterPackTeamListPolicy.generateFromPack(teams, seasonIndex = 1, dynastyId = "dyn")
        assertEquals("Alpha", offers.first().displayName)
    }
}
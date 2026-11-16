package dev.novux.dynasty.cbb.core.pack

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Test

/** Championship venue column on pack templates ([MEGA-WORLD-144]). */
class DefaultPackConferenceChampionshipVenueTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/pack/embedded-default.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/pack/embedded-default.json from cwd")
    }

    @Test
    fun embeddedDefault_championshipVenuesMatchDirectoryManifest() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        assertEquals(
            DefaultPackConferenceChampionshipVenueManifestPolicy.expectedByTemplateId.size,
            pack.tournamentTemplates.size,
        )
        pack.tournamentTemplates.forEach { template ->
            val expected = DefaultPackConferenceChampionshipVenueManifestPolicy.expectedByTemplateId[template.id]
            requireNotNull(expected) { "missing manifest venue for ${template.id}" }
            assertEquals(template.id, expected, template.championshipVenue)
        }
    }
}
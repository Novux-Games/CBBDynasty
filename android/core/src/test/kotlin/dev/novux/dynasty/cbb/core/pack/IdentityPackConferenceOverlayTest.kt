package dev.novux.dynasty.cbb.core.pack

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertTrue
import org.junit.Test

/** Tiebreaker stack exposed on conference overlay ([MEGA-WORLD-151]). */
class IdentityPackConferenceOverlayTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/pack/embedded-default.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/pack/embedded-default.json from cwd")
    }

    @Test
    fun allConferenceDisplaysExposeTiebreakersFromPack() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        val index = IdentityPackIndex.from(pack)
        index.allConferenceDisplays().forEach { overlay ->
            val packConf = pack.conferences.first { it.id == overlay.conferenceId }
            assertEqualsLists(packConf.standingsTiebreakers, overlay.standingsTiebreakers)
            assertTrue(overlay.standingsTiebreakers.isNotEmpty())
        }
    }

    private fun assertEqualsLists(expected: List<String>, actual: List<String>) {
        org.junit.Assert.assertEquals(expected, actual)
    }
}
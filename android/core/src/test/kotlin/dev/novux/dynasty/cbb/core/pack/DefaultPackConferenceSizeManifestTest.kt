package dev.novux.dynasty.cbb.core.pack

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Test

/** Per-conference team counts vs directory membership ([MEGA-WORLD-147]). */
class DefaultPackConferenceSizeManifestTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/pack/embedded-default.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/pack/embedded-default.json from cwd")
    }

  private val expectedSizes = mapOf(
        "conf-nac" to 9,
        "conf-aucc" to 13,
        "conf-cat10" to 14,
        "conf-acrown" to 18,
        "conf-ascu" to 8,
        "conf-c12" to 16,
        "conf-epc" to 11,
        "conf-nsc" to 11,
        "conf-psc" to 9,
        "conf-g10" to 18,
        "conf-gwc" to 12,
        "conf-cca" to 13,
        "conf-ucc" to 10,
        "conf-lhl" to 12,
        "conf-fel" to 8,
        "conf-metro" to 13,
        "conf-mac" to 12,
        "conf-meac" to 8,
        "conf-pvc" to 11,
        "conf-rmwc" to 10,
        "conf-nec" to 9,
        "conf-ova" to 9,
        "conf-pr12" to 9,
        "conf-psl" to 10,
        "conf-sedc" to 16,
        "conf-scf" to 11,
        "conf-gcc" to 12,
        "conf-scal" to 12,
        "conf-summit" to 8,
        "conf-sbc" to 14,
        "conf-uac" to 9,
        "conf-wcc" to 10,
    )

    @Test
    fun embeddedDefault_confSizesMatchDirectoryManifest() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        val actual = pack.teams.groupingBy { it.conferenceId }.eachCount()
        assertEquals(expectedSizes.size, pack.conferences.size)
        expectedSizes.forEach { (confId, expected) ->
            assertEquals("conf size $confId", expected, actual[confId])
        }
    }
}
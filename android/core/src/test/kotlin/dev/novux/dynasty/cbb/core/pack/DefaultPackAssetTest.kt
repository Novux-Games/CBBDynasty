package dev.novux.dynasty.cbb.core.pack

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultPackAssetTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/pack/embedded-default.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/pack/embedded-default.json from cwd")
    }

    @Test
    fun embeddedDefaultPack_validates() {
        val packFile = repoRoot().resolve("assets/pack/embedded-default.json")
        val raw = packFile.readText()
        val result = IdentityPackCodec.parseAndValidate(raw)
        if (result is PackLoadResult.Rejected) {
            throw AssertionError("default pack rejected: ${result.error}")
        }
        val pack = (result as PackLoadResult.Success).pack
        assertEquals(PackConstants.SUPPORTED_PACK_VERSION, pack.packVersion)
        assertEquals(32, pack.conferences.size)
        assertEquals(365, pack.teams.size)
        assertEquals("embedded-default", pack.packId)
        val eligible = pack.teams.count { it.postseasonEligible }
        assertEquals(361, eligible)
        assertEquals(4, pack.teams.count { !it.postseasonEligible })
    }

    @Test
    fun embeddedDefault_teamIds_uniqueAndPrefixed() {
        val raw = repoRoot().resolve("assets/pack/embedded-default.json").readText()
        val pack = (IdentityPackCodec.parseAndValidate(raw) as PackLoadResult.Success).pack
        val ids = pack.teams.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertTrue(ids.all { it.startsWith("team-conf-") })
    }
}

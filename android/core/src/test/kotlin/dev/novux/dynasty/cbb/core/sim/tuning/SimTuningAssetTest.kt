package dev.novux.dynasty.cbb.core.sim.tuning

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Test

class SimTuningAssetTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/sim/sim-tuning.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/sim/sim-tuning.json from cwd")
    }

    @Test
    fun simTuning_embeddedAsset_validates() {
        val raw = repoRoot().resolve("assets/sim/sim-tuning.json").readText()
        val result = SimTuningCodec.parseAndValidate(raw)
        if (result is SimTuningLoadResult.Rejected) {
            throw AssertionError("sim tuning rejected: ${result.error}")
        }
        val tuning = (result as SimTuningLoadResult.Success).tuning
        assertEquals("sim-tuning", tuning.simTuningId)
        assertEquals(1, tuning.schemaVersion)
        assertEquals(60, tuning.possessionsPerGameTarget.min)
        assertEquals(70, tuning.possessionsPerGameTarget.max)
        assertEquals(30, tuning.shotClock.full)
    }
}

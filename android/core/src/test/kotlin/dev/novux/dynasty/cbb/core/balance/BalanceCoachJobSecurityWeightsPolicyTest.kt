package dev.novux.dynasty.cbb.core.balance

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BalanceCoachJobSecurityWeightsPolicyTest {
    private fun repoRoot(): Path {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir ?: error("Could not find assets/balance/balance-2026.json from cwd")
    }

    @Test
    fun shippedBalanceIncludesCoachJobSecurityWeights() {
        val raw = repoRoot().resolve("assets/balance/balance-2026.json").readText()
        val result = BalanceRulesetCodec.parseAndValidate(raw)
        val ruleset = (result as BalanceLoadResult.Success).ruleset
        assertTrue(BalanceCoachJobSecurityWeightsPolicy.weightsPresent(ruleset))
        assertEquals("MEGA-COACH-048", BalanceCoachJobSecurityWeightsPolicy.MEGA_ID)
    }
}
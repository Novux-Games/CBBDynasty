package dev.novux.dynasty.cbb.core.nil

import dev.novux.dynasty.cbb.core.balance.BalanceLoadResult
import dev.novux.dynasty.cbb.core.balance.BalanceNilAnnualPoolParser
import dev.novux.dynasty.cbb.core.balance.BalanceRulesetCodec
import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NilAnnualPoolEngineTest {
    private val tables = BalanceNilAnnualPoolParser.parse(loadRuleset().nil)

    @Test
    fun upperTier_prestige40_interpolatesWithinBand() {
        val usd = NilAnnualPoolEngine.resolve("conf-aucc", 40, tables)
        assertTrue(usd in 900_000L..4_500_000L)
        assertEquals(2_354_545L, usd)
    }

    @Test
    fun powerBlueBlood_prestige95_usesBlueBloodBand() {
        val usd = NilAnnualPoolEngine.resolve("conf-g10", 95, tables)
        assertTrue(usd >= 9_700_000L)
        assertEquals(11_644_444L, usd)
    }

    @Test
    fun upperTier_prestige95_cappedAtTierMax() {
        val usd = NilAnnualPoolEngine.resolve("conf-aucc", 95, tables)
        assertEquals(4_500_000L, usd)
    }

    private fun loadRuleset() =
        (BalanceRulesetCodec.parseAndValidate(loadBalanceJson()) as BalanceLoadResult.Success).ruleset

    private fun loadBalanceJson(): String {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        return dir!!.resolve("assets/balance/balance-2026.json").readText()
    }
}

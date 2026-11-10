package dev.novux.dynasty.cbb.core.balance

import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceRulesetAssetTest {
  private fun repoRoot(): Path {
    var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
    while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
      dir = dir.parent
    }
    return dir ?: error("Could not find assets/balance/balance-2026.json from cwd")
  }

  @Test
  fun balance2026_embeddedAsset_validates() {
    val raw = repoRoot().resolve("assets/balance/balance-2026.json").readText()
    val result = BalanceRulesetCodec.parseAndValidate(raw)
    if (result is BalanceLoadResult.Rejected) {
      throw AssertionError("balance bundle rejected: ${result.error}")
    }
    val ruleset = (result as BalanceLoadResult.Success).ruleset
    assertEquals("balance-2026", ruleset.balanceRulesetId)
    assertEquals(1, ruleset.schemaVersion)
    assertEquals(15, ruleset.calendar.scholarshipLimit)
    assertEquals(15, ruleset.calendar.rosterCap)
    assertEquals(40, ruleset.injuries.bank.size)
  }
}

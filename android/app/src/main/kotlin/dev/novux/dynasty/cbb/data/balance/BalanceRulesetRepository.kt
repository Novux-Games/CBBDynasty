package dev.novux.dynasty.cbb.data.balance

import android.content.Context
import dev.novux.dynasty.cbb.core.balance.BalanceLoadResult
import dev.novux.dynasty.cbb.core.balance.BalanceRuleset
import dev.novux.dynasty.cbb.core.balance.BalanceRulesetCodec
import dev.novux.dynasty.cbb.core.save.DynastySchema
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Loads and holds the active balance ruleset for the session — pinned per dynasty.
 * See [docs/ADR-002-balance-ruleset-bundle.md].
 */
class BalanceRulesetRepository(
  private val context: Context,
) {
  private val mutex = Mutex()
  private var active: BalanceRuleset? = null

  suspend fun ensureShippedDefaultLoaded(): BalanceRuleset = mutex.withLock {
    active?.let { return it }
    loadAsset(DynastySchema.EMBEDDED_BALANCE_ASSET).also { active = it }
  }

  suspend fun activeRuleset(): BalanceRuleset = ensureShippedDefaultLoaded()

  suspend fun restorePinned(rulesetId: String, schemaVersion: Int): BalanceRuleset = mutex.withLock {
    if (rulesetId == DynastySchema.DEFAULT_BALANCE_RULESET_ID) {
      val ruleset = loadAsset(
        DynastySchema.EMBEDDED_BALANCE_ASSET,
        expectedRulesetId = rulesetId,
        expectedSchemaVersion = schemaVersion,
      )
      active = ruleset
      return ruleset
    }
    val assetPath = "balance/$rulesetId.json"
    return try {
      val ruleset = loadAsset(
        assetPath,
        expectedRulesetId = rulesetId,
        expectedSchemaVersion = schemaVersion,
      )
      active = ruleset
      ruleset
    } catch (_: BalanceParseException) {
      throw BalanceParseException(
        dev.novux.dynasty.cbb.core.balance.BalanceValidationError.UnknownRulesetId(rulesetId).toString(),
      )
    }
  }

  private fun loadAsset(
    assetPath: String,
    expectedRulesetId: String? = null,
    expectedSchemaVersion: Int? = null,
  ): BalanceRuleset {
    val raw = context.assets.open(assetPath).bufferedReader().use { it.readText() }
    return when (
      val result = BalanceRulesetCodec.parseAndValidate(
        raw,
        expectedRulesetId,
        expectedSchemaVersion,
      )
    ) {
      is BalanceLoadResult.Success -> result.ruleset
      is BalanceLoadResult.Rejected -> throw BalanceParseException(result.error.toString())
    }
  }
}

class BalanceParseException(message: String) : IllegalStateException(message)

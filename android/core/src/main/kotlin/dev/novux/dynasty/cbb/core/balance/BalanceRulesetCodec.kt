package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object BalanceRulesetCodec {
  private val json = Json {
    ignoreUnknownKeys = false
    isLenient = false
    explicitNulls = false
  }

  fun parseAndValidate(
    raw: String,
    expectedRulesetId: String? = null,
    expectedSchemaVersion: Int? = null,
  ): BalanceLoadResult {
    val decoded = try {
      json.decodeFromString<BalanceRuleset>(raw)
    } catch (e: SerializationException) {
      return BalanceLoadResult.Rejected(BalanceValidationError.MalformedJson(e.message ?: "decode failed"))
    } catch (e: IllegalArgumentException) {
      return BalanceLoadResult.Rejected(BalanceValidationError.MalformedJson(e.message ?: "decode failed"))
    }

    val error = BalanceRulesetValidator.validate(decoded, expectedRulesetId, expectedSchemaVersion)
    return if (error != null) {
      BalanceLoadResult.Rejected(error)
    } else {
      BalanceLoadResult.Success(decoded)
    }
  }
}

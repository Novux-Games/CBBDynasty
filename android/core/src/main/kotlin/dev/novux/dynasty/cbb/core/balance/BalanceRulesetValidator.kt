package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object BalanceRulesetValidator {
  private const val NET_WEIGHT_SUM_TOLERANCE = 0.02
  private const val REQUIRED_NIL_ASK_MULTIPLIER = 3

  fun validate(
    ruleset: BalanceRuleset,
    expectedRulesetId: String? = null,
    expectedSchemaVersion: Int? = null,
  ): BalanceValidationError? {
    expectedRulesetId?.let { expected ->
      if (ruleset.balanceRulesetId != expected) {
        return BalanceValidationError.RulesetIdMismatch(expected, ruleset.balanceRulesetId)
      }
    }
    expectedSchemaVersion?.let { expected ->
      if (ruleset.schemaVersion != expected) {
        return BalanceValidationError.SchemaVersionMismatch(expected, ruleset.schemaVersion)
      }
    }
    if (ruleset.balanceRulesetId.isBlank()) {
      return BalanceValidationError.MalformedJson("balanceRulesetId blank")
    }
    if (ruleset.schemaVersion < 1) {
      return BalanceValidationError.MalformedJson("schemaVersion must be >= 1")
    }
    if (ruleset.calendar.scholarshipLimit != BalanceConstants.SCHOLARSHIP_LIMIT) {
      return BalanceValidationError.InvalidCalendar(
        "scholarshipLimit must be ${BalanceConstants.SCHOLARSHIP_LIMIT}",
      )
    }
    if (ruleset.calendar.rosterCap != BalanceConstants.SCHOLARSHIP_LIMIT) {
      return BalanceValidationError.InvalidCalendar(
        "rosterCap must be ${BalanceConstants.SCHOLARSHIP_LIMIT}",
      )
    }
    if (ruleset.injuries.bank.size < BalanceConstants.REQUIRED_INJURY_BANK_SIZE) {
      return BalanceValidationError.InvalidInjuryBank(ruleset.injuries.bank.size)
    }
    validateJsonSection(ruleset.discipline, "discipline")?.let { return it }
    validateJsonSection(ruleset.recruiting, "recruiting")?.let { return it }
    validateJsonSection(ruleset.nil, "nil")?.let { return it }
    validateJsonSection(ruleset.player.loyalty, "player.loyalty")?.let { return it }
    validateNilAskCap(ruleset.nil)?.let { return it }
    validateNetWeights(ruleset.selection)?.let { return it }
  return null
  }

  private fun validateJsonSection(element: kotlinx.serialization.json.JsonElement, name: String): BalanceValidationError? {
    val obj = element as? JsonObject ?: return BalanceValidationError.MissingSection(name)
    if (obj.isEmpty()) return BalanceValidationError.MissingSection(name)
    return null
  }

  private fun validateNilAskCap(nil: kotlinx.serialization.json.JsonElement): BalanceValidationError? {
    val obj = nil.jsonObject
    val raw = obj["maxOfferMultiplierOfAsk"] ?: return BalanceValidationError.MissingSection("nil.maxOfferMultiplierOfAsk")
    val value = (raw as? JsonPrimitive)?.double
      ?: return BalanceValidationError.MalformedJson("nil.maxOfferMultiplierOfAsk must be a number")
    if (value.toInt() != REQUIRED_NIL_ASK_MULTIPLIER) {
      return BalanceValidationError.MalformedJson(
        "nil.maxOfferMultiplierOfAsk must be $REQUIRED_NIL_ASK_MULTIPLIER (got $value)",
      )
    }
    return null
  }

  private fun validateNetWeights(selection: kotlinx.serialization.json.JsonElement?): BalanceValidationError? {
    val sel = selection?.jsonObject ?: return BalanceValidationError.MissingSection("selection")
    val weights = sel["netWeights"]?.jsonObject ?: return BalanceValidationError.MissingSection("selection.netWeights")
    if (weights.isEmpty()) return BalanceValidationError.MissingSection("selection.netWeights")
    val sum = weights.values.sumOf { entry ->
      (entry as? JsonPrimitive)?.double
        ?: return BalanceValidationError.MalformedJson("selection.netWeights values must be numbers")
    }
    if (kotlin.math.abs(sum - 1.0) > NET_WEIGHT_SUM_TOLERANCE) {
      return BalanceValidationError.InvalidSelectionWeights(sum)
    }
    return null
  }
}

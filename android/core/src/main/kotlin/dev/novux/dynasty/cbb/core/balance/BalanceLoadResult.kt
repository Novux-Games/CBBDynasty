package dev.novux.dynasty.cbb.core.balance

sealed class BalanceLoadResult {
    data class Success(val ruleset: BalanceRuleset) : BalanceLoadResult()
    data class Rejected(val error: BalanceValidationError) : BalanceLoadResult()
}

sealed class BalanceValidationError {
    data class MalformedJson(val message: String) : BalanceValidationError()
    data class RulesetIdMismatch(val expected: String, val actual: String) : BalanceValidationError()
    data class SchemaVersionMismatch(val expected: Int, val actual: Int) : BalanceValidationError()
    data class InvalidCalendar(val message: String) : BalanceValidationError()
    data class MissingSection(val section: String) : BalanceValidationError()
    data class InvalidInjuryBank(val count: Int) : BalanceValidationError()
    data class InvalidSelectionWeights(val sum: Double) : BalanceValidationError()
    data class UnknownRulesetId(val id: String) : BalanceValidationError()

    override fun toString(): String = when (this) {
        is MalformedJson -> "Malformed balance JSON: $message"
        is RulesetIdMismatch -> "balanceRulesetId mismatch: expected $expected, got $actual"
        is SchemaVersionMismatch -> "schemaVersion mismatch: expected $expected, got $actual"
        is InvalidCalendar -> "Invalid calendar: $message"
        is MissingSection -> "Missing balance section: $section"
        is InvalidInjuryBank -> "Injury bank must have >= 40 entries (found $count)"
        is InvalidSelectionWeights -> "NET weights must sum to ~1.0 (got $sum)"
        is UnknownRulesetId -> "Unknown balanceRulesetId: $id"
    }
}

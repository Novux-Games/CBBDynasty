package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Verifies balance bundle exposes `coach.jobSecurityWeights` at runtime load
 * ([MEGA-COACH-048], [ADR-002](../docs/ADR-002-balance-ruleset-bundle.md)).
 */
object BalanceCoachJobSecurityWeightsPolicy {
    const val MEGA_ID: String = "MEGA-COACH-048"
    const val JSON_KEY: String = "jobSecurityWeights"

    fun weightsPresent(ruleset: BalanceRuleset): Boolean {
        val coach = ruleset.coach as? JsonObject ?: return false
        return coach.containsKey(JSON_KEY)
    }
}
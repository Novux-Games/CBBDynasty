package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject

data class BalanceNilRules(
    val maxOfferMultiplierOfAsk: Int = 3,
    val interestNilRatioExponent: Double = 1.0,
)

object BalanceNilParser {
    fun parse(nil: JsonElement): BalanceNilRules {
        val obj = nil.jsonObject
        val multiplier = (obj["maxOfferMultiplierOfAsk"] as? JsonPrimitive)?.content?.toIntOrNull() ?: 3
        val exponent = obj["interestNilRatioExponent"]?.let { el ->
            (el as? JsonPrimitive)?.double
        } ?: 1.0
        return BalanceNilRules(
            maxOfferMultiplierOfAsk = multiplier,
            interestNilRatioExponent = exponent,
        )
    }
}

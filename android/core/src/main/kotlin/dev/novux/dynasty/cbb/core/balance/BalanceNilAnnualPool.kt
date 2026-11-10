package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

data class BalanceNilBlueBloodBand(
    val prestigeMin: Int = 90,
    val prestigeMax: Int = 99,
    val annualPoolMinUsd: Long = 9_700_000L,
    val annualPoolMaxUsd: Long = 13_200_000L,
)

data class BalanceNilConferenceTier(
    val tierId: String,
    val conferenceIds: Set<String>,
    val annualPoolMinUsd: Long,
    val annualPoolMaxUsd: Long,
    val blueBloodEligible: Boolean,
)

data class BalanceNilAnnualPoolTables(
    val tiers: List<BalanceNilConferenceTier>,
    val blueBlood: BalanceNilBlueBloodBand,
)

object BalanceNilAnnualPoolParser {
    fun parse(nil: JsonElement): BalanceNilAnnualPoolTables {
        val obj = nil.jsonObject
        val blue = obj["blueBloodPoolBand"]?.jsonObject ?: JsonObject(emptyMap())
        val blueBlood = BalanceNilBlueBloodBand(
            prestigeMin = blue.int("prestigeMin", 90),
            prestigeMax = blue.int("prestigeMax", 99),
            annualPoolMinUsd = blue.long("annualPoolMinUsd", 9_700_000L),
            annualPoolMaxUsd = blue.long("annualPoolMaxUsd", 13_200_000L),
        )
        val tiersJson = obj["annualPoolByConferenceTier"] as? JsonArray ?: JsonArray(emptyList())
        val tiers = tiersJson.mapNotNull { el ->
            val tier = el.jsonObject
            val confIds = tier["conferenceIds"]?.jsonArray
                ?.mapNotNull { id -> id.jsonPrimitive.content.takeIf { it.isNotBlank() } }
                ?.toSet()
                ?: emptySet()
            if (confIds.isEmpty()) return@mapNotNull null
            BalanceNilConferenceTier(
                tierId = tier["tierId"]?.jsonPrimitive?.content ?: "unknown",
                conferenceIds = confIds,
                annualPoolMinUsd = tier.long("annualPoolMinUsd", 0L),
                annualPoolMaxUsd = tier.long("annualPoolMaxUsd", 0L),
                blueBloodEligible = tier["blueBloodEligible"]?.jsonPrimitive?.content?.toBooleanStrictOrNull()
                    ?: false,
            )
        }
        return BalanceNilAnnualPoolTables(tiers, blueBlood)
    }

    private fun JsonObject.int(key: String, default: Int): Int =
        (this[key] as? JsonPrimitive)?.content?.toIntOrNull() ?: default

    private fun JsonObject.long(key: String, default: Long): Long =
        (this[key] as? JsonPrimitive)?.content?.toLongOrNull() ?: default
}

package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BalanceLoyaltyTuning(
    val declareWeightLoyalty: Double = 0.008,
    val promiseKeptBonus: Double = 0.12,
    val promiseBrokenPenalty: Double = 0.25,
    val minutesToleranceMpg: Int = 3,
    val stayPledgeDefaultTopN: Int = 14,
    val loyaltySeedMin: Int = 25,
    val loyaltySeedMax: Int = 75,
    val promiseKeptLoyaltyBump: Int = 5,
    val promiseBrokenLoyaltyHit: Int = 15,
    val portalBrokenPromiseWeight: Double = 0.35,
)

fun BalanceRuleset.loyaltyTuning(): BalanceLoyaltyTuning =
    BalanceLoyaltyTuningCodec.decode(player.loyalty)

object BalanceLoyaltyTuningCodec {
    private val json = Json { ignoreUnknownKeys = true }

    fun decode(element: JsonElement): BalanceLoyaltyTuning =
        json.decodeFromJsonElement(element)
}

package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Serializable
data class BalanceRecruitingRecommended(
    val listCap: Int = 50,
    val rosterNeedBonus: Int = 8,
    val bands: BalanceSignLikelihoodBands = BalanceSignLikelihoodBands(),
    val prestigeFloorByStars: Map<String, Int> = mapOf("5" to 75, "4" to 52, "3" to 30, "2" to 0),
    val prestigeGapPenaltyPerPoint: Double = 0.9,
    val eliteNationalRankHideThrough: Int = 5,
    val eliteShowMinAdjustedScore: Int = 48,
    val pipelineProximityScore: Double = 0.65,
    val inStateProximityScore: Double = 1.0,
    val outOfRegionProximityScore: Double = 0.22,
)

@Serializable
data class BalanceSignLikelihoodBands(
    val likelyMin: Int = 72,
    val possibleMin: Int = 55,
    val reachMin: Int = 38,
)

object BalanceRecruitingRecommendedParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(recruiting: JsonElement): BalanceRecruitingRecommended {
        val obj = recruiting as? JsonObject ?: return BalanceRecruitingRecommended()
        val recommended = obj["recommended"] ?: return BalanceRecruitingRecommended()
        return json.decodeFromJsonElement(BalanceRecruitingRecommended.serializer(), recommended)
    }

}

package dev.novux.dynasty.cbb.core.balance

import dev.novux.dynasty.cbb.core.recruiting.RecruitingInterestFactor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Serializable
data class BalanceRecruitInterest(
    val priorityMultiplier: Double,
    val baseWeights: Map<String, Double>,
)

object BalanceRecruitInterestParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(recruiting: JsonElement): BalanceRecruitInterest? {
        val interest = recruiting.jsonObject["interest"] ?: return null
        return json.decodeFromJsonElement(BalanceRecruitInterest.serializer(), interest)
    }

    fun toFactorWeights(parsed: BalanceRecruitInterest): Map<RecruitingInterestFactor, Double> =
        parsed.baseWeights.mapNotNull { (key, value) ->
            val factor = when (key) {
                "nil" -> RecruitingInterestFactor.Nil
                "playingTimeFit" -> RecruitingInterestFactor.PlayingTimeFit
                "prestigeWinning" -> RecruitingInterestFactor.PrestigeWinning
                "coachRelationship" -> RecruitingInterestFactor.CoachRelationship
                "proximity" -> RecruitingInterestFactor.Proximity
                "campusLife" -> RecruitingInterestFactor.CampusLife
                else -> null
            }
            factor?.let { it to value }
        }.toMap()
}
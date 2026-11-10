package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Serializable
data class BalanceRecruitingEconomy(
    val weeklyCarryoverFraction: Double,
    val signingCrunchMultiplier: Double,
    val signingCrunchOffseasonWeeks: List<Int>,
    val prestigeTiers: List<BalanceRecruitingPrestigeTier>,
)

@Serializable
data class BalanceRecruitingPrestigeTier(
    val id: String,
    val label: String,
    val prestigeMin: Int,
    val prestigeMax: Int,
    val weeklyPointsMin: Int,
    val weeklyPointsMax: Int,
    val realisticCapacity: String,
)

object BalanceRecruitingParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(recruiting: JsonElement): BalanceRecruitingEconomy =
        json.decodeFromJsonElement(BalanceRecruitingEconomy.serializer(), recruiting)
}

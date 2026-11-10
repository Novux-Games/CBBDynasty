package dev.novux.dynasty.cbb.core.history

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class BalanceWinContributionTuning(
    val minuteBlend: Double = 0.6,
    val productionBlend: Double = 0.4,
    val productionIdxClampMin: Double = 0.25,
    val productionIdxClampMax: Double = 1.75,
    val ptsWeight: Double = 1.0,
    val astWeight: Double = 1.5,
    val rebWeight: Double = 1.0,
    val toWeight: Double = -1.0,
)

@Serializable
data class BalanceGoatWeights(
    val winShares: Double = 10.0,
    val nationalTitles: Double = 25.0,
    val nationalPoty: Double = 40.0,
    val allAmericaPoints: Double = 8.0,
    val confPoty: Double = 3.0,
    val peakOvrBonus: Double = 1.5,
    val peakOvrThreshold: Int = 75,
)

data class BalanceHistoryTuning(
    val winContribution: BalanceWinContributionTuning,
    val goatWeights: BalanceGoatWeights,
    val nationalHofMinGoatScore: Double = 85.0,
)

object BalanceHistoryParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(history: JsonElement?): BalanceHistoryTuning {
        val root = history?.jsonObject ?: return defaults()
        val wc = root["winContribution"]?.jsonObject
        val win = if (wc != null) {
            val w = wc["productionWeights"]?.jsonObject
            BalanceWinContributionTuning(
                minuteBlend = wc["minuteBlend"]?.jsonPrimitive?.double ?: 0.6,
                productionBlend = wc["productionBlend"]?.jsonPrimitive?.double ?: 0.4,
                productionIdxClampMin = wc["productionIdxClampMin"]?.jsonPrimitive?.double ?: 0.25,
                productionIdxClampMax = wc["productionIdxClampMax"]?.jsonPrimitive?.double ?: 1.75,
                ptsWeight = w?.get("pts")?.jsonPrimitive?.double ?: 1.0,
                astWeight = w?.get("ast")?.jsonPrimitive?.double ?: 1.5,
                rebWeight = w?.get("reb")?.jsonPrimitive?.double ?: 1.0,
                toWeight = w?.get("to")?.jsonPrimitive?.double ?: -1.0,
            )
        } else {
            BalanceWinContributionTuning()
        }
        val goat = root["goatWeights"]?.let {
            json.decodeFromJsonElement(BalanceGoatWeights.serializer(), it)
        } ?: BalanceGoatWeights()
        val hof = root["nationalHofMinGoatScore"]?.jsonPrimitive?.double ?: 85.0
        return BalanceHistoryTuning(win, goat, hof)
    }

    fun defaults() = BalanceHistoryTuning(BalanceWinContributionTuning(), BalanceGoatWeights(), 85.0)
}

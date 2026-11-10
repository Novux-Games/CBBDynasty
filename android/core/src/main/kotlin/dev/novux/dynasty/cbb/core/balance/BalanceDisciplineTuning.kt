package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BalanceDisciplineOccurrence(
    val baseChancePerTeamWeek: Double = 0.06,
    val maxPerSeason: Int = 3,
    val academicWindowWeeks: List<Int> = listOf(18, 19, 20, 21, 22),
    val academicOffWindowChanceMultiplier: Double = 0.05,
)

@Serializable
data class BalanceDisciplineAcademic(
    val gpaRequired: Double = 2.0,
    val weeklyGpaGain: Double = 0.08,
    val auditBaseChance: Double = 0.35,
)

@Serializable
data class BalanceDisciplineConferenceDraw(
    val cleared: Double = 0.25,
    val shortSuspensionGamesMin: Int = 1,
    val shortSuspensionGamesMax: Int = 3,
    val shortSuspensionWeight: Double = 0.45,
    val longSuspensionGamesMin: Int = 8,
    val longSuspensionGamesMax: Int = 16,
    val longSuspensionWeight: Double = 0.2,
    val dismissedWeight: Double = 0.1,
)

@Serializable
data class BalanceConductTemplate(
    val id: String,
    val label: String,
    val weight: Double,
)

@Serializable
data class BalanceDisciplineTuning(
    val occurrence: BalanceDisciplineOccurrence = BalanceDisciplineOccurrence(),
    val academic: BalanceDisciplineAcademic = BalanceDisciplineAcademic(),
    val conferenceDraw: BalanceDisciplineConferenceDraw = BalanceDisciplineConferenceDraw(),
    val conductTemplates: List<BalanceConductTemplate> = emptyList(),
)

fun BalanceRuleset.disciplineTuning(): BalanceDisciplineTuning =
    BalanceDisciplineTuningCodec.decode(discipline)

object BalanceDisciplineTuningCodec {
    private val json = Json { ignoreUnknownKeys = true }

    fun decode(element: JsonElement): BalanceDisciplineTuning =
        json.decodeFromJsonElement(element)
}

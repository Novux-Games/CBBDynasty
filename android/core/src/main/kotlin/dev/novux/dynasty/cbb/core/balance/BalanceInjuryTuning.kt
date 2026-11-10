package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BalanceInjuryOccurrence(
    val baseChancePerPlayerGame: Double = 0.018,
    val maxInjuriesPerTeamPerGame: Int = 2,
    val exhibitionMultiplier: Double = 0.45,
    val minutesWeightExponent: Double = 1.15,
    val fatigueWeightExponent: Double = 1.1,
    val medicalStaffGradeBaseline: Int = 70,
    val stackPolicy: String = "replace",
    val headlineOverallMin: Int = 78,
)

@Serializable
data class BalanceMedicalRedshirtRules(
    val maxGamesPlayed: Int = 15,
    val maxScheduleRsGameIndex: Int = 20,
    val blockedFromRegularSeasonWeek: Int = 21,
    val qualifyingSeverities: List<String> = listOf("moderate", "major"),
)

@Serializable
data class BalanceInjuryTuning(
    val occurrence: BalanceInjuryOccurrence = BalanceInjuryOccurrence(),
    val medicalRedshirt: BalanceMedicalRedshirtRules = BalanceMedicalRedshirtRules(),
)

fun BalanceRuleset.injuryTuning(): BalanceInjuryTuning =
    BalanceInjuryTuningCodec.decode(injuries)

object BalanceInjuryTuningCodec {
    private val json = Json { ignoreUnknownKeys = true }

    fun decode(injuries: BalanceInjuries): BalanceInjuryTuning {
        val occurrence = json.decodeFromJsonElement<BalanceInjuryOccurrence>(injuries.occurrence)
        val medical = json.decodeFromJsonElement<BalanceMedicalRedshirtRules>(injuries.medicalRedshirt)
        return BalanceInjuryTuning(occurrence = occurrence, medicalRedshirt = medical)
    }
}

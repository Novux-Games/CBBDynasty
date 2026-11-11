package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BalanceRuleset(
    val balanceRulesetId: String,
    val schemaVersion: Int,
    val labelMembershipYear: String,
    val selection: JsonElement? = null,
    val coach: JsonElement? = null,
    val calendar: BalanceCalendar,
    val discipline: JsonElement,
    val chemistry: BalanceChemistry,
    val fatigue: BalanceFatigue,
    val injuries: BalanceInjuries,
    val recruiting: JsonElement,
    val nil: JsonElement,
    val player: BalancePlayer,
    val awards: JsonElement? = null,
    val history: JsonElement? = null,
)

@Serializable
data class BalanceCalendar(
    val scholarshipLimit: Int,
    val rosterCap: Int,
)

@Serializable
data class BalanceChemistry(
    val startValue: Int,
    val winStreakBonusPerWeek: Int,
    val lossStreakPenaltyPerWeek: Int,
    val clutchBonusAbove80: Double,
    val clutchPenaltyBelow50: Double,
)

@Serializable
data class BalanceFatigue(
    val baseDrainPerMinute: Double,
    val recoveryByeWeek: Int,
    val recoveryGameWeek: Int,
    val starterBlockThreshold: Int,
    val rotationPullThreshold: Int,
) {
  companion object {
        val DEFAULT = BalanceFatigue(
            baseDrainPerMinute = 0.35,
            recoveryByeWeek = 25,
            recoveryGameWeek = 15,
            starterBlockThreshold = 20,
            rotationPullThreshold = 35,
        )
    }
}

@Serializable
data class BalanceInjuries(
    val occurrence: JsonElement,
    val medicalRedshirt: JsonElement,
    val bank: List<BalanceInjuryBankEntry>,
)

@Serializable
data class BalanceInjuryBankEntry(
    val id: String,
    val label: String,
    val bodyPart: String,
    val severity: String,
    val durationWeeksMin: Int,
    val durationWeeksMax: Int,
    val weight: Double,
)

@Serializable
data class BalancePlayer(
    val loyalty: JsonElement,
)

object BalanceConstants {
    const val REQUIRED_INJURY_BANK_SIZE = 40
    const val SCHOLARSHIP_LIMIT = 15
}

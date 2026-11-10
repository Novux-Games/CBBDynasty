package dev.novux.dynasty.cbb.database.roster

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dynasty_roster_player",
    indices = [Index(value = ["dynastyId"])],
)
data class DynastyRosterPlayerEntity(
    @PrimaryKey val playerId: String,
    val dynastyId: String,
    val positionWire: String,
    val classYearWire: String,
    val isStarter: Boolean,
    val depthOrder: Int,
    val inTransferPortal: Boolean = false,
    val declaredForDraft: Boolean = false,
    val draftStockPoints: Int = 0,
    val proDayCompleted: Boolean = false,
    val displayName: String = "",
    val hometownCity: String = "",
    val hometownState: String = "",
    val heightInches: Int = 76,
    val weightLbs: Int = 200,
    val nilUsdPerYear: Long = 0,
    val ovr: Int = 60,
    val offRating: Int = 60,
    val defRating: Int = 60,
    val eligibilityYearsUsed: Int = 0,
    val eligibilityYearsRemaining: Int = 4,
    val medicalRedshirtSeason: Boolean = false,
    val injuryActive: Boolean = false,
    val injuryId: String? = null,
    val injuryWeeksRemaining: Int = 0,
    val injuryLabel: String? = null,
)

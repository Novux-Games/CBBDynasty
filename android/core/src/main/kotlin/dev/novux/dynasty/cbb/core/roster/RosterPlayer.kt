package dev.novux.dynasty.cbb.core.roster

import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.player.PlayerRatings
import dev.novux.dynasty.cbb.core.player.RosterRatingSpread

data class RosterPlayer(
    val playerId: String,
    val dynastyId: String,
    val position: PlayerPosition,
    val classYear: PlayerClassYear,
    val isStarter: Boolean,
    val depthOrder: Int,
    val inTransferPortal: Boolean,
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
    val off: Int = 60,
    val def: Int = 60,
    val eligibilityYearsUsed: Int = 0,
    val eligibilityYearsRemaining: Int = 4,
    val medicalRedshirtSeason: Boolean = false,
    val injuryActive: Boolean = false,
    val injuryId: String? = null,
    val injuryWeeksRemaining: Int = 0,
    val injuryLabel: String? = null,
    val injurySeverityWire: String? = null,
    val injuryOccurredAtRsWeek: Int = 0,
    val injuryOccurredAtRsGameIndex: Int = 0,
    val rsGamesPlayedAtInjury: Int = 0,
    val rsGamesPlayedSeason: Int = 0,
    val injuryWasStarterSlotWire: String? = null,
    val injuryPriorRotationBand: Int? = null,
    val starterSlotWire: String? = null,
    val archetypeWire: String = "",
    val loyaltyRating: Int = 50,
    val loyaltyPersonalityWire: String = "",
    val promiseTypeWire: String? = null,
    val promiseStatusWire: String = "",
    val promiseLockedSeasonIndex: Int? = null,
    val promiseStayTopN: Int? = null,
) {
    fun displayLabel(): String =
        if (displayName.isNotBlank()) displayName else "${position.label} · ${classYear.label}"

    fun toRatings(): PlayerRatings = RosterRatingSpread.fromHeadline(ovr, off, def, position)

    fun toDynastyPlayer(): DynastyPlayer = DynastyPlayer.fromRosterPlayer(this)
}

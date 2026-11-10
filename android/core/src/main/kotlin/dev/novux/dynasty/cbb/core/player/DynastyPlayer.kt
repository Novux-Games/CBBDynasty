package dev.novux.dynasty.cbb.core.player

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Full player record for cards and simulation — [planning/player-cards.md]. */
data class DynastyPlayer(
    val playerId: String,
    val dynastyId: String,
    val identity: PlayerIdentity,
    val position: PlayerPosition,
    val classYear: PlayerClassYear,
    val ratings: PlayerRatings,
    val eligibility: PlayerEligibility,
    val injury: PlayerInjuryState,
    val role: PlayerRosterRole,
) {
    fun toRosterPlayer(): RosterPlayer = RosterPlayer(
        playerId = playerId,
        dynastyId = dynastyId,
        position = position,
        classYear = classYear,
        isStarter = role.isStarter,
        depthOrder = role.depthOrder,
        inTransferPortal = role.inTransferPortal,
        declaredForDraft = role.declaredForDraft,
        draftStockPoints = role.draftStockPoints,
        proDayCompleted = role.proDayCompleted,
        displayName = identity.displayName,
        hometownCity = identity.hometownCity,
        hometownState = identity.hometownState,
        heightInches = identity.heightInches,
        weightLbs = identity.weightLbs,
        nilUsdPerYear = identity.nilUsdPerYear,
        ovr = ratings.ovr,
        off = ratings.off,
        def = ratings.def,
        eligibilityYearsUsed = eligibility.yearsUsed,
        eligibilityYearsRemaining = eligibility.yearsRemaining,
        medicalRedshirtSeason = eligibility.medicalRedshirtSeason,
        injuryActive = injury.injuryActive,
        injuryId = injury.injuryId,
        injuryWeeksRemaining = injury.weeksRemaining,
        injuryLabel = injury.label,
    )

    companion object {
        fun fromRosterPlayer(player: RosterPlayer): DynastyPlayer = DynastyPlayer(
            playerId = player.playerId,
            dynastyId = player.dynastyId,
            identity = PlayerIdentity(
                displayName = player.displayName,
                hometownCity = player.hometownCity,
                hometownState = player.hometownState,
                heightInches = player.heightInches,
                weightLbs = player.weightLbs,
                nilUsdPerYear = player.nilUsdPerYear,
            ),
            position = player.position,
            classYear = player.classYear,
            ratings = PlayerRatings.headline(player.ovr, player.off, player.def),
            eligibility = PlayerEligibility(
                yearsUsed = player.eligibilityYearsUsed,
                yearsRemaining = player.eligibilityYearsRemaining,
                medicalRedshirtSeason = player.medicalRedshirtSeason,
            ),
            injury = PlayerInjuryState(
                injuryActive = player.injuryActive,
                injuryId = player.injuryId,
                weeksRemaining = player.injuryWeeksRemaining,
                label = player.injuryLabel,
            ),
            role = PlayerRosterRole(
                isStarter = player.isStarter,
                depthOrder = player.depthOrder,
                inTransferPortal = player.inTransferPortal,
                declaredForDraft = player.declaredForDraft,
                draftStockPoints = player.draftStockPoints,
                proDayCompleted = player.proDayCompleted,
            ),
        )
    }
}

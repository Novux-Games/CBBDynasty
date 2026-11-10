package dev.novux.dynasty.cbb.core.roster

import dev.novux.dynasty.cbb.core.player.PlayerEligibility

/**
 * Generated roster row at dynasty create — enriched to [dev.novux.dynasty.cbb.core.player.DynastyPlayer].
 */
data class RosterPlayerSeed(
    val playerId: String,
    val dynastyId: String,
    val position: PlayerPosition,
    val classYear: PlayerClassYear,
    val isStarter: Boolean,
    val depthOrder: Int,
    val displayName: String = "",
    val hometownCity: String = "",
    val hometownState: String = "",
    val heightInches: Int = 76,
    val weightLbs: Int = 200,
    val ovr: Int = 60,
    val off: Int = 60,
    val def: Int = 60,
    val eligibility: PlayerEligibility = PlayerEligibility.forClassYear(classYear),
)

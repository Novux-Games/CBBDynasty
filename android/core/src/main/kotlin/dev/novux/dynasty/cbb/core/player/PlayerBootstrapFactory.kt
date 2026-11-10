package dev.novux.dynasty.cbb.core.player

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayerSeed

object PlayerBootstrapFactory {
    fun enrichSeed(seed: RosterPlayerSeed): RosterPlayerSeed {
        val base = positionBaseRating(seed.position, seed.isStarter)
        val identity = placeholderIdentity(seed.playerId, seed.depthOrder)
        return seed.copy(
            displayName = identity.first,
            hometownCity = identity.second,
            hometownState = identity.third,
            heightInches = defaultHeight(seed.position),
            weightLbs = defaultWeight(seed.position),
            ovr = base,
            off = (base + positionOffBias(seed.position)).coerceIn(0, 99),
            def = (base + positionDefBias(seed.position)).coerceIn(0, 99),
            eligibility = PlayerEligibility.forClassYear(seed.classYear),
        )
    }

    fun fromSeed(seed: RosterPlayerSeed): DynastyPlayer {
        val enriched = if (seed.displayName.isBlank()) enrichSeed(seed) else seed
        return DynastyPlayer(
            playerId = enriched.playerId,
            dynastyId = enriched.dynastyId,
            identity = PlayerIdentity(
                displayName = enriched.displayName,
                hometownCity = enriched.hometownCity,
                hometownState = enriched.hometownState,
                heightInches = enriched.heightInches,
                weightLbs = enriched.weightLbs,
            ),
            position = enriched.position,
            classYear = enriched.classYear,
            ratings = PlayerRatings.headline(enriched.ovr, enriched.off, enriched.def),
            eligibility = enriched.eligibility,
            injury = PlayerInjuryState.Healthy,
            role = PlayerRosterRole(
                isStarter = enriched.isStarter,
                depthOrder = enriched.depthOrder,
                inTransferPortal = false,
                declaredForDraft = false,
                draftStockPoints = 0,
                proDayCompleted = false,
            ),
        )
    }

    private fun positionBaseRating(position: PlayerPosition, starter: Boolean): Int {
        val starterBump = if (starter) 8 else 0
        val pos = when (position) {
            PlayerPosition.PointGuard -> 68
            PlayerPosition.ShootingGuard -> 66
            PlayerPosition.SmallForward -> 65
            PlayerPosition.PowerForward -> 64
            PlayerPosition.Center -> 63
        }
        return (pos + starterBump).coerceIn(45, 92)
    }

    private fun positionOffBias(position: PlayerPosition): Int = when (position) {
        PlayerPosition.PointGuard, PlayerPosition.ShootingGuard -> 4
        PlayerPosition.Center -> -2
        else -> 0
    }

    private fun positionDefBias(position: PlayerPosition): Int = when (position) {
        PlayerPosition.Center, PlayerPosition.PowerForward -> 3
        PlayerPosition.ShootingGuard -> -2
        else -> 0
    }

    private fun defaultHeight(position: PlayerPosition): Int = when (position) {
        PlayerPosition.PointGuard -> 74
        PlayerPosition.ShootingGuard -> 76
        PlayerPosition.SmallForward -> 79
        PlayerPosition.PowerForward -> 80
        PlayerPosition.Center -> 82
    }

    private fun defaultWeight(position: PlayerPosition): Int = when (position) {
        PlayerPosition.PointGuard -> 185
        PlayerPosition.ShootingGuard -> 195
        PlayerPosition.SmallForward -> 210
        PlayerPosition.PowerForward -> 225
        PlayerPosition.Center -> 240
    }

    private fun placeholderIdentity(playerId: String, depth: Int): Triple<String, String, String> {
        val hash = playerId.hashCode().absoluteValue
        val first = FIRST_NAMES[hash % FIRST_NAMES.size]
        val last = LAST_NAMES[(hash / FIRST_NAMES.size) % LAST_NAMES.size]
        val city = CITIES[(hash + depth) % CITIES.size]
        val state = STATES[(hash + depth * 3) % STATES.size]
        return Triple("$first $last", city, state)
    }

    private val Int.absoluteValue: Int get() = if (this < 0) -this else this

    private val FIRST_NAMES = listOf("Jordan", "Miles", "Eli", "Cameron", "Devin", "Tyler", "Noah", "Marcus")
    private val LAST_NAMES = listOf("Hayes", "Brooks", "Carter", "Dixon", "Nguyen", "Walsh", "Okafor", "Silva")
    private val CITIES = listOf("Columbus", "Louisville", "Dallas", "Atlanta", "Indianapolis", "Omaha")
    private val STATES = listOf("OH", "KY", "TX", "GA", "IN", "NE")
}

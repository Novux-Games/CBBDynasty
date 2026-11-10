package dev.novux.dynasty.cbb.core.player.loyalty

import dev.novux.dynasty.cbb.core.balance.BalanceLoyaltyTuning

object LoyaltyBootstrap {
    fun seedLoyalty(
        playerId: String,
        archetypeWire: String,
        tuning: BalanceLoyaltyTuning = BalanceLoyaltyTuning(),
    ): Pair<LoyaltyRating, LoyaltyPersonalityTag> {
        val personality = LoyaltyPersonalityTag.pickForSeed(playerId, archetypeWire)
        val span = (tuning.loyaltySeedMax - tuning.loyaltySeedMin).coerceAtLeast(1)
        val hash = (playerId.hashCode() * 31 + archetypeWire.hashCode()).absoluteValue
        var base = tuning.loyaltySeedMin + (hash % (span + 1))
        base = when (personality) {
            LoyaltyPersonalityTag.ProgramGuy, LoyaltyPersonalityTag.HometownKid -> base + 8
            LoyaltyPersonalityTag.OneAndDoneLean -> base - 10
            LoyaltyPersonalityTag.Mercurial -> base - 4
        }
        return LoyaltyRating(base.coerceIn(0, 99)) to personality
    }

    private val Int.absoluteValue: Int get() = if (this < 0) -this else this
}

object TenurePromiseBootstrap {
    /** Default promise locked at dynasty sign for scholarship players. */
    fun atHardCommit(
        isStarter: Boolean,
        lockedAtSeasonIndex: Int = 0,
        stayTopN: Int = CoachPromise.DEFAULT_STAY_TOP_N,
    ): CoachPromise {
        val type = if (isStarter) {
            CoachPromiseType.MinutesStarter
        } else {
            CoachPromiseType.MinutesRotation
        }
        return CoachPromise(
            type = type,
            stayTopN = stayTopN,
            lockedAtSeasonIndex = lockedAtSeasonIndex,
            status = PromiseStatus.Pending,
        )
    }
}

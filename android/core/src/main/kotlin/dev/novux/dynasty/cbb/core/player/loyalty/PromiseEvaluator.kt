package dev.novux.dynasty.cbb.core.player.loyalty

import dev.novux.dynasty.cbb.core.balance.BalanceLoyaltyTuning
import dev.novux.dynasty.cbb.core.player.DynastyPlayer

/**
 * Evaluates kept / broken against roster reality until season mpg is sim-backed.
 */
object PromiseEvaluator {
    private const val STARTER_MPG_TARGET = 26.0
    private const val ROTATION_MPG_LOW = 14.0
    private const val ROTATION_MPG_HIGH = 25.0

    fun evaluateSeasonWindow(
        player: DynastyPlayer,
        seasonMinutesPerGame: Double?,
        tuning: BalanceLoyaltyTuning = BalanceLoyaltyTuning(),
    ): PromiseStatus {
        val promise = player.loyalty.promise ?: return PromiseStatus.Pending
        if (promise.type == CoachPromiseType.RoleRedshirt ||
            promise.type == CoachPromiseType.StayPledgeLottery ||
            promise.type == CoachPromiseType.StayPledgeTopN
        ) {
            return PromiseStatus.Deferred
        }

        val mpg = seasonMinutesPerGame
        val tolerance = tuning.minutesToleranceMpg.toDouble()

        return when (promise.type) {
            CoachPromiseType.MinutesStarter -> {
                if (mpg != null) {
                    when {
                        mpg >= STARTER_MPG_TARGET - tolerance -> PromiseStatus.Kept
                        mpg < STARTER_MPG_TARGET - tolerance - 4 -> PromiseStatus.Broken
                        else -> PromiseStatus.AtRisk
                    }
                } else {
                    if (player.role.isStarter) PromiseStatus.Kept else PromiseStatus.Broken
                }
            }
            CoachPromiseType.MinutesRotation -> {
                if (mpg != null) {
                    when {
                        mpg in (ROTATION_MPG_LOW - tolerance)..(ROTATION_MPG_HIGH + tolerance) -> PromiseStatus.Kept
                        mpg < ROTATION_MPG_LOW - tolerance -> PromiseStatus.Broken
                        else -> PromiseStatus.AtRisk
                    }
                } else {
                    if (!player.role.isStarter && player.role.depthOrder <= 9) PromiseStatus.Kept
                    else PromiseStatus.AtRisk
                }
            }
            CoachPromiseType.MinutesDevelop -> {
                if (mpg != null) {
                    if (mpg <= ROTATION_MPG_LOW) PromiseStatus.Kept else PromiseStatus.AtRisk
                } else {
                    if (player.role.depthOrder > 8) PromiseStatus.Kept else PromiseStatus.AtRisk
                }
            }
            CoachPromiseType.RoleStarter ->
                if (player.role.isStarter) PromiseStatus.Kept else PromiseStatus.Broken
            CoachPromiseType.RoleSixthMan ->
                if (!player.role.isStarter && player.role.depthOrder in 6..9) PromiseStatus.Kept
                else PromiseStatus.AtRisk
            CoachPromiseType.RoleRedshirt,
            CoachPromiseType.StayPledgeLottery,
            CoachPromiseType.StayPledgeTopN,
            -> PromiseStatus.Deferred
        }
    }

    fun applyStatusDelta(
        profile: PlayerLoyaltyProfile,
        newStatus: PromiseStatus,
        tuning: BalanceLoyaltyTuning,
    ): PlayerLoyaltyProfile {
        val promise = profile.promise ?: return profile
        if (promise.status == newStatus) return profile
        val loyalty = when {
            newStatus == PromiseStatus.Kept && promise.status != PromiseStatus.Kept ->
                profile.loyalty.applyDelta(tuning.promiseKeptLoyaltyBump)
            newStatus == PromiseStatus.Broken && promise.status != PromiseStatus.Broken ->
                profile.loyalty.applyDelta(-tuning.promiseBrokenLoyaltyHit)
            else -> profile.loyalty
        }
        return profile.copy(loyalty = loyalty, promise = promise.copy(status = newStatus))
    }
}

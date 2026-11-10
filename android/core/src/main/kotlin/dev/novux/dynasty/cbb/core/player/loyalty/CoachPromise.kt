package dev.novux.dynasty.cbb.core.player.loyalty

/** Exactly one coach promise per program tenure — [planning/player-loyalty-promises.md#coach-promises]. */
data class CoachPromise(
    val type: CoachPromiseType,
    val stayTopN: Int? = null,
    val lockedAtSeasonIndex: Int? = null,
    val status: PromiseStatus = PromiseStatus.Pending,
) {
    fun displayLabel(): String = when (type) {
        CoachPromiseType.MinutesStarter -> "Starter minutes"
        CoachPromiseType.MinutesRotation -> "Rotation minutes"
        CoachPromiseType.MinutesDevelop -> "Development minutes"
        CoachPromiseType.RoleStarter -> "Starter role"
        CoachPromiseType.RoleSixthMan -> "Sixth man role"
        CoachPromiseType.RoleRedshirt -> "Redshirt year"
        CoachPromiseType.StayPledgeLottery -> "Stay unless lottery"
        CoachPromiseType.StayPledgeTopN -> "Stay unless top-${stayTopN ?: DEFAULT_STAY_TOP_N} mock"
    }

    fun statusChipLabel(): String = when (status) {
        PromiseStatus.Pending -> "Pending"
        PromiseStatus.AtRisk -> "At risk"
        PromiseStatus.Kept -> "Kept"
        PromiseStatus.Broken -> "Broken"
        PromiseStatus.Deferred -> "Deferred"
    }

    companion object {
        const val DEFAULT_STAY_TOP_N = 14
    }
}

enum class CoachPromiseType(val wire: String) {
    MinutesStarter("minutes_starter"),
    MinutesRotation("minutes_rotation"),
    MinutesDevelop("minutes_develop"),
    RoleStarter("role_starter"),
    RoleSixthMan("role_sixth_man"),
    RoleRedshirt("role_redshirt"),
    StayPledgeLottery("stay_lottery"),
    StayPledgeTopN("stay_top_n"),
    ;

    companion object {
        fun fromWire(wire: String): CoachPromiseType? = entries.firstOrNull { it.wire == wire }
    }
}

enum class PromiseStatus(val wire: String) {
    Pending("pending"),
    AtRisk("at_risk"),
    Kept("kept"),
    Broken("broken"),
    Deferred("deferred"),
    ;

    companion object {
        fun fromWire(wire: String): PromiseStatus? = entries.firstOrNull { it.wire == wire }
    }
}

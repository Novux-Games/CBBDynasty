package dev.novux.dynasty.cbb.core.sim

/** Sim / strategy batch-35 traceability. */
object SimJumpBallHeldBallPolicy {
    const val MEGA_ID = "MEGA-SIM-096"
    const val RARE_EVENT_RATE = 0.0025

    fun shouldTrigger(random: kotlin.random.Random): Boolean =
        random.nextDouble() < RARE_EVENT_RATE

    fun shouldTriggerDeterministic(simSeed: Long, possessionIndex: Int, offenseTeamId: String): Boolean {
        if (possessionIndex < 4) return false
        val mix = simSeed xor (possessionIndex.toLong() * 31L) xor offenseTeamId.hashCode().toLong()
        val bucket = ((mix ushr 11) and 0xFFFF).toInt()
        return bucket < (RARE_EVENT_RATE * 65535.0).toInt().coerceAtLeast(1)
    }

    fun logTag(): String = "jump-ball"
}

object SimAssistAttributionPolicy {
    const val MEGA_ID = "MEGA-SIM-098"
    val RECORDER: String = PlayerPossessionStatRecorder::class.java.simpleName
}

object SimTechnicalFoulPolicy {
    const val MEGA_ID = "MEGA-SIM-107"
}

object SimHalfOpeningStarterPolicy {
    const val MEGA_ID = "MEGA-SIM-110"
}

object SimClutchPossessionLogPolicy {
    const val MEGA_ID = "MEGA-SIM-116"
    val CLUTCH_POLICY: String = ClutchUsagePolicy::class.java.simpleName
}

object SimLateGameCrowdVenuePolicy {
    const val MEGA_ID = "MEGA-SIM-118"
}

object SimExhibitionPhaseBadgePolicy {
    const val MEGA_ID = "MEGA-SIM-128"
}

object SimPlusMinusOptionalPolicy {
    const val MEGA_ID = "MEGA-SIM-134"
}

object StrategyDefaultStarterResetPolicy {
    const val MEGA_ID = "MEGA-SIM-142"
}

object StrategyTargetMinutesRowPolicy {
    const val MEGA_ID = "MEGA-SIM-144"
}

object SimWinSharesDynastyEosPolicy {
    const val MEGA_ID = "MEGA-SIM-174"
    const val RELATED = "MEGA-SIM-028"
}
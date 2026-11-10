package dev.novux.dynasty.cbb.core.fatigue

import dev.novux.dynasty.cbb.core.balance.BalanceFatigue
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import kotlin.math.pow

object FatigueEngine {
    const val FRESH = 100

    fun fatigueInjuryWeight(stamina: Int, exponent: Double = 1.1): Double {
        if (stamina >= 35) return 1.0
        val clamped = stamina.coerceAtLeast(1)
        return (35.0 / clamped).pow(exponent)
    }

    fun blocksAutoStarter(stamina: Int, tuning: BalanceFatigue): Boolean =
        stamina < tuning.starterBlockThreshold

    fun shouldPullInRotation(stamina: Int, tuning: BalanceFatigue): Boolean =
        stamina < tuning.rotationPullThreshold

    fun applyWeeklyRecovery(
        players: List<RosterPlayer>,
        phase: CalendarPhase,
        teamPlayedGame: Boolean,
        tuning: BalanceFatigue,
    ): List<RosterPlayer> {
        if (phase == CalendarPhase.Offseason) {
            return players.map { it.copy(stamina = FRESH) }
        }
        val gain = if (teamPlayedGame) tuning.recoveryGameWeek else tuning.recoveryByeWeek
        return players.map { player ->
            player.copy(stamina = (player.stamina + gain).coerceAtMost(FRESH))
        }
    }

    /** Estimate drain when box scores are not available yet. */
    fun applyEstimatedGameDrain(
        players: List<RosterPlayer>,
        tuning: BalanceFatigue,
    ): List<RosterPlayer> =
        players.map { player ->
            val minutes = if (player.isStarter) 28.0 else 10.0
            val drain = (tuning.baseDrainPerMinute * minutes).toInt()
            player.copy(stamina = (player.stamina - drain).coerceAtLeast(0))
        }
}

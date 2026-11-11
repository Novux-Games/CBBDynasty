package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.strategy.ResolvedSchemeEffects
import kotlin.random.Random

/**
 * Optional possession sub-phases **drive → kick → shot** before terminal outcome
 * ([planning/simulation-engine.md#why-track-a-shot-clock], MEGA-SIM-089).
 */
object PossessionSubPhaseEngine {
    const val QA_ID: String = "MEGA-SIM-089"

    private const val DRIVE_TO_SHARE = 0.28
    private const val KICK_TO_SHARE = 0.22
    private const val KICK_LOOK_MAKE_BONUS = 0.025

    fun resolve(
        offense: TeamBoxScore,
        offRating: Int,
        defRating: Int,
        effects: ResolvedSchemeEffects,
        random: Random,
    ): PossessionResolveResult {
        val box = offense.copy(possessions = offense.possessions + 1)
        val chain = mutableListOf("drive")
        val baseTo = effects.turnoverChance.coerceIn(0.05, 0.40)
        if (random.nextDouble() < baseTo * DRIVE_TO_SHARE) {
            return turnoverResult(box, chain, TurnoverTerminal.Generic, "TO drive")
        }
        chain += "kick"
        if (random.nextDouble() < baseTo * KICK_TO_SHARE) {
            return turnoverResult(box, chain, TurnoverTerminal.Generic, "TO kick")
        }
        chain += "shot"
        return resolveShotTerminal(box, offRating, defRating, effects, random, chain)
    }

    private fun turnoverResult(
        box: TeamBoxScore,
        chain: List<String>,
        terminal: TurnoverTerminal,
        logHint: String,
    ): PossessionResolveResult =
        PossessionResolveResult(
            box = box.copy(turnovers = box.turnovers + 1),
            change = PossessionChange.DefenseTakesBall,
            turnoverTerminal = terminal,
            subPhaseChain = chain.joinToString("→") + "→$logHint",
        )

    private fun resolveShotTerminal(
        box: TeamBoxScore,
        offRating: Int,
        defRating: Int,
        effects: ResolvedSchemeEffects,
        random: Random,
        chain: List<String>,
    ): PossessionResolveResult {
        val edge = (offRating - defRating).coerceIn(-30, 30)
        val makeBias = (
            0.42 + edge * 0.004 + effects.makeRateAdjust + KICK_LOOK_MAKE_BONUS
        ).coerceIn(0.25, 0.65)
        val three = random.nextDouble() < effects.threePointAttemptRate
        val attempt = box.fieldGoalsAttempted + 1
        val made = random.nextDouble() < makeBias
        var next = box.copy(fieldGoalsAttempted = attempt)
        if (three) {
            next = next.copy(threePointsAttempted = next.threePointsAttempted + 1)
        }
        val chainLabel = chain.joinToString("→")
        if (made) {
            val pts = if (three) 3 else 2
            next = next.copy(
                fieldGoalsMade = next.fieldGoalsMade + 1,
                points = next.points + pts,
                threePointsMade = next.threePointsMade + if (three) 1 else 0,
            )
            return PossessionResolveResult(
                next,
                PossessionChange.DefenseTakesBall,
                subPhaseChain = "$chainLabel | make",
            )
        }
        val reb = PossessionSimEngine.resolveMissRebound(next, random)
        return reb.copy(subPhaseChain = "$chainLabel | miss")
    }
}

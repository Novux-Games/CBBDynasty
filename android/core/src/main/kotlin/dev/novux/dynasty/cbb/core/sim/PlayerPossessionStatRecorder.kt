package dev.novux.dynasty.cbb.core.sim

import kotlin.random.Random

/** Attributes team possession deltas to on-floor players. */
object PlayerPossessionStatRecorder {
    fun record(
        offenseLedger: PlayerBoxScoreLedger,
        defenseLedger: PlayerBoxScoreLedger,
        offenseFloor: List<String>,
        defenseFloor: List<String>,
        usageWeights: Map<String, Int>,
        before: TeamBoxScore,
        after: TeamBoxScore,
        change: PossessionChange,
        turnoverTerminal: TurnoverTerminal?,
        foulStoppage: Boolean,
        blockedShot: Boolean = false,
        random: Random,
    ) {
        if (offenseFloor.isEmpty()) return
        val pts = after.points - before.points
        val fga = after.fieldGoalsAttempted - before.fieldGoalsAttempted
        val fgm = after.fieldGoalsMade - before.fieldGoalsMade
        val threeAtt = after.threePointsAttempted - before.threePointsAttempted
        val threeMade = after.threePointsMade - before.threePointsMade
        val turnovers = after.turnovers - before.turnovers
        val ftAttempts = after.freeThrowsAttempted - before.freeThrowsAttempted
        val ftMade = after.freeThrowsMade - before.freeThrowsMade

        if (turnovers > 0) {
            val handler = pickWeighted(offenseFloor, usageWeights, random)
            offenseLedger.addTurnover(handler)
            if (defenseFloor.isNotEmpty() &&
                turnoverTerminal != TurnoverTerminal.ShotClockViolation &&
                (
                    turnoverTerminal == TurnoverTerminal.Steal ||
                        random.nextDouble() < 0.38
                    )
            ) {
                defenseLedger.addSteal(pickUniform(defenseFloor, random))
            }
        }

        if (fga > 0) {
            val shooter = pickWeighted(offenseFloor, usageWeights, random)
            val three = threeAtt > 0
            offenseLedger.addFieldGoalAttempt(shooter, three)
            if (fgm > 0) {
                offenseLedger.addFieldGoalMake(shooter, pts, three)
                val others = offenseFloor.filter { it != shooter }
                if (others.isNotEmpty() && random.nextDouble() < 0.58) {
                    offenseLedger.addAssist(pickWeighted(others, usageWeights, random))
                }
            } else when {
                blockedShot && defenseFloor.isNotEmpty() -> {
                    defenseLedger.addBlock(pickUniform(defenseFloor, random))
                }
                change == PossessionChange.OffenseRetainsOffensiveRebound ->
                    offenseLedger.addOffensiveRebound(pickWeighted(offenseFloor, usageWeights, random))
                change == PossessionChange.DefenseTakesBall && defenseFloor.isNotEmpty() -> {
                    defenseLedger.addDefensiveRebound(pickUniform(defenseFloor, random))
                }
            }
        }

        if (ftAttempts > 0) {
            val shooter = pickWeighted(offenseFloor, usageWeights, random)
            offenseLedger.addFreeThrows(shooter, ftMade, ftAttempts)
        }
    }

    private const val FREE_THROW_MAKE_RATE = 0.74

    fun sampleFreeThrowChain(attempts: Int, random: Random): Pair<Int, Int> {
        if (attempts <= 0) return 0 to 0
        var makes = 0
        repeat(attempts) {
            if (random.nextDouble() < FREE_THROW_MAKE_RATE) makes++
        }
        return makes to attempts
    }

    fun sampleBonusFreeThrows(random: Random): Pair<Int, Int> =
        sampleFreeThrowChain(attempts = 2, random = random)

    fun shouldShootBonusFreeThrows(
        before: TeamBoxScore,
        after: TeamBoxScore,
        foulStoppage: Boolean,
    ): Boolean =
        foulStoppage &&
            after.fieldGoalsAttempted == before.fieldGoalsAttempted &&
            after.turnovers == before.turnovers &&
            after.points == before.points

    private fun pickWeighted(ids: List<String>, weights: Map<String, Int>, random: Random): String {
        if (ids.size == 1) return ids.first()
        val total = ids.sumOf { weights[it] ?: 15 }
        if (total <= 0) return ids.random(random)
        var roll = random.nextInt(total)
        for (id in ids) {
            roll -= weights[id] ?: 15
            if (roll < 0) return id
        }
        return ids.last()
    }

    private fun pickUniform(ids: List<String>, random: Random): String = ids.random(random)
}

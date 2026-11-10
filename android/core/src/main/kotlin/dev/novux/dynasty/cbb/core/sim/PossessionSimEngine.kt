package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Possession-based regulation sim — [planning/simulation-engine.md#possession-loop--clock].
 */
object PossessionSimEngine {
    fun simulate(input: GameSimInput): GameSimResult {
        val random = Random(input.seed)
        var gameClock = GameSimClock.REGULATION_SECONDS
        var offenseHome = random.nextBoolean()
        var homeBox = TeamBoxScore(teamId = input.home.teamId)
        var awayBox = TeamBoxScore(teamId = input.away.teamId)
        val log = mutableListOf<String>()
        var totalPossessions = 0

        while (gameClock > 0) {
            val tripSeconds = pacedTripSeconds(input.tuning, random)
            val used = min(tripSeconds, gameClock)
            gameClock -= used
            totalPossessions++

            val (before, offTeam, offRating, defRating) = if (offenseHome) {
                Quad(homeBox, "HOME", input.home.offensiveRating, input.away.defensiveRating)
            } else {
                Quad(awayBox, "AWAY", input.away.offensiveRating, input.home.defensiveRating)
            }
            val after = resolvePossession(before, offRating, defRating, random)
            if (offenseHome) {
                homeBox = after
            } else {
                awayBox = after
            }
            log += formatLog(used, gameClock, offTeam, before, after)
            offenseHome = !offenseHome
        }

        val result = GameSimResult(
            homeScore = homeBox.points,
            awayScore = awayBox.points,
            homeBox = homeBox,
            awayBox = awayBox,
            totalPossessions = totalPossessions,
            possessionLog = log,
        )
        BoxScoreCoherence.assertValid(result)
        return result
    }

    private data class Quad(
        val offense: TeamBoxScore,
        val label: String,
        val offRating: Int,
        val defRating: Int,
    )

    internal fun pacedTripSeconds(tuning: SimTuning, random: Random): Int {
        val raw = sampleTripSeconds(tuning, random)
        val targetPerTeam = (tuning.possessionsPerGameTarget.min + tuning.possessionsPerGameTarget.max) / 2
        val targetTrips = targetPerTeam * 2
        val baselineAvg = 12.0
        val scale = (GameSimClock.REGULATION_SECONDS.toDouble() / targetTrips) / baselineAvg
        return (raw * scale).toInt().coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS)
    }

    internal fun sampleTripSeconds(tuning: SimTuning, random: Random): Int {
        val bucket = random.nextInt(3)
        val range = when (bucket) {
            0 -> tuning.timeUsedSeconds.quickTurnover
            1 -> tuning.timeUsedSeconds.quickShot
            else -> tuning.timeUsedSeconds.foulStoppage
        }
        val lo = range.firstOrNull() ?: GameSimClock.MIN_TRIP_SECONDS
        val hi = range.lastOrNull() ?: GameSimClock.MAX_TRIP_SECONDS
        val raw = if (lo >= hi) lo else random.nextInt(lo, hi + 1)
        return raw.coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS)
    }

    private fun resolvePossession(
        offense: TeamBoxScore,
        offRating: Int,
        defRating: Int,
        random: Random,
    ): TeamBoxScore {
        var box = offense.copy(possessions = offense.possessions + 1)
        val edge = (offRating - defRating).coerceIn(-30, 30)
        val makeBias = 0.42 + edge * 0.004
        val roll = random.nextDouble()
        return when {
            roll < 0.22 -> box.copy(turnovers = box.turnovers + 1)
            roll < 0.55 -> {
                val three = random.nextDouble() < 0.38
                val attempt = box.fieldGoalsAttempted + 1
                val made = random.nextDouble() < makeBias
                var next = box.copy(fieldGoalsAttempted = attempt)
                if (three) {
                    next = next.copy(threePointsAttempted = next.threePointsAttempted + 1)
                }
                if (made) {
                    val pts = if (three) 3 else 2
                    next = next.copy(
                        fieldGoalsMade = next.fieldGoalsMade + 1,
                        points = next.points + pts,
                        threePointsMade = next.threePointsMade + if (three) 1 else 0,
                    )
                }
                next
            }
            else -> {
                val attempt = box.fieldGoalsAttempted + 1
                val three = random.nextDouble() < 0.35
                val next = if (three) {
                    box.copy(
                        fieldGoalsAttempted = attempt,
                        threePointsAttempted = box.threePointsAttempted + 1,
                    )
                } else {
                    box.copy(fieldGoalsAttempted = attempt)
                }
                next
            }
        }
    }

    private fun formatLog(
        used: Int,
        clockRemaining: Int,
        offense: String,
        before: TeamBoxScore,
        after: TeamBoxScore,
    ): String {
        val clock = formatClock(clockRemaining)
        val pointsScored = after.points - before.points
        val turnedOver = after.turnovers > before.turnovers
        val scoreLine = when {
            pointsScored > 0 -> "+$pointsScored PTS"
            turnedOver -> "TO"
            after.fieldGoalsAttempted > before.fieldGoalsAttempted -> "miss"
            else -> "possession"
        }
        return "$clock | $offense | ${used}s trip | $scoreLine"
    }

    private fun formatClock(seconds: Int): String {
        val clamped = max(0, seconds)
        val minutes = clamped / 60
        val secs = clamped % 60
        return "%d:%02d".format(minutes, secs)
    }
}

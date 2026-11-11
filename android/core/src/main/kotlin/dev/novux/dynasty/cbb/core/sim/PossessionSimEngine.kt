package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.strategy.LateGamePossessionPolicy
import dev.novux.dynasty.cbb.core.strategy.ResolvedSchemeEffects
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Possession-based regulation sim — [planning/simulation-engine.md#possession-loop--clock].
 */
object PossessionSimEngine {
    fun simulate(input: GameSimInput): GameSimResult = simulate(input, SimulateOptions())

    fun simulate(
        input: GameSimInput,
        options: SimulateOptions = SimulateOptions(),
        injuryRoll: GameSimInjuryRollInput? = null,
    ): GameSimResult {
        val session = PossessionSimSession(
            input,
            options.resume,
            options.stopAtHalftime,
            injuryRoll = injuryRoll,
        )
        while (session.hasNext()) {
            val step = session.step()
            if (step.haltedAtHalftime) {
                return session.toResult(isComplete = false)
            }
        }
        GameSimOvertime.playUntilWinner(session)
        var result = session.toResult(isComplete = true)
        BoxScoreCoherence.assertValid(result)
        if (injuryRoll != null) {
            result = GameSimInjuryRoll.attach(input, result, injuryRoll)
        }
        return result
    }

    internal data class Sides(
        val offense: GameSimTeamInput,
        val defense: GameSimTeamInput,
        val box: TeamBoxScore,
        val label: String,
    )

    internal data class TripSample(
        val rawSeconds: Int,
        val foulStoppage: Boolean,
    )

    internal fun sampleTripForLateGame(
        tuning: SimTuning,
        random: Random,
        decision: LateGamePossessionPolicy.PossessionDecision,
    ): TripSample =
        when (decision) {
            LateGamePossessionPolicy.PossessionDecision.IntentionalFoulTrip -> {
                if (random.nextDouble() < LateGamePossessionPolicy.INTENTIONAL_FOUL_TRIP_RATE) {
                    foulStoppageTripSample(tuning, random)
                } else {
                    sampleTrip(tuning, random)
                }
            }
            LateGamePossessionPolicy.PossessionDecision.ProtectLeadPace -> {
                val base = sampleTrip(tuning, random)
                if (base.foulStoppage &&
                    random.nextDouble() < LateGamePossessionPolicy.PROTECT_LEAD_AVOID_FOUL_STOPPAGE_RATE
                ) {
                    quickShotTripSample(tuning, random)
                } else {
                    base
                }
            }
            LateGamePossessionPolicy.PossessionDecision.None -> sampleTrip(tuning, random)
        }

    private fun foulStoppageTripSample(tuning: SimTuning, random: Random): TripSample {
        val range = tuning.timeUsedSeconds.foulStoppage
        val lo = range.firstOrNull() ?: GameSimClock.MIN_TRIP_SECONDS
        val hi = range.lastOrNull() ?: GameSimClock.MAX_TRIP_SECONDS
        val raw = if (lo >= hi) lo else random.nextInt(lo, hi + 1)
        return TripSample(
            rawSeconds = raw.coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS),
            foulStoppage = true,
        )
    }

    private fun quickShotTripSample(tuning: SimTuning, random: Random): TripSample {
        val range = tuning.timeUsedSeconds.quickShot
        val lo = range.firstOrNull() ?: GameSimClock.MIN_TRIP_SECONDS
        val hi = range.lastOrNull() ?: GameSimClock.MAX_TRIP_SECONDS
        val raw = if (lo >= hi) lo else random.nextInt(lo, hi + 1)
        return TripSample(
            rawSeconds = raw.coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS),
            foulStoppage = false,
        )
    }

    internal fun sampleTrip(tuning: SimTuning, random: Random): TripSample {
        val bucket = random.nextInt(3)
        val foulStoppage = bucket == 2
        val range = when (bucket) {
            0 -> tuning.timeUsedSeconds.quickTurnover
            1 -> tuning.timeUsedSeconds.quickShot
            else -> tuning.timeUsedSeconds.foulStoppage
        }
        val lo = range.firstOrNull() ?: GameSimClock.MIN_TRIP_SECONDS
        val hi = range.lastOrNull() ?: GameSimClock.MAX_TRIP_SECONDS
        val raw = if (lo >= hi) lo else random.nextInt(lo, hi + 1)
        return TripSample(
            rawSeconds = raw.coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS),
            foulStoppage = foulStoppage,
        )
    }

    internal fun pacedTripSecondsFromRaw(
        tuning: SimTuning,
        rawSeconds: Int,
        tripPaceScale: Double = 1.0,
    ): Int {
        val targetPerTeam = (tuning.possessionsPerGameTarget.min + tuning.possessionsPerGameTarget.max) / 2
        val targetTrips = targetPerTeam * 2
        val baselineAvg = 12.0
        val scale = (GameSimClock.REGULATION_SECONDS.toDouble() / targetTrips) / baselineAvg
        val adjusted = (rawSeconds * scale * tripPaceScale).toInt()
        return adjusted.coerceIn(GameSimClock.MIN_TRIP_SECONDS, GameSimClock.MAX_TRIP_SECONDS)
    }

    internal fun pacedTripSeconds(tuning: SimTuning, random: Random, tripPaceScale: Double = 1.0): Int =
        pacedTripSecondsFromRaw(tuning, sampleTrip(tuning, random).rawSeconds, tripPaceScale)

    internal fun sampleTripSeconds(tuning: SimTuning, random: Random): Int =
        sampleTrip(tuning, random).rawSeconds

    internal fun resolveShotClockViolation(offense: TeamBoxScore): PossessionResolveResult {
        val box = offense.copy(
            possessions = offense.possessions + 1,
            turnovers = offense.turnovers + 1,
        )
        return PossessionResolveResult(
            box,
            PossessionChange.DefenseTakesBall,
            turnoverTerminal = TurnoverTerminal.ShotClockViolation,
        )
    }

    internal fun resolvePossessionForSession(
        offense: TeamBoxScore,
        offRating: Int,
        defRating: Int,
        effects: ResolvedSchemeEffects,
        random: Random,
    ): PossessionResolveResult =
        PossessionSubPhaseEngine.resolve(offense, offRating, defRating, effects, random)

    /** Miss terminal → OReb or DReb change ([MEGA-SIM-090]). */
    internal fun resolveMissRebound(
        afterMiss: TeamBoxScore,
        random: Random,
    ): PossessionResolveResult = offensiveReboundOrDefReb(afterMiss, random)

    private fun offensiveReboundOrDefReb(
        afterMiss: TeamBoxScore,
        random: Random,
    ): PossessionResolveResult {
        val oreb = random.nextDouble() < PossessionShotClock.DEFAULT_OFFENSIVE_REBOUND_KEEP_RATE
        return if (oreb) {
            PossessionResolveResult(afterMiss, PossessionChange.OffenseRetainsOffensiveRebound)
        } else {
            PossessionResolveResult(afterMiss, PossessionChange.DefenseTakesBall)
        }
    }

    internal fun formatPossessionLog(
        used: Int,
        clockRemaining: Int,
        offense: String,
        before: TeamBoxScore,
        after: TeamBoxScore,
        shotClockBefore: Int,
        shotClockAfter: Int,
        change: PossessionChange,
        turnoverTerminal: TurnoverTerminal? = null,
        foulNote: String? = null,
        lateGameTag: String? = null,
        subPhaseChain: String? = null,
    ): String {
        val clock = formatClock(clockRemaining)
        val pointsScored = after.points - before.points
        val turnedOver = after.turnovers > before.turnovers
        val scoreLine = when {
            pointsScored > 0 -> "+$pointsScored PTS"
            turnedOver && turnoverTerminal == TurnoverTerminal.ShotClockViolation -> "TO shot-clock"
            turnedOver -> "TO"
            change == PossessionChange.OffenseRetainsOffensiveRebound ->
                "miss | OREB +1 | retain possession"
            after.fieldGoalsAttempted > before.fieldGoalsAttempted -> "miss"
            else -> "possession"
        }
        val foulSuffix = foulNote?.let { " | $it" }.orEmpty()
        val lateSuffix = lateGameTag?.let { " | $it" }.orEmpty()
        val phaseSuffix = subPhaseChain?.let { " | $it" }.orEmpty()
        return "$clock | $offense | ${used}s trip | SC $shotClockBefore→$shotClockAfter | $scoreLine$foulSuffix$lateSuffix$phaseSuffix"
    }

    private fun formatClock(seconds: Int): String {
        val clamped = max(0, seconds)
        val minutes = clamped / 60
        val secs = clamped % 60
        return "%d:%02d".format(minutes, secs)
    }
}

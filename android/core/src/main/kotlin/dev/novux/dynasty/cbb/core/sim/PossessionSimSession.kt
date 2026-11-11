package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.strategy.GameplanEffectsResolver
import dev.novux.dynasty.cbb.core.strategy.GameplanUsageNormalizer
import dev.novux.dynasty.cbb.core.strategy.SchemeMatchupResolver
import kotlin.random.Random

data class PossessionStep(
    val logLine: String,
    val haltedAtHalftime: Boolean,
)

/**
 * Steppable possession loop for watch playback and halftime resume.
 */
class PossessionSimSession(
  input: GameSimInput,
  resume: MidGameState? = null,
  private val stopAtHalftime: Boolean = false,
) {
    private val input: GameSimInput = input
    private var random: Random = resume?.random ?: Random(input.seed)
    private var gameClock: Int = resume?.gameClockSeconds ?: GameSimClock.REGULATION_SECONDS
    private var shotClockSeconds: Int = resume?.shotClockSecondsRemaining
        ?: PossessionShotClock.resetOnChangeOfPossession(input.tuning.shotClock)
    private var offenseHome: Boolean = resume?.offenseHome ?: random.nextBoolean()
    private var homeBox: TeamBoxScore = resume?.homeBox ?: TeamBoxScore(teamId = input.home.teamId)
    private var awayBox: TeamBoxScore = resume?.awayBox ?: TeamBoxScore(teamId = input.away.teamId)
    private val log: MutableList<String> = resume?.possessionLog?.toMutableList() ?: mutableListOf()
    private var totalPossessions: Int = resume?.totalPossessions ?: 0

    fun hasNext(): Boolean = gameClock > 0

    fun step(): PossessionStep {
        check(hasNext()) { "No possessions remaining" }
        val sides = if (offenseHome) {
            PossessionSimEngine.Sides(input.home, input.away, homeBox, "HOME")
        } else {
            PossessionSimEngine.Sides(input.away, input.home, awayBox, "AWAY")
        }
        val schemeEffects = SchemeMatchupResolver.resolve(
            sides.offense.offensiveScheme,
            sides.defense.defensiveScheme,
        )
        val offGameplan = if (offenseHome) {
            input.activeHomeGameplan(gameClock)
        } else {
            input.activeAwayGameplan(gameClock)
        }
        val defGameplan = if (offenseHome) {
            input.activeAwayGameplan(gameClock)
        } else {
            input.activeHomeGameplan(gameClock)
        }
        val defenseScore = if (offenseHome) awayBox.points else homeBox.points
        val offenseScore = if (offenseHome) homeBox.points else awayBox.points
        val usagePeak = GameplanUsageNormalizer.normalize(offGameplan?.usageWeights.orEmpty())
            .values
            .maxOrNull() ?: 0.2
        val effects = GameplanEffectsResolver.mergeInto(
            schemeEffects,
            GameplanEffectsResolver.offenseTweaks(offGameplan, usagePeak),
            GameplanEffectsResolver.defenseTweaks(defGameplan, gameClock, defenseScore, offenseScore),
        )
        val tripSeconds = PossessionSimEngine.pacedTripSeconds(input.tuning, random, effects.tripPaceScale)
        val shotClockBefore = shotClockSeconds
        val used = PossessionShotClock.secondsConsumed(tripSeconds, shotClockSeconds, gameClock)
        gameClock -= used
        val shotClockAfterTrip = PossessionShotClock.remainingAfterTrip(shotClockBefore, used)
        totalPossessions++

        val offRating = (if (offenseHome) input.homeOffensive() else input.awayOffensive()) +
            effects.offensiveRatingDelta
        val defRating = (if (offenseHome) input.awayDefensive() else input.homeDefensive()) +
            effects.defensiveRatingDelta
        val resolution = PossessionSimEngine.resolvePossessionForSession(
            sides.box,
            offRating,
            defRating,
            effects,
            random,
        )
        if (offenseHome) {
            homeBox = resolution.box
        } else {
            awayBox = resolution.box
        }
        when (resolution.change) {
            PossessionChange.DefenseTakesBall -> {
                offenseHome = !offenseHome
                shotClockSeconds = PossessionShotClock.resetOnChangeOfPossession(input.tuning.shotClock)
            }
            PossessionChange.OffenseRetainsOffensiveRebound -> {
                shotClockSeconds = PossessionShotClock.resetAfterOffensiveRebound(input.tuning.shotClock)
            }
        }
        val line = PossessionSimEngine.formatPossessionLog(
            used,
            gameClock,
            sides.label,
            sides.box,
            resolution.box,
            shotClockBefore = shotClockBefore,
            shotClockAfter = shotClockAfterTrip,
            change = resolution.change,
        )
        log += line
        val halftime = stopAtHalftime && gameClock <= GameSimClock.HALF_SECONDS
        return PossessionStep(line, haltedAtHalftime = halftime)
    }

    fun toMidGame(): MidGameState = MidGameState(
        random = random,
        gameClockSeconds = gameClock,
        offenseHome = offenseHome,
        homeBox = homeBox,
        awayBox = awayBox,
        possessionLog = log.toList(),
        totalPossessions = totalPossessions,
        shotClockSecondsRemaining = shotClockSeconds,
    )

    fun toResult(isComplete: Boolean): GameSimResult = GameSimResult(
        homeScore = homeBox.points,
        awayScore = awayBox.points,
        homeBox = homeBox,
        awayBox = awayBox,
        totalPossessions = totalPossessions,
        possessionLog = log.toList(),
        isComplete = isComplete,
        midGame = if (isComplete) null else toMidGame(),
    )
}

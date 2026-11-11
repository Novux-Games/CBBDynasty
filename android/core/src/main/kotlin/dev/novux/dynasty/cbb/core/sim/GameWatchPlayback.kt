package dev.novux.dynasty.cbb.core.sim

import kotlinx.coroutines.delay

/**
 * Watch pacing — [planning/simulation-engine.md#playback-watch-speed-sim-to-end].
 */
object GameWatchPlayback {
    const val DEFAULT_POSSESSION_DELAY_MS: Long = 2_000L

    /** Real-time watch uses ~2s per possession line at 1× (not scrimmage fast-forward). */
    const val DEFAULT_WATCH_SPEED_MULTIPLIER: Double = 1.0

    fun lineDelayMs(speedMultiplier: Double): Long =
        if (speedMultiplier <= 0.0) {
            0L
        } else {
            (DEFAULT_POSSESSION_DELAY_MS / speedMultiplier).toLong().coerceAtLeast(0L)
        }

    suspend fun watch(
        input: GameSimInput,
        speedMultiplier: Double = DEFAULT_WATCH_SPEED_MULTIPLIER,
        injuryRoll: GameSimInjuryRollInput? = null,
        onLine: suspend (String) -> Unit,
        onHalftime: suspend (GameSimResult, MidGameState) -> GameSimInput,
    ): GameSimResult {
        val lineDelay = lineDelayMs(speedMultiplier)
        var activeInput = input
        var session = PossessionSimSession(activeInput, stopAtHalftime = true)
        var resumedAfterHalftime = false
        while (session.hasNext()) {
            val step = session.step()
            onLine(step.logLine)
            if (lineDelay > 0) {
                delay(lineDelay)
            }
            if (step.haltedAtHalftime) {
                val partial = session.toResult(isComplete = false)
                val mid = checkNotNull(partial.midGame)
                activeInput = onHalftime(partial, mid)
                session = PossessionSimSession(activeInput, resume = mid, stopAtHalftime = false)
                resumedAfterHalftime = true
            }
        }
        var final = session.toResult(isComplete = true)
        require(resumedAfterHalftime || final.totalPossessions > 0)
        if (injuryRoll != null) {
            final = GameSimInjuryRoll.attach(input, final, injuryRoll)
        }
        BoxScoreCoherence.assertValid(final)
        return final
    }

    fun simToEnd(
        input: GameSimInput,
        injuryRoll: GameSimInjuryRollInput? = null,
    ): GameSimResult = PossessionSimEngine.simulate(input, injuryRoll = injuryRoll)
}

package dev.novux.dynasty.cbb.core.sim

import kotlinx.coroutines.delay

/**
 * Watch pacing — [planning/simulation-engine.md#playback-watch-speed-sim-to-end].
 */
object GameWatchPlayback {
    const val DEFAULT_POSSESSION_DELAY_MS: Long = 2_000L

    suspend fun watch(
        input: GameSimInput,
        speedMultiplier: Double = 1.0,
        onLine: suspend (String) -> Unit,
        onHalftime: suspend (GameSimResult, MidGameState) -> GameSimInput,
    ): GameSimResult {
        val lineDelay = if (speedMultiplier <= 0.0) {
            0L
        } else {
            (DEFAULT_POSSESSION_DELAY_MS / speedMultiplier).toLong().coerceAtLeast(0L)
        }
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
        val final = session.toResult(isComplete = true)
        require(resumedAfterHalftime || final.totalPossessions > 0)
        BoxScoreCoherence.assertValid(final)
        return final
    }

    fun simToEnd(input: GameSimInput): GameSimResult = PossessionSimEngine.simulate(input)
}

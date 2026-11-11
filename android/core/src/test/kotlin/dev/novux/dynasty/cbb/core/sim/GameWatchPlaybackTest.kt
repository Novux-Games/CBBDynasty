package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.sim.tuning.SimShotClock
import dev.novux.dynasty.cbb.core.sim.tuning.SimTimeUsedSeconds
import dev.novux.dynasty.cbb.core.sim.tuning.SimVenueModifiers
import dev.novux.dynasty.cbb.core.sim.tuning.IntMinMax
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameWatchPlaybackTest {
    private val tuning = SimTuning(
        simTuningId = "test",
        schemaVersion = 1,
        possessionsPerGameTarget = IntMinMax(60, 70),
        shotClock = SimShotClock(full = 30, offensiveRebound = 20),
        timeUsedSeconds = SimTimeUsedSeconds(
            quickTurnover = listOf(3, 6),
            quickShot = listOf(8, 14),
            foulStoppage = listOf(12, 18),
        ),
        eventWeights = JsonObject(emptyMap()),
        homeNeutralModifiers = SimVenueModifiers(1.0, 1.0, 1.0),
    )

    @Test
    fun defaultWatchSpeedYieldsTwoSecondLineDelay() {
        assertEquals(2_000L, GameWatchPlayback.lineDelayMs(GameWatchPlayback.DEFAULT_WATCH_SPEED_MULTIPLIER))
    }

    @Test
    fun watchStreamsLinesAndMatchesSimToEnd() = runBlocking {
        val input = GameSimInput(
            home = GameSimTeamInput("h", 80, 78),
            away = GameSimTeamInput("a", 77, 76),
            tuning = tuning,
            seed = 33L,
        )
        val lines = mutableListOf<String>()
        var halftimeCalls = 0
        val watched = GameWatchPlayback.watch(
            input = input,
            speedMultiplier = 0.0,
            onLine = { lines += it },
            onHalftime = { _, _ ->
                halftimeCalls++
                input
            },
        )
        val instant = GameWatchPlayback.simToEnd(input)
        assertEquals(instant.homeScore, watched.homeScore)
        assertEquals(instant.awayScore, watched.awayScore)
        assertEquals(instant.possessionLog.size, lines.size)
        assertEquals(1, halftimeCalls)
        assertTrue(lines.isNotEmpty())
    }
}

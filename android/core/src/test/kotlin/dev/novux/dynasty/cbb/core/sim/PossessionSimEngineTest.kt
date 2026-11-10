package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.sim.tuning.SimShotClock
import dev.novux.dynasty.cbb.core.sim.tuning.SimTimeUsedSeconds
import dev.novux.dynasty.cbb.core.sim.tuning.SimVenueModifiers
import dev.novux.dynasty.cbb.core.sim.tuning.IntMinMax
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PossessionSimEngineTest {
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
    fun tripSecondsWithinThreeToThirty() {
        val random = kotlin.random.Random(42)
        repeat(100) {
            val sec = PossessionSimEngine.sampleTripSeconds(tuning, random)
            assertTrue(sec in GameSimClock.MIN_TRIP_SECONDS..GameSimClock.MAX_TRIP_SECONDS)
        }
    }

    @Test
    fun boxScoreMatchesScoreboard() {
        val result = PossessionSimEngine.simulate(
            GameSimInput(
                home = GameSimTeamInput("h", 78, 74),
                away = GameSimTeamInput("a", 76, 75),
                tuning = tuning,
                seed = 99L,
            ),
        )
        assertEquals(result.homeScore, result.homeBox.points)
        assertEquals(result.awayScore, result.awayBox.points)
        assertTrue(result.homeScore in 20..120)
        assertTrue(result.awayScore in 20..120)
        val possessionsPerTeam = (result.homeBox.possessions + result.awayBox.possessions) / 2
        assertTrue(possessionsPerTeam in 35..100)
    }

    @Test
    fun strongerOffenseScoresMoreOften() {
        var homeTotal = 0
        var awayTotal = 0
        repeat(30) { i ->
            val result = PossessionSimEngine.simulate(
                GameSimInput(
                    home = GameSimTeamInput("h", 92, 70),
                    away = GameSimTeamInput("a", 62, 70),
                    tuning = tuning,
                    seed = i.toLong(),
                ),
            )
            homeTotal += result.homeScore
            awayTotal += result.awayScore
        }
        assertTrue(homeTotal > awayTotal)
    }
}

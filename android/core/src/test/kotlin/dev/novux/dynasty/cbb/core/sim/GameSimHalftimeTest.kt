package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.sim.tuning.SimShotClock
import dev.novux.dynasty.cbb.core.sim.tuning.SimTimeUsedSeconds
import dev.novux.dynasty.cbb.core.sim.tuning.SimVenueModifiers
import dev.novux.dynasty.cbb.core.sim.tuning.IntMinMax
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSimHalftimeTest {
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

  private fun input(seed: Long) = GameSimInput(
        home = GameSimTeamInput("h", 78, 74),
        away = GameSimTeamInput("a", 76, 75),
        tuning = tuning,
        seed = seed,
    )

    @Test
    fun stopAtHalftimePausesWithClockAtOrBelowHalf() {
        val firstHalf = PossessionSimEngine.simulate(input(12L), SimulateOptions(stopAtHalftime = true))
        assertFalse(firstHalf.isComplete)
        assertNotNull(firstHalf.midGame)
        val mid = firstHalf.midGame!!
        assertTrue(mid.gameClockSeconds <= GameSimClock.HALF_SECONDS)
        val full = PossessionSimEngine.simulate(input(12L), SimulateOptions(resume = mid))
        assertTrue(full.isComplete)
        assertEquals(
            PossessionSimEngine.simulate(input(12L)).homeScore,
            full.homeScore,
        )
        assertEquals(
            PossessionSimEngine.simulate(input(12L)).awayScore,
            full.awayScore,
        )
    }
}

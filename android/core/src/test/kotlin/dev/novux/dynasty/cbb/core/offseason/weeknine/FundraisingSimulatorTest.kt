package dev.novux.dynasty.cbb.core.offseason.weeknine

import org.junit.Assert.assertTrue
import org.junit.Test

class FundraisingSimulatorTest {
    @Test
    fun donorDriveSkewsNil() {
        val gala = FundraisingSimulator.simulate(FundraisingEventType.Gala, 50)
        val donor = FundraisingSimulator.simulate(FundraisingEventType.DonorDrive, 50)
        assertTrue(donor.nilBoostUsd >= gala.nilBoostUsd)
    }

    @Test
    fun higherPrestigeRaisesCash() {
        val low = FundraisingSimulator.simulate(FundraisingEventType.AlumniGame, 30)
        val high = FundraisingSimulator.simulate(FundraisingEventType.AlumniGame, 80)
        assertTrue(high.totalCashGain > low.totalCashGain)
    }
}

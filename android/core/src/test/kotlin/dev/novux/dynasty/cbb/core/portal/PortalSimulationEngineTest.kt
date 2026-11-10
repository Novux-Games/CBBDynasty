package dev.novux.dynasty.cbb.core.portal

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PortalSimulationEngineTest {
    @Test
    fun consideringPoolSize_inDesignBand() {
        repeat(50) {
            val size = PortalSimulationEngine.rollConsideringPoolSize(Random(it.toLong()))
            assertTrue(size in PortalConstants.CONSIDERING_POOL_MIN..PortalConstants.CONSIDERING_POOL_MAX)
        }
    }

    @Test
    fun finalEntrants_averageNearThreeToFourPerTeam() {
        val samples = (1..20).map {
            PortalSimulationEngine.simulateOffseasonCycle(2, Random(42L + it)).entrants.size
        }
        val perTeam = samples.map { it.toDouble() / PortalConstants.DI_PROGRAM_COUNT }
        val mean = perTeam.average()
        assertTrue("mean per team $mean", mean in 2.5..4.5)
    }

    @Test
    fun week2Start_locksPortalTop40() {
        val cycle = PortalSimulationEngine.simulateOffseasonCycle(1, Random(99))
        assertTrue(cycle.entrants.size >= PortalConstants.TOP_ENTRANTS_RANKED)
        val locked = cycle.onOffseasonWeekStarted(2)
        assertTrue(locked.top40Locked)
        val top = PortalTop40.rankedList(locked.entrants)
        assertEquals(PortalConstants.TOP_ENTRANTS_RANKED, top.size)
        assertEquals(1, top.first().nationalTop40Rank)
        assertTrue(top.all { it.stars != null })
        assertTrue(top.all { entrant ->
            PortalEligibility.immediateEligibilityAtSign(entrant.eligibilityClock, 2)
        })
    }
}

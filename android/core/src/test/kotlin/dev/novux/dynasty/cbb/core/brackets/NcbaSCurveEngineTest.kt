package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import org.junit.Assert.assertEquals
import org.junit.Test

class NcbaSCurveEngineTest {
    @Test
    fun serpentineAssignsTopFourToDistinctRegions() {
        assertEquals(NcbaRegionId.East, NcbaSCurveEngine.regionForBracketSeed(1))
        assertEquals(NcbaRegionId.Midwest, NcbaSCurveEngine.regionForBracketSeed(2))
        assertEquals(NcbaRegionId.South, NcbaSCurveEngine.regionForBracketSeed(3))
        assertEquals(NcbaRegionId.West, NcbaSCurveEngine.regionForBracketSeed(4))
        assertEquals(NcbaRegionId.West, NcbaSCurveEngine.regionForBracketSeed(5))
    }

    @Test
    fun assignEntrants_placesSixteenPerRegion() {
        val field = (1..64).map { seed ->
            NcbaSelectionEntry(
                teamId = "t$seed",
                bidType = if (seed <= 52) NcbaBidType.DirectAtLarge else NcbaBidType.OpeningRound,
                netRank = seed,
                seedIndex = 1.0,
                ncbaSeed = seed,
            )
        }
        val entrants = NcbaSCurveEngine.assignEntrants(field)
        assertEquals(64, entrants.size)
        NcbaRegionId.serpentineOrder.forEach { region ->
            assertEquals(16, entrants.count { it.regionId == region })
        }
    }
}

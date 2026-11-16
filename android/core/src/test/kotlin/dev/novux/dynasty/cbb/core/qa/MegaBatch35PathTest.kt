package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.coach.MegaCoachBatch35TracePolicy
import dev.novux.dynasty.cbb.core.gdd.MegaGddBatch35TracePolicy
import dev.novux.dynasty.cbb.core.gdd.OfflineInternetPermissionPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyResetStartersPolicy
import dev.novux.dynasty.cbb.core.lineup.StrategyStarterWirePolicy
import dev.novux.dynasty.cbb.core.pack.ConferenceTournamentVenuePolicy
import dev.novux.dynasty.cbb.core.pack.DefaultPackConferenceSizeManifestPolicy
import dev.novux.dynasty.cbb.core.recruiting.MegaRecruitBatch35TracePolicy
import dev.novux.dynasty.cbb.core.sim.GameWatchPhaseBadgePolicy
import dev.novux.dynasty.cbb.core.sim.HalftimeOpeningFivePolicy
import dev.novux.dynasty.cbb.core.sim.PlayerBoxPlusMinusPolicy
import dev.novux.dynasty.cbb.core.sim.PossessionClutchLogPolicy
import dev.novux.dynasty.cbb.core.sim.PossessionRareEventPolicy
import dev.novux.dynasty.cbb.core.sim.WinSharesUiDocPolicy
import dev.novux.dynasty.cbb.core.strategy.HalftimeGameplanPersistPolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch35PathTest {
    @Test
    fun batch35SimAndWorldPolicies() {
        assertEquals("MEGA-SIM-116", PossessionClutchLogPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-096", PossessionRareEventPolicy.MEGA_JUMP_BALL_ID)
        assertEquals("MEGA-SIM-107", PossessionRareEventPolicy.MEGA_TECHNICAL_ID)
        assertEquals("MEGA-SIM-110", HalftimeOpeningFivePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-128", GameWatchPhaseBadgePolicy.MEGA_ID)
        assertEquals("MEGA-SIM-134", PlayerBoxPlusMinusPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-142", StrategyResetStartersPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-053", HalftimeGameplanPersistPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-058", StrategyStarterWirePolicy.MEGA_WIRE_058)
        assertEquals("MEGA-WORLD-144", ConferenceTournamentVenuePolicy.MEGA_ID)
        assertEquals("MEGA-WORLD-147", DefaultPackConferenceSizeManifestPolicy.MEGA_ID)
        assertEquals("MEGA-SIM-174", WinSharesUiDocPolicy.MEGA_ID)
        assertEquals("MEGA-GDD-092", OfflineInternetPermissionPolicy.MEGA_ID)
        assertTrue(WinSharesUiDocPolicy.HINT.contains("season end"))
        assertEquals(
            "Neutral site",
            ConferenceTournamentVenuePolicy.venueLabel("neutral-primary", "Template"),
        )
    }

    @Test
    fun batch35QueueTraceIds() {
        assertEquals("MEGA-RECR-016", MegaRecruitBatch35TracePolicy.MEGA_016)
        assertEquals("MEGA-COACH-103", MegaCoachBatch35TracePolicy.MEGA_103)
        assertEquals("MEGA-GDD-076", MegaGddBatch35TracePolicy.MEGA_076)
    }
}
package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertEquals
import org.junit.Test

class DynastyMetadataTest {
    @Test
    fun bootstrap_matchesGameStartSeasonOne() {
        val meta = DynastyBootstrap.newDynasty(dynastyId = "dyn-1")

        assertEquals(1, meta.seasonIndex)
        assertEquals(CalendarPhase.Offseason, meta.phase)
        assertEquals(DynastySchema.DEFAULT_PACK_ID, meta.packId)
        assertEquals(DynastySchema.CURRENT_VERSION, meta.dynastySchemaVersion)
        assertEquals(DynastySchema.DEFAULT_BALANCE_RULESET_ID, meta.balanceRulesetId)
        assertEquals(DynastySchema.DEFAULT_BALANCE_SCHEMA_VERSION, meta.balanceSchemaVersion)
        assertEquals(DynastySchema.DEFAULT_SIM_TUNING_ID, meta.simTuningId)
        assertEquals(DynastySchema.DEFAULT_SIM_SCHEMA_VERSION, meta.simSchemaVersion)
    }
}

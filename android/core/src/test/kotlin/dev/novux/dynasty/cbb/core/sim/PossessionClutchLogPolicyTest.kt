package dev.novux.dynasty.cbb.core.sim

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PossessionClutchLogPolicyTest {
    @Test
    fun clutchTagWhenCloseAndLate() {
        assertEquals("CLUTCH", PossessionClutchLogPolicy.logTag(25, 70, 68))
    }

    @Test
    fun noTagWhenBlowout() {
        assertNull(PossessionClutchLogPolicy.logTag(25, 80, 60))
    }
}
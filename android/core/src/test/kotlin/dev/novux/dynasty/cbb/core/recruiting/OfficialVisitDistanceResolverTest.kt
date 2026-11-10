package dev.novux.dynasty.cbb.core.recruiting

import org.junit.Assert.assertEquals
import org.junit.Test

class OfficialVisitDistanceResolverTest {
    private val school = SchoolRecruitingContext(
        programPrestige = 40,
        schoolHomeState = "OH",
        pipelineStates = setOf("KY", "IN", "MI", "PA", "WV"),
        nilPoolAnnualUsd = 0,
    )

    @Test
    fun local_inStateAndPipeline() {
        assertEquals(OfficialVisitDistanceBand.Local, OfficialVisitDistanceResolver.resolve("OH", school))
        assertEquals(OfficialVisitDistanceBand.Local, OfficialVisitDistanceResolver.resolve("KY", school))
    }

    @Test
    fun regional_sameRegion() {
        assertEquals(OfficialVisitDistanceBand.Regional, OfficialVisitDistanceResolver.resolve("IL", school))
    }

    @Test
    fun remote_crossCountry() {
        assertEquals(OfficialVisitDistanceBand.Remote, OfficialVisitDistanceResolver.resolve("CA", school))
    }
}

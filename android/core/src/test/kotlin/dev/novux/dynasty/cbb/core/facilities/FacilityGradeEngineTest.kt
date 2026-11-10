package dev.novux.dynasty.cbb.core.facilities

import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityGrades
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FacilityGradeEngineTest {
    @Test
    fun defaultGrades_averageFive_pitchNeutral() {
        val summary = FacilityGradeEngine.summarize(FacilityGrades())
        assertEquals(5, summary.averageGrade)
        assertEquals(1.0, summary.recruitingPitchMultiplier, 0.001)
    }

    @Test
    fun loungeUpgrade_increasesPitchMultiplier() {
        val boosted = FacilityGrades(
            grades = mapOf(FacilityCategory.RecruitingLounge to 8),
        )
        assertTrue(FacilityGradeEngine.summarize(boosted).recruitingPitchMultiplier > 1.0)
    }
}

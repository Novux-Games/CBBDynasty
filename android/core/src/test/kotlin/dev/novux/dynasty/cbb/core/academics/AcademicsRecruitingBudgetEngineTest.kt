package dev.novux.dynasty.cbb.core.academics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AcademicsRecruitingBudgetEngineTest {
    @Test
    fun baselineGrade_neutralMultiplier() {
        assertEquals(1.0, AcademicsRecruitingBudgetEngine.budgetMultiplier(70), 0.001)
    }

    @Test
    fun highGrade_boostsWeeklyPool() {
        val base = 1000
        val boosted = AcademicsRecruitingBudgetEngine.effectiveWeeklyPool(base, 90)
        assertTrue(boosted > base)
    }
}

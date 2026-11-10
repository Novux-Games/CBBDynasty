package dev.novux.dynasty.cbb.core.offseason.weeknine

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FacilityUpgradePolicyTest {
    @Test
    fun blocksAtMaxGrade() {
        val grades = FacilityGrades(
            grades = FacilityCategory.entries.associateWith { 10 },
        )
        assertFalse(FacilityUpgradePolicy.canUpgrade(grades, FacilityCategory.Arena))
    }

    @Test
    fun requiresCash() {
        val grades = FacilityGrades()
        val cost = FacilityUpgradePolicy.upgradeCostUsd(grades.grade(FacilityCategory.Medical))
        assertFalse(FacilityUpgradePolicy.canAfford(cost - 1, grades, FacilityCategory.Medical))
        assertTrue(FacilityUpgradePolicy.canAfford(cost, grades, FacilityCategory.Medical))
    }
}

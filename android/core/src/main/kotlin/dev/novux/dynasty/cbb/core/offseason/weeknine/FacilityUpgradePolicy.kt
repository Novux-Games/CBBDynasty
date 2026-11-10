package dev.novux.dynasty.cbb.core.offseason.weeknine

object FacilityUpgradePolicy {
    fun upgradeCostUsd(currentGrade: Int): Int = 40_000 + currentGrade * 25_000

    fun canUpgrade(grades: FacilityGrades, category: FacilityCategory): Boolean =
        grades.grade(category) < OffseasonWeekNineConstants.FACILITY_GRADE_MAX

    fun canAfford(cashUsd: Int, grades: FacilityGrades, category: FacilityCategory): Boolean {
        if (!canUpgrade(grades, category)) return false
        return cashUsd >= upgradeCostUsd(grades.grade(category))
    }
}

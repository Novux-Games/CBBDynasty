package dev.novux.dynasty.cbb.core.academics

import kotlin.math.roundToInt

/** Academics → weekly recruiting pool multiplier ([planning/game-design.md] glossary). */
object AcademicsRecruitingBudgetEngine {
    const val BASELINE_GRADE: Int = 70

    fun budgetMultiplier(programGrade: Int): Double {
        val grade = programGrade.coerceIn(0, 100)
        val delta = (grade - BASELINE_GRADE) / 100.0
        return (1.0 + delta * 0.20).coerceIn(0.85, 1.15)
    }

    fun effectiveWeeklyPool(prestigeTierBase: Int, programGrade: Int): Int =
        (prestigeTierBase * budgetMultiplier(programGrade)).roundToInt().coerceAtLeast(0)
}

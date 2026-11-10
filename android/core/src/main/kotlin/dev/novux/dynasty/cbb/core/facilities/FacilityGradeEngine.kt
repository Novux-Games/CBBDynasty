package dev.novux.dynasty.cbb.core.facilities

import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityGrades
import dev.novux.dynasty.cbb.core.offseason.weeknine.OffseasonWeekNineConstants
import kotlin.math.roundToInt

/**
 * Facility grades (1–10) drive pitch efficiency and overview displays ([planning/game-design.md#facility-categories-grades-1–10]).
 */
data class FacilityGradeSummary(
    val averageGrade: Int,
    val byCategory: Map<FacilityCategory, Int>,
    val recruitingPitchMultiplier: Double,
    val campusWowScore: Double,
)

object FacilityGradeEngine {
    fun summarize(grades: FacilityGrades): FacilityGradeSummary {
        val byCategory = FacilityCategory.entries.associateWith { grades.grade(it) }
        val average = byCategory.values.average().roundToInt()
            .coerceIn(
                OffseasonWeekNineConstants.FACILITY_GRADE_MIN,
                OffseasonWeekNineConstants.FACILITY_GRADE_MAX,
            )
        val lounge = grades.grade(FacilityCategory.RecruitingLounge)
        return FacilityGradeSummary(
            averageGrade = average,
            byCategory = byCategory,
            recruitingPitchMultiplier = recruitingPitchMultiplier(lounge),
            campusWowScore = campusWowScore(average),
        )
    }

    /** Recruiting lounge grade → action efficiency (1.0 at grade 5, +4% per grade above). */
    fun recruitingPitchMultiplier(recruitingLoungeGrade: Int): Double {
        val g = recruitingLoungeGrade.coerceIn(
            OffseasonWeekNineConstants.FACILITY_GRADE_MIN,
            OffseasonWeekNineConstants.FACILITY_GRADE_MAX,
        )
        return 1.0 + (g - FacilityGrades.DEFAULT_GRADE) * 0.04
    }

    fun campusWowScore(averageGrade: Int): Double {
        val g = averageGrade.coerceIn(
            OffseasonWeekNineConstants.FACILITY_GRADE_MIN,
            OffseasonWeekNineConstants.FACILITY_GRADE_MAX,
        )
        return (0.35 + g * 0.065).coerceIn(0.0, 1.0)
    }
}

package dev.novux.dynasty.cbb.core.academics

import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityGrades
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import kotlin.math.roundToInt

data class ProgramAcademicsInputs(
    val teamGpaAverage: Double,
    val retentionScore: Double,
    val eligibilityIncidentCount: Int,
    val academicFacilityGrade: Int,
)

data class ProgramAcademicsSnapshot(
    val programGrade: Int,
    val recruitingBudgetMultiplier: Double,
)

/**
 * Program academics grade (0–100) — [planning/game-design.md#academic-grades-program].
 */
object ProgramAcademicsGradeEngine {
    fun inputsFromRoster(players: List<RosterPlayer>, facilityGrades: FacilityGrades): ProgramAcademicsInputs {
        val active = players.filter { !it.inTransferPortal && !it.declaredForDraft }
        val gpaAvg = if (active.isEmpty()) {
            2.5
        } else {
            active.sumOf { it.gpaCurrent } / active.size
        }
        val retained = active.count { it.eligibilityYearsRemaining > 0 }
        val retentionPct = if (active.isEmpty()) 1.0 else retained.toDouble() / active.size
        val incidents = players.count { it.academicHold || it.gamesSuspendedRemaining > 0 }
        return ProgramAcademicsInputs(
            teamGpaAverage = gpaAvg,
            retentionScore = (retentionPct * 100.0).coerceIn(0.0, 100.0),
            eligibilityIncidentCount = incidents,
            academicFacilityGrade = facilityGrades.grade(FacilityCategory.AcademicCenter),
        )
    }

    fun computeGrade(inputs: ProgramAcademicsInputs): Int {
        val gpaScore = ((inputs.teamGpaAverage - 1.5) / 2.5 * 100.0).coerceIn(0.0, 100.0)
        val incidentPenalty = (inputs.eligibilityIncidentCount * 8.0).coerceAtMost(40.0)
        val facilityBonus = (inputs.academicFacilityGrade - 5).coerceAtLeast(0) * 2.0
        val raw = gpaScore * 0.30 +
            inputs.retentionScore * 0.40 -
            incidentPenalty +
            facilityBonus
        return raw.roundToInt().coerceIn(0, 100)
    }

    fun snapshot(inputs: ProgramAcademicsInputs): ProgramAcademicsSnapshot {
        val grade = computeGrade(inputs)
        val multiplier = AcademicsRecruitingBudgetEngine.budgetMultiplier(grade)
        return ProgramAcademicsSnapshot(grade, multiplier)
    }
}

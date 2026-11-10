package dev.novux.dynasty.cbb.core.academics

import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityGrades
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgramAcademicsGradeEngineTest {
  @Test
    fun strongGpaAndFacility_boostsBudgetMultiplier() {
        val players = listOf(
            samplePlayer(gpa = 3.4),
            samplePlayer(gpa = 3.2),
        )
        val facilities = FacilityGrades(grades = mapOf(FacilityCategory.AcademicCenter to 9))
        val inputs = ProgramAcademicsGradeEngine.inputsFromRoster(players, facilities)
        val snap = ProgramAcademicsGradeEngine.snapshot(inputs)
        assertTrue(snap.programGrade >= 68)
        assertTrue(snap.recruitingBudgetMultiplier >= 1.0)
    }

    @Test
    fun academicHolds_lowerGradeAndMultiplier() {
        val players = listOf(
            samplePlayer(gpa = 2.0, hold = true),
            samplePlayer(gpa = 2.1),
        )
        val snap = ProgramAcademicsGradeEngine.snapshot(
            ProgramAcademicsGradeEngine.inputsFromRoster(players, FacilityGrades()),
        )
        assertTrue(snap.programGrade < 70)
        assertTrue(snap.recruitingBudgetMultiplier < 1.0)
    }

    private fun samplePlayer(gpa: Double, hold: Boolean = false) = RosterPlayer(
        playerId = "p-${gpa}",
        dynastyId = "d1",
        position = PlayerPosition.PointGuard,
        classYear = PlayerClassYear.Sophomore,
        isStarter = false,
        depthOrder = 5,
        inTransferPortal = false,
        gpaCurrent = gpa,
        academicHold = hold,
    )
}

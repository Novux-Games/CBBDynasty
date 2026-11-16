package dev.novux.dynasty.cbb.core.coach.staff

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachCard
import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachRatings
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachRatings
import org.junit.Assert.assertTrue
import org.junit.Test

class SchoolStaffGradeEngineTest {
    @Test
    fun aggregateGrade_inValidRange() {
        val roster = SchoolStaffRoster(
            headCoach = HeadCoachCard(
                dynastyId = "d1",
                displayName = "HC",
                age = 45,
                schoolDisplayName = "State",
                seasonRecordLabel = "10-5",
                archetype = CoachArchetype.Balanced,
                offensiveSchemeLabel = "Balanced",
                defensiveSchemeLabel = "Man",
                contractYearsLeft = 2,
                contractLengthYears = 4,
                annualSalaryUsd = 500_000,
                buyoutUsd = 100_000,
                ratings = HeadCoachRatings(80, 80, 82, 75),
                almaMaterLabel = null,
            ),
            assistants = listOf(
                AssistantCoachCard(
                    staffId = "a1",
                    displayName = "Asst",
                    age = 40,
                    schoolDisplayName = "State",
                    seasonRecordLabel = "10-5",
                    role = CoachTrackRole.Assistant,
                    yearsInRole = 2,
                    yearsAtSchool = 3,
                    contractYearsLeft = 2,
                    contractLengthYears = 3,
                    ratings = AssistantCoachRatings(75, 74, 72),
                ),
            ),
            supportStaff = emptyList(),
        )
        val grade = SchoolStaffGradeEngine.aggregateGrade(roster)
        assertTrue(grade in 0..99)
        assertTrue(grade >= 70)
    }
}
package dev.novux.dynasty.cbb.core.coach.contract

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachRatings
import org.junit.Assert.assertTrue
import org.junit.Test

class HeadCoachContractEngineTest {
    @Test
    fun deriveSalaryScalesWithPrestige() {
        val card = sampleCard()
        val low = HeadCoachContractEngine.derive(card, null, programPrestige = 20)
        val high = HeadCoachContractEngine.derive(card, null, programPrestige = 90)
        assertTrue(high.annualSalaryUsd > low.annualSalaryUsd)
        assertTrue(high.buyoutUsd > low.buyoutUsd)
    }

    private fun sampleCard() = HeadCoachCard(
        dynastyId = "d1",
        displayName = "Test Coach",
        age = 45,
        schoolDisplayName = "State U",
        seasonRecordLabel = "12-5",
        archetype = CoachArchetype.Balanced,
        offensiveSchemeLabel = "Balanced",
        defensiveSchemeLabel = "Man",
        contractYearsLeft = 3,
        contractLengthYears = 4,
        ratings = HeadCoachRatings(80, 80, 80, 80),
        almaMaterLabel = null,
    )
}

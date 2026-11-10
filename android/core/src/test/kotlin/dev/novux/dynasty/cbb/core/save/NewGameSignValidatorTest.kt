package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.coach.CoachCreateProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NewGameSignValidatorTest {
    @Test
    fun validate_acceptsCompleteDraft() {
        val draft = NewGameDraft(
            coach = CoachCreateProfile(firstName = "A", lastName = "B"),
            selectedTeamId = "team-conf-test-001",
            schoolSummaryAcknowledged = true,
        )
        assertNull(NewGameSignValidator.validate(draft))
    }

    @Test
    fun validate_rejectsMissingSchoolReview() {
        val draft = NewGameDraft(
            coach = CoachCreateProfile(firstName = "A", lastName = "B"),
            selectedTeamId = "team-conf-test-001",
            schoolSummaryAcknowledged = false,
        )
        assertEquals(NewGameSignValidator.Error.SchoolNotReviewed, NewGameSignValidator.validate(draft))
    }
}

package dev.novux.dynasty.cbb.core.coach

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CoachCreateValidatorTest {
    @Test
    fun displayName_includesSuffix() {
        val profile = CoachCreateProfile(
            firstName = "Marcus",
            lastName = "Hayes",
            nameSuffix = CoachNameSuffix.Jr,
        )
        assertEquals("Marcus Jr. Hayes", profile.displayName())
    }

    @Test
    fun validate_acceptsCompleteProfile() {
        val profile = CoachCreateProfile(firstName = "A", lastName = "B")
        assertNull(CoachCreateValidator.validate(profile))
    }

    @Test
    fun validate_rejectsBlankLastName() {
        assertEquals(
            CoachCreateValidationError.LastNameRequired,
            CoachCreateValidator.validate(CoachCreateProfile(firstName = "A", lastName = "")),
        )
    }
}

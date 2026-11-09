package dev.novux.dynasty.cbb.core.pack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IdentityPackValidatorTest {
    private val basePack: IdentityPack
        get() {
            val json = javaClass.classLoader
                .getResourceAsStream("pack/minimal-valid.json")!!
                .bufferedReader()
                .readText()
            return (IdentityPackCodec.parseAndValidate(json) as PackLoadResult.Success).pack
        }

    @Test
    fun valid_minimalPack() {
        assertNull(IdentityPackValidator.validate(basePack))
    }

    @Test
    fun rejects_duplicateTeamId() {
        val team = basePack.teams.first()
        val dup = basePack.copy(
            teams = basePack.teams + team.copy(displayName = "Duplicate Row"),
        )
        assertEquals(
            PackValidationError.DuplicateId("team", team.id),
            IdentityPackValidator.validate(dup),
        )
    }

    @Test
    fun rejects_emptyTeams() {
        val empty = basePack.copy(teams = emptyList())
        assertEquals(PackValidationError.EmptyCollection("teams"), IdentityPackValidator.validate(empty))
    }
}

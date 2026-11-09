package dev.novux.dynasty.cbb.core.pack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IdentityPackCodecTest {
    private val minimalValid: String by lazy {
        javaClass.classLoader
            .getResourceAsStream("pack/minimal-valid.json")!!
            .bufferedReader()
            .readText()
    }

    @Test
    fun accepts_minimalValidPack() {
        val result = IdentityPackCodec.parseAndValidate(minimalValid)
        assertTrue(result is PackLoadResult.Success)
        val pack = (result as PackLoadResult.Success).pack
        assertEquals(1, pack.packVersion)
        assertEquals("conf-sec", pack.conferences.single().id)
        assertEquals("team-001", pack.teams.single().id)
    }

    @Test
    fun rejects_malformedJson() {
        val result = IdentityPackCodec.parseAndValidate("{not json")
        assertEquals(
            true,
            (result as PackLoadResult.Rejected).error is PackValidationError.MalformedJson,
        )
    }

    @Test
    fun rejects_unsupportedPackVersion() {
        val bad = minimalValid.replace("\"packVersion\": 1", "\"packVersion\": 99")
        val result = IdentityPackCodec.parseAndValidate(bad)
        val error = (result as PackLoadResult.Rejected).error
        assertEquals(PackValidationError.UnsupportedPackVersion(99), error)
    }

    @Test
    fun rejects_unknownConference() {
        val bad = minimalValid.replace("\"conferenceId\": \"conf-sec\"", "\"conferenceId\": \"conf-missing\"")
        val error = (IdentityPackCodec.parseAndValidate(bad) as PackLoadResult.Rejected).error
        assertEquals(PackValidationError.UnknownConference("team-001", "conf-missing"), error)
    }

    @Test
    fun rejects_unknownTournamentTemplate() {
        val bad = minimalValid.replace("\"tournamentTemplateId\": \"tpl-sec-16\"", "\"tournamentTemplateId\": \"tpl-nope\"")
        val error = (IdentityPackCodec.parseAndValidate(bad) as PackLoadResult.Rejected).error
        assertEquals(PackValidationError.UnknownTournamentTemplate("conf-sec", "tpl-nope"), error)
    }

    @Test
    fun roundTrip_encode() {
        val pack = (IdentityPackCodec.parseAndValidate(minimalValid) as PackLoadResult.Success).pack
        val encoded = IdentityPackCodec.encode(pack)
        val again = IdentityPackCodec.parseAndValidate(encoded)
        assertTrue(again is PackLoadResult.Success)
    }
}

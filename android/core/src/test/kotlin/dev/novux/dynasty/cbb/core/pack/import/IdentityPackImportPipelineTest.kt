package dev.novux.dynasty.cbb.core.pack.import

import dev.novux.dynasty.cbb.core.pack.PackLoadResult
import dev.novux.dynasty.cbb.core.pack.IdentityPackCodec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IdentityPackImportPipelineTest {
    private val minimalJson: String by lazy {
        javaClass.classLoader.getResourceAsStream("pack/minimal-valid.json")!!.bufferedReader().readText()
    }

    private val minimalTxt: String by lazy {
        javaClass.classLoader.getResourceAsStream("pack/minimal-valid.txt")!!.bufferedReader().readText()
    }

    @Test
    fun json_import_requires_attestation_when_pack_demands_it() {
        val result = IdentityPackImportPipeline.importLocal(
            minimalJson,
            PackImportFormat.Json,
            PackImportAttestation(userAcceptedDisclaimer = false),
        )
        assertTrue(result is PackImportResult.Rejected)
        assertEquals(
            PackImportError.AttestationRequired::class,
            (result as PackImportResult.Rejected).error::class,
        )
    }

    @Test
    fun json_import_succeeds_with_attestation() {
        val result = IdentityPackImportPipeline.importLocal(
            minimalJson,
            PackImportFormat.Json,
            PackImportAttestation(userAcceptedDisclaimer = true),
        )
        assertTrue(result is PackImportResult.Success)
        assertEquals("team-001", (result as PackImportResult.Success).pack.teams.single().id)
    }

    @Test
    fun txt_full_import_matches_minimal_json() {
        val result = IdentityPackImportPipeline.importLocal(
            minimalTxt,
            PackImportFormat.Txt,
            PackImportAttestation(userAcceptedDisclaimer = true),
        )
        assertTrue(result is PackImportResult.Success)
        val imported = (result as PackImportResult.Success).pack
        assertEquals("txt-minimal", imported.packId)
        assertEquals("Southeast Conference", imported.conferences.single().displayName)
    }

    @Test
    fun url_policy_rejects_http() {
        val error = PackUrlPolicy.validate("http://packs.novux.dev/pack.json")
        assertTrue(error is PackImportError.UrlPolicy)
    }

    @Test
    fun url_policy_allows_https_allowlist() {
        assertEquals(null, PackUrlPolicy.validate("https://packs.novux.dev/v1/pack.json"))
    }

    @Test
    fun overlay_merge_updates_display_name() {
        val base = (IdentityPackCodec.parseAndValidate(minimalJson) as PackLoadResult.Success).pack
        val overlayTxt = """
            mode=overlay
            membershipYear=2026-27
            rulesetYear=2026
            packId=overlay-test

            [team team-001]
            conferenceId=conf-sec
            displayName=Renamed University
            shortName=RN
            homeCity=Springfield
            homeState=ST
            colors.primary=#003366
            colors.secondary=#CCCCCC
            arenaBaseName=Arena
        """.trimIndent()
        val result = IdentityPackImportPipeline.importLocal(
            overlayTxt,
            PackImportFormat.Txt,
            PackImportAttestation(userAcceptedDisclaimer = true),
            overlayBase = base,
        )
        assertTrue(result is PackImportResult.Success)
        val team = (result as PackImportResult.Success).pack.teams.single()
        assertEquals("Renamed University", team.displayName)
        assertEquals("team-001", team.id)
    }
}

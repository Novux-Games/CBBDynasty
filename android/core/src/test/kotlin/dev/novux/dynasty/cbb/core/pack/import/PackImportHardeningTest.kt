package dev.novux.dynasty.cbb.core.pack.import

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PackImportHardeningTest {
  @Test
    fun payloadPolicy_rejectsOversizedUtf8() {
        val oversized = "x".repeat((PackImportConstants.MAX_REMOTE_BYTES + 1).toInt())
        val error = PackPayloadPolicy.validateUtf8Payload(oversized)
        assertTrue(error is PackImportError.PayloadTooLarge)
    }

  @Test
    fun urlPolicy_rejectsLiteralIp() {
        assertNotNull(PackUrlPolicy.validate("https://127.0.0.1/pack.json"))
    }

  @Test
    fun urlPolicy_rejectsNonDefaultPort() {
        val error = PackUrlPolicy.validate("https://packs.novux.dev:8443/pack.json")
        assertTrue(error is PackImportError.UrlPolicy)
    }

  @Test
    fun localImport_rejectsOversizedPayload() {
        val huge = "z".repeat(6_000_001)
        val result = IdentityPackImportPipeline.importLocal(
            huge,
            PackImportFormat.Json,
            PackImportAttestation(userAcceptedDisclaimer = true),
        )
        assertTrue(result is PackImportResult.Rejected)
        assertEquals(
            PackImportError.PayloadTooLarge::class,
            (result as PackImportResult.Rejected).error::class,
        )
    }
}

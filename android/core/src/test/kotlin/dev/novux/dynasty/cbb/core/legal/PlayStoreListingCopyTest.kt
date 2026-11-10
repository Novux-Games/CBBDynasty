package dev.novux.dynasty.cbb.core.legal

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayStoreListingCopyTest {
    @Test
    fun shortDescription_withinPlayStoreLimit() {
        assertTrue(
            "short description length ${PlayStoreListingCopy.SHORT_DESCRIPTION.length}",
            PlayStoreListingCopy.SHORT_DESCRIPTION.length <= 80,
        )
    }

    @Test
    fun listingCopy_statesOfflineAndUnofficial() {
        val combined = listOf(
            PlayStoreListingCopy.SHORT_DESCRIPTION,
            PlayStoreListingCopy.FULL_DESCRIPTION,
            PlayStoreListingCopy.OFFLINE_SUMMARY,
            PlayStoreListingCopy.UNOFFICIAL_DISCLAIMER,
        ).joinToString(" ")
        assertTrue(combined.contains("offline", ignoreCase = true))
        assertTrue(combined.contains("unofficial", ignoreCase = true))
        assertTrue(PlayStoreListingCopy.UNOFFICIAL_DISCLAIMER.contains("not affiliated", ignoreCase = true))
    }

    @Test
    fun listingCopy_avoidsImpliedNcaaEndorsement() {
        val marketing = PlayStoreListingCopy.FULL_DESCRIPTION + PlayStoreListingCopy.SHORT_DESCRIPTION
        assertFalse(marketing.contains("NCAA approved", ignoreCase = true))
        assertFalse(marketing.contains("official NCAA", ignoreCase = true))
    }

    @Test
    fun privacyPolicyUrl_isHttps() {
        assertTrue(PlayStoreListingCopy.PRIVACY_POLICY_URL.startsWith("https://"))
    }
}

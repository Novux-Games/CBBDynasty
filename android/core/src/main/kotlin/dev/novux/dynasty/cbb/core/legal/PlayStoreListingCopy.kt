package dev.novux.dynasty.cbb.core.legal

import dev.novux.dynasty.cbb.core.GameConstants

/**
 * Canonical Play Store listing and legal snippets — [docs/PLAY_STORE_LISTING.md].
 */
object PlayStoreListingCopy {
    const val LISTING_TITLE: String = GameConstants.GAME_TITLE

    /** Google Play short description (max 80 characters). */
    const val SHORT_DESCRIPTION: String =
        "Build a college hoops dynasty offline. Recruit, sim seasons, chase March."

    const val PRIVACY_POLICY_URL: String = "https://novux.dev/privacy/dynasty-college-basketball"

    const val UNOFFICIAL_DISCLAIMER: String =
        "Dynasty: College Basketball is an unofficial college basketball simulation. " +
            "Novux Games is not affiliated with, endorsed by, or sponsored by the NCAA, " +
            "any conference, or any school. Team and league names in custom imports are " +
            "display overlays only; simulation data uses stable fictional IDs."

    const val OFFLINE_SUMMARY: String =
        "Core dynasty play is fully offline. Saves stay on your device. Network access is " +
            "only used when you optionally import a league identity pack from an HTTPS URL."

    val FULL_DESCRIPTION: String = """
        Run your program year-round in Dynasty: College Basketball — an unofficial college hoops sim from Novux Games.

        • Recruit, manage NIL, and fight through portal season
        • Craft schedules, climb NET and polls, survive conference tournaments
        • Chase Selection Sunday, March Mayhem, and build your legacy

        Offline-first: continue dynasties without an account or constant connection. Optional one-time pack import can rename the universe while your save IDs stay stable.

        Not affiliated with the NCAA or any real institution. Fan identity packs are optional and unofficial.
    """.trimIndent()
}

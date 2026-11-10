package dev.novux.dynasty.cbb.core.legal

import dev.novux.dynasty.cbb.core.GameConstants

/**
 * Canonical Play Store listing and legal snippets — [docs/PLAY_STORE_LISTING.md].
 */
object PlayStoreListingCopy {
    const val LISTING_TITLE: String = GameConstants.GAME_TITLE

    /** Google Play short description (max 80 characters). */
    const val SHORT_DESCRIPTION: String =
        "Run a 365-team college hoops dynasty offline. Recruit, NIL, March."

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
        Dynasty: College Basketball is a year-round head-coach sim from Novux Games—unofficial, offline-first, and built for phone play.

        YOUR PROGRAM, EVERY SEASON
        • Staff, schemes, and player development across offseason, preseason, and the calendar
        • High school and portal recruiting, NIL offers, and Signing Day closes
        • Schedule craft, weekly NET and polls, conference tournaments, and résumé building
        • Selection Sunday, Opening Round, March Mayhem, and national brackets

        FULL LEAGUE
        • A 365-team universe sims around your school while you play or fast-forward your games
        • Text-based possession sim—watch the feed or sim to the final score
        • News, awards, history, and a dashboard that keeps the job feeling alive year-round

        OFFLINE SAVES
        • Progress stays on your device; no account required
        • Optional one-time HTTPS pack import can rename the league; save IDs stay stable

        Not affiliated with the NCAA or any real school or conference. Custom identity packs are optional fan overlays only.
    """.trimIndent()
}

package dev.novux.dynasty.cbb.core.pack.import

/** URL fetch and legal copy for identity pack import — [planning/game-design.md#identity-packs--save-model]. */
object PackImportConstants {
    const val MAX_REMOTE_BYTES: Long = 5L * 1024 * 1024

    /**
     * HTTPS hosts permitted for one-shot pack fetch (ship allowlist; expand in Phase 16.3).
     */
    val ALLOWED_URL_HOSTS: Set<String> = setOf(
        "packs.novux.dev",
        "raw.githubusercontent.com",
    )

    const val UNOFFICIAL_DISCLAIMER =
        "Imported identity packs are unofficial fan content. Novux Games does not claim " +
            "affiliation with any real institution, conference, or governing body. Stable team " +
            "and conference IDs drive save data; display names come from the pack overlay only."
}

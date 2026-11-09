package dev.novux.dynasty.cbb.core.save

/** Dynasty row / export manifest version — [docs/ADR-001-dynasty-save-backend.md]. */
object DynastySchema {
    const val CURRENT_VERSION = 1

    /** Embedded league data (`assets/pack/embedded-default.json` in repo). */
    const val DEFAULT_PACK_ID = "embedded-default"

    /** Asset path inside the APK (mirror of repo `assets/pack/`). */
    const val EMBEDDED_PACK_ASSET = "pack/embedded-default.json"
}

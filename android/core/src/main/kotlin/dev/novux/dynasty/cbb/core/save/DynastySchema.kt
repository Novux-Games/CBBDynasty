package dev.novux.dynasty.cbb.core.save

/** Dynasty row / export manifest version — [docs/ADR-001-dynasty-save-backend.md]. */
object DynastySchema {
    const val CURRENT_VERSION = 1

    /** Embedded default identity pack id until pack import (Phase 3). */
    const val DEFAULT_PACK_ID = "embedded-default"
}

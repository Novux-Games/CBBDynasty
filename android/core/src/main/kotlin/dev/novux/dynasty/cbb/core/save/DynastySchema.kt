package dev.novux.dynasty.cbb.core.save

/** Dynasty row / export manifest version — [docs/ADR-001-dynasty-save-backend.md]. */
object DynastySchema {
    const val CURRENT_VERSION = 1

    /** Embedded league data (`assets/pack/embedded-default.json` in repo). */
    const val DEFAULT_PACK_ID = "embedded-default"

    /** Asset path inside the APK (mirror of repo `assets/pack/`). */
    const val EMBEDDED_PACK_ASSET = "pack/embedded-default.json"

    /** Shipped balance bundle — [docs/ADR-002-balance-ruleset-bundle.md]. */
    const val DEFAULT_BALANCE_RULESET_ID = "balance-2026"
    const val DEFAULT_BALANCE_SCHEMA_VERSION = 1
    const val EMBEDDED_BALANCE_ASSET = "balance/balance-2026.json"

    /** Shipped sim tuning bundle — [docs/ADR-003-sim-tuning-bundle.md]. */
    const val DEFAULT_SIM_TUNING_ID = "sim-tuning"
    const val DEFAULT_SIM_SCHEMA_VERSION = 1
    const val EMBEDDED_SIM_TUNING_ASSET = "sim/sim-tuning.json"
}

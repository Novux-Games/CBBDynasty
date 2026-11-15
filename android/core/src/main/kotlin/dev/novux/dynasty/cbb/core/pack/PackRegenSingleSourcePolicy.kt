package dev.novux.dynasty.cbb.core.pack

/** Default pack regenerates from `generate_default_pack.py` ([MEGA-WORLD-010]). */
object PackRegenSingleSourcePolicy {
    const val MEGA_ID: String = "MEGA-WORLD-010"

    const val GENERATOR_SCRIPT: String = "planning/tools/generate_default_pack.py"
    const val EMBEDDED_COPY: String = "android/app/src/main/assets/pack/embedded-default.json"
}
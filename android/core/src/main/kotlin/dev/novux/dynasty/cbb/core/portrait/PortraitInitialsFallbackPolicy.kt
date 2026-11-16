package dev.novux.dynasty.cbb.core.portrait

/** MEGA queue batch 36 — [MEGA-COACH-106]. */
object PortraitInitialsFallbackPolicy {
    const val MEGA_ID = "MEGA-COACH-106"
    fun useInitialsFallback(avatarPresetId: String?): Boolean = avatarPresetId.isNullOrBlank()
}

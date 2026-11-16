package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.player.appearance.PlayerAvatarPresets

/** Deterministic recruit row headshot id when prospect has no roster appearance yet ([MEGA-COACH-103]). */
object RecruitProspectPortraitPolicy {
    const val MEGA_ID: String = "MEGA-COACH-103"

    fun presetForProspect(prospectId: String): String {
        val skins = PlayerAvatarPresets.skinTones
        val hairs = PlayerAvatarPresets.hairColors
        val styles = PlayerAvatarPresets.hairStyles
        val h = prospectId.hashCode().and(0x7fffffff)
        return PlayerAvatarPresets.compositePresetId(
            skins[h % skins.size].id,
            hairs[(h / skins.size) % hairs.size].id,
            styles[(h / (skins.size * hairs.size).coerceAtLeast(1)) % styles.size].id,
        )
    }
}
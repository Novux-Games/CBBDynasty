package dev.novux.dynasty.cbb.core.coach

/**
 * Preset avatar components at coach create — pack-driven expansion later
 * ([planning/game-start.md#appearance-preset-avatars]).
 */
data class AvatarPresetOption(val id: String, val label: String)

object CoachAvatarPresets {
    val skinTones: List<AvatarPresetOption> = listOf(
        AvatarPresetOption("skin_1", "Tone 1"),
        AvatarPresetOption("skin_2", "Tone 2"),
        AvatarPresetOption("skin_3", "Tone 3"),
        AvatarPresetOption("skin_4", "Tone 4"),
        AvatarPresetOption("skin_5", "Tone 5"),
    )
    val hairColors: List<AvatarPresetOption> = listOf(
        AvatarPresetOption("hair_black", "Black"),
        AvatarPresetOption("hair_brown", "Brown"),
        AvatarPresetOption("hair_blonde", "Blonde"),
        AvatarPresetOption("hair_red", "Auburn"),
        AvatarPresetOption("hair_gray", "Gray"),
    )
    val hairStyles: List<AvatarPresetOption> = listOf(
        AvatarPresetOption("hair_short", "Short"),
        AvatarPresetOption("hair_fade", "Fade"),
        AvatarPresetOption("hair_curly", "Curly"),
        AvatarPresetOption("hair_bald", "Bald"),
    )
    val beardStyles: List<AvatarPresetOption> = listOf(
        AvatarPresetOption("beard_none", "None"),
        AvatarPresetOption("beard_stubble", "Stubble"),
        AvatarPresetOption("beard_full", "Full"),
        AvatarPresetOption("beard_goatee", "Goatee"),
    )

    fun isKnownSkin(id: String): Boolean = skinTones.any { it.id == id }
    fun isKnownHairColor(id: String): Boolean = hairColors.any { it.id == id }
    fun isKnownHairStyle(id: String): Boolean = hairStyles.any { it.id == id }
    fun isKnownBeard(id: String): Boolean = beardStyles.any { it.id == id }
}

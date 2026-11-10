package dev.novux.dynasty.cbb.core.coach

/**
 * Head coach row created in the wizard — persisted on dynasty bootstrap (step 4.6).
 * [planning/coach-card.md], [planning/game-start.md#step-1--coach-info--appearance].
 */
data class CoachCreateProfile(
    val firstName: String = "",
    val lastName: String = "",
    val nameSuffix: CoachNameSuffix = CoachNameSuffix.None,
    val age: Int = DEFAULT_AGE,
    val almaMaterTeamId: String? = null,
    val skinToneId: String = CoachAvatarPresets.skinTones.first().id,
    val hairColorId: String = CoachAvatarPresets.hairColors.first().id,
    val hairStyleId: String = CoachAvatarPresets.hairStyles.first().id,
    val beardStyleId: String = CoachAvatarPresets.beardStyles.first().id,
) {
    val avatarPresetId: String
        get() = "coach-$skinToneId-$hairColorId-$hairStyleId-$beardStyleId"

    fun displayName(): String {
        val first = firstName.trim()
        val last = lastName.trim()
        if (first.isEmpty() && last.isEmpty()) return ""
        val suffix = nameSuffix.displayToken?.let { "$it " } ?: ""
        return buildString {
            append(first)
            if (suffix.isNotEmpty()) append(' ').append(suffix.trim())
            if (last.isNotEmpty()) {
                if (isNotEmpty()) append(' ')
                append(last)
            }
        }.trim()
    }

    companion object {
        const val MIN_AGE = 28
        const val MAX_AGE = 65
        const val DEFAULT_AGE = 42
    }
}

package dev.novux.dynasty.cbb.core.coach

sealed class CoachCreateValidationError {
    data object FirstNameRequired : CoachCreateValidationError()
    data object LastNameRequired : CoachCreateValidationError()
    data class AgeOutOfRange(val age: Int) : CoachCreateValidationError()
    data object InvalidAvatar : CoachCreateValidationError()
}

object CoachCreateValidator {
    fun validate(profile: CoachCreateProfile): CoachCreateValidationError? {
        if (profile.firstName.isBlank()) return CoachCreateValidationError.FirstNameRequired
        if (profile.lastName.isBlank()) return CoachCreateValidationError.LastNameRequired
        if (profile.age !in CoachCreateProfile.MIN_AGE..CoachCreateProfile.MAX_AGE) {
            return CoachCreateValidationError.AgeOutOfRange(profile.age)
        }
        if (!CoachAvatarPresets.isKnownSkin(profile.skinToneId) ||
            !CoachAvatarPresets.isKnownHairColor(profile.hairColorId) ||
            !CoachAvatarPresets.isKnownHairStyle(profile.hairStyleId) ||
            !CoachAvatarPresets.isKnownBeard(profile.beardStyleId)
        ) {
            return CoachCreateValidationError.InvalidAvatar
        }
        return null
    }
}

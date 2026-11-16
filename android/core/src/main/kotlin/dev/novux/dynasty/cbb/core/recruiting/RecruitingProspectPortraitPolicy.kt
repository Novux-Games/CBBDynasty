package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.coach.CoachAvatarPresets
import dev.novux.dynasty.cbb.core.player.appearance.PlayerAvatarPresets

/** Shared headshot preset for recruit / portal rows ([MEGA-COACH-103]). */
object RecruitingProspectPortraitPolicy {
    const val MEGA_ID = "MEGA-COACH-103"

    fun avatarPresetId(prospectId: String): String {
        val hash = prospectId.hashCode().and(0x7fffffff)
        val skin = CoachAvatarPresets.skinTones[hash % CoachAvatarPresets.skinTones.size].id
        val hairColor = CoachAvatarPresets.hairColors[(hash / 3) % CoachAvatarPresets.hairColors.size].id
        val hairStyle = CoachAvatarPresets.hairStyles[(hash / 7) % CoachAvatarPresets.hairStyles.size].id
        return PlayerAvatarPresets.compositePresetId(skin, hairColor, hairStyle)
    }
}
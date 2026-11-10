package dev.novux.dynasty.cbb.database.dynasty

import dev.novux.dynasty.cbb.core.save.NewGameDraft

fun NewGameDraft.toCoachEntity(dynastyId: String): DynastyCoachEntity {
    val teamId = requireNotNull(selectedTeamId) { "selectedTeamId required" }
    return DynastyCoachEntity(
        dynastyId = dynastyId,
        userTeamId = teamId,
        firstName = coach.firstName.trim(),
        lastName = coach.lastName.trim(),
        nameSuffixWire = coach.nameSuffix.wireValue,
        age = coach.age,
        almaMaterTeamId = coach.almaMaterTeamId,
        skinToneId = coach.skinToneId,
        hairColorId = coach.hairColorId,
        hairStyleId = coach.hairStyleId,
        beardStyleId = coach.beardStyleId,
        avatarPresetId = coach.avatarPresetId,
        archetypeWire = archetype.wireId,
        offensiveSchemeWire = offensiveScheme.wireId,
        defensiveSchemeWire = defensiveScheme.wireId,
        autoUpgrade = dynastySettings.autoUpgrade,
        unlimitedCoachAge = dynastySettings.unlimitedCoachAge,
    )
}

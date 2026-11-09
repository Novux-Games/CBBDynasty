package dev.novux.dynasty.cbb.database.roster

import dev.novux.dynasty.cbb.core.roster.RosterPlayerSeed

fun RosterPlayerSeed.toEntity(): DynastyRosterPlayerEntity =
    DynastyRosterPlayerEntity(
        playerId = playerId,
        dynastyId = dynastyId,
        positionWire = position.wire,
        classYearWire = classYear.wire,
        isStarter = isStarter,
        depthOrder = depthOrder,
    )

fun List<RosterPlayerSeed>.toEntities(): List<DynastyRosterPlayerEntity> = map { it.toEntity() }

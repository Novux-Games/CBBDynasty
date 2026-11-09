package dev.novux.dynasty.cbb.database.dynasty

import dev.novux.dynasty.cbb.core.save.DynastyMetadata

fun DynastyMetadataEntity.toDomain(): DynastyMetadata =
    DynastyMetadata(
        dynastyId = dynastyId,
        seasonIndex = seasonIndex,
        phase = phase,
        packId = packId,
        dynastySchemaVersion = dynastySchemaVersion,
    )

fun DynastyMetadata.toEntity(): DynastyMetadataEntity =
    DynastyMetadataEntity(
        dynastyId = dynastyId,
        seasonIndex = seasonIndex,
        phase = phase,
        packId = packId,
        dynastySchemaVersion = dynastySchemaVersion,
    )

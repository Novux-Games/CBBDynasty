package dev.novux.dynasty.cbb.database.dynasty

import dev.novux.dynasty.cbb.core.save.DynastyMetadata

fun DynastyMetadataEntity.toDomain(): DynastyMetadata =
    DynastyMetadata(
        dynastyId = dynastyId,
        seasonIndex = seasonIndex,
        phase = phase,
        packId = packId,
        dynastySchemaVersion = dynastySchemaVersion,
        balanceRulesetId = balanceRulesetId,
        balanceSchemaVersion = balanceSchemaVersion,
        simTuningId = simTuningId,
        simSchemaVersion = simSchemaVersion,
    )

fun DynastyMetadata.toEntity(): DynastyMetadataEntity =
    DynastyMetadataEntity(
        dynastyId = dynastyId,
        seasonIndex = seasonIndex,
        phase = phase,
        packId = packId,
        dynastySchemaVersion = dynastySchemaVersion,
        balanceRulesetId = balanceRulesetId,
        balanceSchemaVersion = balanceSchemaVersion,
        simTuningId = simTuningId,
        simSchemaVersion = simSchemaVersion,
    )

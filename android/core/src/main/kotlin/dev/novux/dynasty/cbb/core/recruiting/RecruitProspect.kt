package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/**
 * HS/JUCO pool row — hidden 0–99 attrs stay off UI until commit
 * ([planning/recruiting-engine.md#hidden-ratings--letter-grades]).
 */
data class RecruitProspect(
    val prospectId: String,
    val displayName: String,
    val stars: Int,
    val position: PlayerPosition,
    val homeState: String,
    /** National HS rank when in Top 60 band (1 = best). */
    val nationalHsRank: Int? = null,
    val nilAskUsd: Int,
    /** Scouted letter grade shown on board — never raw attribute values. */
    val scoutLetterOverall: String,
)

data class SchoolRecruitingContext(
    val programPrestige: Int,
    val schoolHomeState: String,
    val pipelineStates: Set<String> = emptySet(),
    val nilPoolAnnualUsd: Int,
    val positionsNeed: Set<PlayerPosition> = PlayerPosition.entries.toSet(),
)

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
    /** 1–2 priorities rolled at creation; unchanged if player enters portal. */
    val topPriorities: List<RecruitPriority> = emptyList(),
    /** National HS rank when in Top 60 band (1 = best). */
    val nationalHsRank: Int? = null,
    val nilAskUsd: Int,
    /** Scouted letter grade shown on board — never raw attribute values. */
    val scoutLetterOverall: String,
    val poolKind: RecruitPoolKind = RecruitPoolKind.HighSchool,
    /** Internal composite for national order — not shown until commit. */
    val hiddenRating: Int = 0,
)

data class SchoolRecruitingContext(
    val programPrestige: Int,
    val schoolHomeState: String,
    val pipelineStates: Set<String> = emptySet(),
    val nilPoolAnnualUsd: Int,
    val positionsNeed: Set<PlayerPosition> = PlayerPosition.entries.toSet(),
)

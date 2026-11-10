package dev.novux.dynasty.cbb.core.recruiting.ai

/**
 * Hidden AI board — not shown in user recruiting hub
 * ([planning/recruiting-engine.md#visibility-player]).
 */
data class AiRecruitingBoard(
    val programId: String,
    val targetProspectIds: List<String>,
    val pointsSpentThisWeek: Int,
)

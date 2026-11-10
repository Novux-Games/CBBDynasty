package dev.novux.dynasty.cbb.core.advance

import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState

enum class SimWeekConfirmationLineKind {
    UnplayedGameFastSim,
    UserRecruitingAutoMaintain,
    LeagueRecruitingBatch,
    PortalRecruitingWeek,
    SimpleAdvance,
}

data class SimWeekConfirmationLine(
    val kind: SimWeekConfirmationLineKind,
    val formatArgs: List<Int> = emptyList(),
)

data class SimWeekConfirmation(
    val needsDialog: Boolean,
    val nextWeekLabel: String,
    val lines: List<SimWeekConfirmationLine>,
)

data class SimWeekAdvanceInput(
    val nextWeekLabel: String,
    val unplayedUserGameCount: Int,
    val recruitingPointsAvailable: Int,
    val userManualRecruitingSpent: Int,
    val recruitingHubOpen: Boolean,
    val estimatedAutoRecruitingSpend: Int,
    val portalRecruitingWeekOpen: Boolean,
    val unresolvedOptionalTaskCount: Int = 0,
)

data class SimWeekAdvanceResolution(
    val checkpointAfterPreAdvance: WeekCheckpointState,
    val fastSimmedGames: Int,
)

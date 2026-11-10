package dev.novux.dynasty.cbb.core.advance

import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState

/**
 * Advance week confirmation + resolve hook
 * ([planning/game-frame.md#advance-week-confirmation-sim-week]).
 */
object SimWeekAdvanceResolver {
    fun buildConfirmation(input: SimWeekAdvanceInput): SimWeekConfirmation {
        val lines = mutableListOf<SimWeekConfirmationLine>()
        if (input.unplayedUserGameCount > 0) {
            lines.add(SimWeekConfirmationLine(SimWeekConfirmationLineKind.UnplayedGameFastSim))
        }
        val willAutoMaintain = input.recruitingHubOpen &&
            input.userManualRecruitingSpent == 0 &&
            input.recruitingPointsAvailable > 0
        if (willAutoMaintain) {
            lines.add(
                SimWeekConfirmationLine(
                    SimWeekConfirmationLineKind.UserRecruitingAutoMaintain,
                    listOf(input.estimatedAutoRecruitingSpend.coerceAtLeast(1)),
                ),
            )
        }
        if (input.portalRecruitingWeekOpen) {
            lines.add(SimWeekConfirmationLine(SimWeekConfirmationLineKind.PortalRecruitingWeek))
        }
        if (input.recruitingHubOpen || input.portalRecruitingWeekOpen) {
            lines.add(SimWeekConfirmationLine(SimWeekConfirmationLineKind.LeagueRecruitingBatch))
        }
        if (lines.isEmpty()) {
            lines.add(
                SimWeekConfirmationLine(
                    SimWeekConfirmationLineKind.SimpleAdvance,
                    formatArgs = emptyList(),
                ),
            )
        }
        val needsDialog = input.unplayedUserGameCount > 0 ||
            willAutoMaintain ||
            input.unresolvedOptionalTaskCount > 0 ||
            input.portalRecruitingWeekOpen
        return SimWeekConfirmation(
            needsDialog = needsDialog,
            nextWeekLabel = input.nextWeekLabel,
            lines = lines,
        )
    }

    fun resolvePreAdvance(
        checkpoint: WeekCheckpointState,
        input: SimWeekAdvanceInput,
        dynastySeed: Long,
    ): SimWeekAdvanceResolution {
        val afterSim = UserFastGameSim.fastSimUnplayedGames(
            checkpoint,
            input.unplayedUserGameCount,
            dynastySeed,
        )
        return SimWeekAdvanceResolution(
            checkpointAfterPreAdvance = afterSim,
            fastSimmedGames = input.unplayedUserGameCount,
        )
    }
}

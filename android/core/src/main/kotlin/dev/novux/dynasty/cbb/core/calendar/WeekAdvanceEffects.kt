package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.calendar.bye.ByeWeekPolicy
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import dev.novux.dynasty.cbb.core.injuries.InjuryInstance
import dev.novux.dynasty.cbb.core.injuries.InjuryWeekAdvance

/**
 * Side effects when a calendar week is committed ([planning/time-system.md], [planning/injuries.md]).
 */
data class WeekAdvanceEffects(
    val checkpoint: WeekCheckpointState.AdvanceResult,
    val injuries: List<InjuryInstance>,
    val wasByeWeek: Boolean,
)

object WeekAdvanceRunner {
    fun advanceWeek(
        state: WeekCheckpointState,
        teamHasScheduledGame: Boolean,
        injuries: List<InjuryInstance>,
    ): WeekAdvanceEffects {
        val wasBye = ByeWeekPolicy.isByeWeek(state.activePosition.phase, teamHasScheduledGame)
        require(ByeWeekPolicy.canAdvanceWeek(state.activePosition.phase, teamHasScheduledGame)) {
            "week advance should always be allowed"
        }
        val checkpoint = state.advanceWeek()
        val nextInjuries = InjuryWeekAdvance.afterWeekCommit(
            phaseCommitted = checkpoint.committedWeek.phase,
            injuries = injuries,
        )
        return WeekAdvanceEffects(
            checkpoint = checkpoint,
            injuries = nextInjuries,
            wasByeWeek = wasBye,
        )
    }
}

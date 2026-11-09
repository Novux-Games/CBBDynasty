package dev.novux.dynasty.cbb.core.calendar.checkpoint

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.calendar.PhaseWeekPolicy
import dev.novux.dynasty.cbb.core.calendar.isAtOrBefore

/**
 * Dynasty week checkpoint — provisional sims for [activePosition] until [advanceWeek].
 * See [planning/game-design.md#when-games-run-vs-when-the-save-commits].
 */
data class WeekCheckpointState(
    val activePosition: CalendarPosition,
    val committedThrough: CalendarPosition? = null,
    val provisionalGames: Map<String, ProvisionalGameMark> = emptyMap(),
) {
    /** User may re-sim while the game remains in the open week bucket (replace mark). */
    fun canReplay(gameId: String): Boolean = gameId in provisionalGames

    /** True when the game is not in the current provisional bucket (e.g. after week advance). */
    fun isGameLocked(gameId: String): Boolean = !canReplay(gameId)

    fun stageSim(mark: ProvisionalGameMark): WeekCheckpointState {
        val prior = provisionalGames[mark.gameId]
        val generation = (prior?.simGeneration ?: 0) + 1
        val staged = mark.copy(simGeneration = generation)
        return copy(provisionalGames = provisionalGames + (mark.gameId to staged))
    }

    data class AdvanceResult(
        val nextState: WeekCheckpointState,
        val committedWeek: CalendarPosition,
        val gamesCommitted: Map<String, ProvisionalGameMark>,
        val phaseAfterAdvance: CalendarPhase,
    )

    /**
     * Commits the active week, clears provisional games (locked on disk in Phase 2.4+ persistence),
     * and rolls [activePosition] forward.
     */
    fun advanceWeek(): AdvanceResult {
        val committedWeek = activePosition
        val nextActive = PhaseWeekPolicy.nextPosition(activePosition)
        val nextState = copy(
            activePosition = nextActive,
            committedThrough = committedWeek,
            provisionalGames = emptyMap(),
        )
        return AdvanceResult(
            nextState = nextState,
            committedWeek = committedWeek,
            gamesCommitted = provisionalGames,
            phaseAfterAdvance = nextActive.phase,
        )
    }

    fun isWeekCommitted(position: CalendarPosition): Boolean =
        committedThrough != null && position.isAtOrBefore(committedThrough)

    companion object {
        fun bootstrap(seasonIndex: Int, phase: CalendarPhase): WeekCheckpointState =
            WeekCheckpointState(
                activePosition = PhaseWeekPolicy.bootstrapPosition(seasonIndex, phase),
            )
    }
}

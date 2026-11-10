package dev.novux.dynasty.cbb.core.advance

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.checkpoint.ProvisionalGameMark
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import kotlin.random.Random

object UserWeekGameStatus {
    fun unplayedUserGames(
        state: WeekCheckpointState,
        teamHasScheduledGame: Boolean,
    ): Int {
        if (!teamHasScheduledGame) return 0
        if (!phaseMayHaveUserGames(state.activePosition.phase)) return 0
        return if (state.provisionalGames.isEmpty()) 1 else 0
    }

    private fun phaseMayHaveUserGames(phase: CalendarPhase): Boolean =
        phase in setOf(
            CalendarPhase.Exhibition,
            CalendarPhase.RegularSeason,
            CalendarPhase.ConferenceTournament,
            CalendarPhase.Postseason,
        )
}

/** Fast sim for advance confirm — [planning/game-frame.md#advance-week-confirmation-sim-week]. */
object UserFastGameSim {
    fun fastSimUnplayedGames(
        state: WeekCheckpointState,
        unplayedCount: Int,
        dynastySeed: Long,
    ): WeekCheckpointState {
        if (unplayedCount <= 0) return state
        var next = state
        val random = Random(dynastySeed xor state.activePosition.hashCode().toLong())
        repeat(unplayedCount) { index ->
            val gameId = "user-week-${state.activePosition.seasonIndex}-" +
                "${state.activePosition.phase.name}-${state.activePosition.weekIndex}-$index"
            val home = 60 + random.nextInt(45)
            val away = 60 + random.nextInt(45)
            next = next.stageSim(ProvisionalGameMark(gameId, homeScore = home, awayScore = away))
        }
        return next
    }
}

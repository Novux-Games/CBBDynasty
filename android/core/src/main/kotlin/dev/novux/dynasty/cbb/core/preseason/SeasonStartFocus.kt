package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.schedule.PreseasonSchedulePlan
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind

data class SeasonStartFocus(
    val calendarPosition: CalendarPosition,
    val firstGame: FirstPlayableGame,
)

object PreseasonSeasonStartPolicy {
    fun resolve(
        seasonIndex: Int,
        exhibitionCount: Int,
        plan: PreseasonSchedulePlan?,
    ): SeasonStartFocus {
        val exhibitions = plan?.exhibitions.orEmpty()
        if (exhibitionCount > 0 && exhibitions.isNotEmpty()) {
            val first = exhibitions.first()
            return SeasonStartFocus(
                calendarPosition = CalendarPosition(seasonIndex, CalendarPhase.Exhibition, 1),
                firstGame = FirstPlayableGame(
                    gameId = "exhib-1-${first.opponentTeamId}",
                    opponentDisplayName = first.opponentDisplayName,
                    homeGame = first.homeGame,
                    kind = ScheduleGameKind.Exhibition,
                    calendarWeekIndex = 1,
                ),
            )
        }
        val rsGames = plan?.regularSeason.orEmpty()
        val firstRs = rsGames.minByOrNull { it.rsWeekIndex ?: Int.MAX_VALUE }
        val week = firstRs?.rsWeekIndex?.coerceAtLeast(1) ?: 1
        return SeasonStartFocus(
            calendarPosition = CalendarPosition(seasonIndex, CalendarPhase.RegularSeason, week),
            firstGame = FirstPlayableGame(
                gameId = firstRs?.let { "rs-${week}-${it.opponentTeamId}" } ?: "rs-${week}-opener",
                opponentDisplayName = firstRs?.opponentDisplayName ?: "League opponent",
                homeGame = firstRs?.homeGame ?: true,
                kind = firstRs?.kind ?: ScheduleGameKind.NonConference,
                calendarWeekIndex = week,
            ),
        )
    }
}

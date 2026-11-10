package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.core.schedule.ScheduledGameStub

/**
 * Maps simmed conference bracket games onto unified conf-tourney week indices for the user team.
 */
object ConferenceTournamentUserCalendar {
    fun userGamesByWeek(
        result: ConferenceTournamentResult,
        userTeamId: String,
        opponentDisplayName: (String) -> String,
    ): Map<Int, List<ScheduledGameStub>> {
        if (userTeamId !in result.qualifiedTeamIds) return emptyMap()
        val byWeek = mutableMapOf<Int, MutableList<ScheduledGameStub>>()
        result.gameResults.forEach { game ->
            val userHome = game.homeTeamId == userTeamId
            val userAway = game.awayTeamId == userTeamId
            if (!userHome && !userAway) return@forEach
            val opponentId = if (userHome) game.awayTeamId else game.homeTeamId
            val week = game.rsWeekIndex
            val stub = ScheduledGameStub(
                kind = ScheduleGameKind.Conference,
                opponentTeamId = opponentId,
                opponentDisplayName = opponentDisplayName(opponentId),
                homeGame = userHome,
                rsWeekIndex = week,
            )
            byWeek.getOrPut(week) { mutableListOf() } += stub
        }
        return byWeek
    }

    fun userQualified(
        result: ConferenceTournamentResult?,
        templateQualified: List<String>,
        userTeamId: String,
    ): Boolean = when {
        result != null -> userTeamId in result.qualifiedTeamIds
        else -> userTeamId in templateQualified
    }
}

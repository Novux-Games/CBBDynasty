package dev.novux.dynasty.cbb.core.preseason.tournament

import dev.novux.dynasty.cbb.core.calendar.bye.EventByeWeeks
import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import kotlin.random.Random

object EarlySeasonBracketEngine {
    fun bootstrapSnapshot(
        event: EarlySeasonEvent,
        seasonIndex: Int,
        participantIds: List<String>,
        @Suppress("UNUSED_PARAMETER") seed: Long,
    ): EarlySeasonBracketSnapshot = EarlySeasonBracketSnapshot(
        eventTemplateId = event.templateId,
        seasonIndex = seasonIndex,
        seeds = participantIds.take(event.teamCount),
    )

    fun teamWeekSlot(snapshot: EarlySeasonBracketSnapshot, teamId: String, rsWeekIndex: Int): EarlySeasonWeekSlot {
        val event = snapshot.event ?: return EarlySeasonWeekSlot.NotInEvent
        if (!snapshot.isParticipant(teamId)) return EarlySeasonWeekSlot.NotInEvent
        if (rsWeekIndex !in event.rsWeekIndices) return EarlySeasonWeekSlot.NotInEvent
        val eliminatedWeek = snapshot.eliminatedAtRsWeek[teamId]
        if (eliminatedWeek != null && rsWeekIndex > eliminatedWeek) {
            return EarlySeasonWeekSlot.EliminationBye
        }
        val pairings = pairingsForWeek(snapshot, rsWeekIndex)
        val hasGame = pairings.any { teamId == it.homeTeamId || teamId == it.awayTeamId }
        return if (hasGame) EarlySeasonWeekSlot.HasGame else EarlySeasonWeekSlot.EliminationBye
    }

    fun eliminationByeWeeks(snapshot: EarlySeasonBracketSnapshot, teamId: String): List<Int> {
        val event = snapshot.event ?: return emptyList()
        val eliminatedAt = snapshot.eliminatedAtRsWeek[teamId] ?: return emptyList()
        return EventByeWeeks.remainingByeWeeksAfterElimination(event.rsWeekIndices, eliminatedAt)
    }

    fun pairingsForWeek(snapshot: EarlySeasonBracketSnapshot, rsWeekIndex: Int): List<EarlySeasonBracketMatchup> {
        val event = snapshot.event ?: return emptyList()
        if (rsWeekIndex in snapshot.completedRsWeeks) return emptyList()
        val roundIndex = event.rsWeekIndices.indexOf(rsWeekIndex)
        if (roundIndex < 0) return emptyList()
        val raw = when (roundIndex) {
            0 -> openingRoundPairings(snapshot.seeds)
            else -> {
                val prior = snapshot.roundWinners.getOrNull(roundIndex - 1) ?: return emptyList()
                nextRoundPairings(prior)
            }
        }
        return raw.map { (home, away) ->
            EarlySeasonBracketMatchup(
                homeTeamId = home,
                awayTeamId = away,
                rsWeekIndex = rsWeekIndex,
                eventTemplateId = snapshot.eventTemplateId,
            )
        }
    }

    fun openingRoundPairings(seeds: List<String>): List<Pair<String, String>> {
        val n = seeds.size
        require(n >= 2 && n % 2 == 0) { "seeds must be even count" }
        return (0 until n / 2).map { i -> seeds[i] to seeds[n - 1 - i] }
    }

    fun nextRoundPairings(winnersInGameOrder: List<String>): List<Pair<String, String>> {
        require(winnersInGameOrder.size >= 2) { "winners required" }
        return winnersInGameOrder.chunked(2).map { chunk ->
            require(chunk.size == 2) { "odd winner count" }
            chunk[0] to chunk[1]
        }
    }

    fun advanceWeek(
        snapshot: EarlySeasonBracketSnapshot,
        rsWeekIndex: Int,
        results: List<LeagueGameResult>,
        seed: Long,
    ): EarlySeasonBracketSnapshot {
        if (rsWeekIndex in snapshot.completedRsWeeks) return snapshot
        val pairings = pairingsForWeek(snapshot, rsWeekIndex)
        if (pairings.isEmpty()) return snapshot
        val resultByKey = results.associateBy { canonicalKey(it.homeTeamId, it.awayTeamId) }
        val winners = mutableListOf<String>()
        var eliminated = snapshot.eliminatedAtRsWeek.toMutableMap()
        pairings.forEach { matchup ->
            val key = canonicalKey(matchup.homeTeamId, matchup.awayTeamId)
            val result = resultByKey[key]
                ?: simPlaceholder(matchup, seed + winners.size)
            val homeWon = result.homeScore > result.awayScore
            val winner = if (homeWon) result.homeTeamId else result.awayTeamId
            val loser = if (homeWon) result.awayTeamId else result.homeTeamId
            winners += winner
            eliminated[loser] = rsWeekIndex
        }
        val roundIndex = snapshot.event?.rsWeekIndices?.indexOf(rsWeekIndex) ?: 0
        val priorRounds = snapshot.roundWinners.toMutableList()
        while (priorRounds.size <= roundIndex) {
            priorRounds.add(emptyList())
        }
        priorRounds[roundIndex] = winners.toList()
        return snapshot.copy(
            eliminatedAtRsWeek = eliminated,
            completedRsWeeks = snapshot.completedRsWeeks + rsWeekIndex,
            roundWinners = priorRounds,
        )
    }

    fun simWeekForLeague(
        season: EarlySeasonBracketSeason,
        standings: LeagueStandingsState,
        rsWeekIndex: Int,
        userTeamId: String,
        userResults: List<LeagueGameResult>,
        seed: Long,
    ): EarlySeasonBracketWeekOutcome {
        var nextSeason = season
        var state = standings.copy(lastWeekResults = emptyList())
        val produced = mutableListOf<LeagueGameResult>()
        season.brackets.values.forEach { snapshot ->
            if (rsWeekIndex !in (snapshot.event?.rsWeekIndices.orEmpty())) return@forEach
            val pairings = pairingsForWeek(snapshot, rsWeekIndex)
            val simResults = mutableListOf<LeagueGameResult>()
            pairings.forEach { matchup ->
                val involvesUser = matchup.homeTeamId == userTeamId || matchup.awayTeamId == userTeamId
                val result = if (involvesUser) {
                    userResults.firstOrNull {
                        canonicalKey(it.homeTeamId, it.awayTeamId) ==
                            canonicalKey(matchup.homeTeamId, matchup.awayTeamId)
                    } ?: simPlaceholder(matchup, seed + simResults.size)
                } else {
                    simPlaceholder(matchup, seed + simResults.size)
                }
                simResults += result
                produced += result
                state = state.withResult(result)
            }
            val advanced = advanceWeek(snapshot, rsWeekIndex, simResults, seed)
            nextSeason = nextSeason.copy(
                brackets = nextSeason.brackets + (snapshot.eventTemplateId to advanced),
            )
        }
        return EarlySeasonBracketWeekOutcome(nextSeason, state, produced)
    }

    private fun simPlaceholder(matchup: EarlySeasonBracketMatchup, seed: Long): LeagueGameResult {
        val league = LeagueMatchup(
            homeTeamId = matchup.homeTeamId,
            awayTeamId = matchup.awayTeamId,
            rsWeekIndex = matchup.rsWeekIndex,
        )
        return LeagueFastSim.simGame(league, seed)
    }

    private fun canonicalKey(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("|")
}

data class EarlySeasonBracketWeekOutcome(
    val season: EarlySeasonBracketSeason,
    val standings: LeagueStandingsState,
    val gameResults: List<LeagueGameResult>,
)

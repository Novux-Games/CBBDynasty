package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** Incremental NCBA main-bracket sim aligned to postseason week advance. */
object NcbaMainBracketWeekEngine {
    internal fun userRegionalPairings(
        snapshot: NcbaMainBracketSnapshot,
        region: NcbaRegionId,
        roundIndex: Int,
    ): List<Pair<String, String>> = regionalPairings(snapshot, region, roundIndex)

    fun ensureBuilt(
        selection: SelectionSundaySnapshot,
        opening: NcbaOpeningRoundSnapshot,
        conferenceIdByTeam: Map<String, String>,
        existing: NcbaMainBracketSnapshot?,
    ): NcbaMainBracketSnapshot =
        existing ?: NcbaMainBracketEngine.build(selection, opening, conferenceIdByTeam)

    fun simulatePostseasonWeek(
        snapshot: NcbaMainBracketSnapshot,
        postseasonWeek: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NcbaMainBracketSnapshot {
        if (snapshot.completed) return snapshot
        if (snapshot.gameResults.any { it.rsWeekIndex == postseasonWeek }) return snapshot
        return when (postseasonWeek) {
            in 2..5 -> simulateRegionalWeek(snapshot, postseasonWeek, userTeamId, userResult, seed)
            6 -> simulateSemifinals(snapshot, postseasonWeek, userTeamId, userResult, seed)
            7 -> simulateNationalFinal(snapshot, postseasonWeek, userTeamId, userResult, seed)
            else -> snapshot
        }
    }

    private fun simulateRegionalWeek(
        snapshot: NcbaMainBracketSnapshot,
        week: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NcbaMainBracketSnapshot {
        val roundIndex = week - PostseasonCalendar.OPENING_ROUND_WEEK - 1
        val played = snapshot.gameResults.toMutableList()
        val regionalChampions = snapshot.regionalChampions.toMutableMap()
        NcbaRegionId.serpentineOrder.forEach { region ->
            val entrants = snapshot.entrants.filter { it.regionId == region }
            val pairings = regionalPairings(snapshot, region, roundIndex)
            pairings.forEachIndexed { index, (home, away) ->
                if (BracketGameMerge.alreadyPlayed(played, home, away)) return@forEachIndexed
                val game = BracketGameMerge.resolveGame(
                    home = home,
                    away = away,
                    postseasonWeek = week,
                    userTeamId = userTeamId,
                    userResult = userResult,
                    seed = seed + region.ordinal * 10_000L + index,
                )
                played += game
            }
            if (roundIndex == 3) {
                val finalGame = played.lastOrNull {
                    it.rsWeekIndex == week && (it.homeTeamId in entrants.map { e -> e.teamId } ||
                        it.awayTeamId in entrants.map { e -> e.teamId })
                }
                finalGame?.let { regionalChampions[region] = BracketGameMerge.winnerOf(it) }
            }
        }
        return snapshot.copy(gameResults = played, regionalChampions = regionalChampions)
    }

    private fun regionalPairings(
        snapshot: NcbaMainBracketSnapshot,
        region: NcbaRegionId,
        roundIndex: Int,
    ): List<Pair<String, String>> {
        val entrants = snapshot.entrants.filter { it.regionId == region }
        if (roundIndex == 0) return NcbaRegionalBracket.firstRoundPairings(entrants)
        var alive = entrants.map { it.teamId }
        repeat(roundIndex) { priorRound ->
            val week = PostseasonCalendar.OPENING_ROUND_WEEK + 1 + priorRound
            val wins = snapshot.gameResults
                .filter { it.rsWeekIndex == week }
                .filter { it.homeTeamId in alive || it.awayTeamId in alive }
                .map { BracketGameMerge.winnerOf(it) }
            alive = wins
        }
        return EarlySeasonBracketEngine.nextRoundPairings(alive)
    }

    private fun simulateSemifinals(
        snapshot: NcbaMainBracketSnapshot,
        week: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NcbaMainBracketSnapshot {
        require(snapshot.regionalChampions.size == 4) { "regional champions required before semifinals" }
        val matchups = listOf(
            snapshot.regionalChampions.getValue(NcbaRegionId.East) to
                snapshot.regionalChampions.getValue(NcbaRegionId.West),
            snapshot.regionalChampions.getValue(NcbaRegionId.South) to
                snapshot.regionalChampions.getValue(NcbaRegionId.Midwest),
        )
        val played = snapshot.gameResults.toMutableList()
        matchups.forEachIndexed { index, (home, away) ->
            if (BracketGameMerge.alreadyPlayed(played, home, away)) return@forEachIndexed
            played += BracketGameMerge.resolveGame(home, away, week, userTeamId, userResult, seed + index)
        }
        return snapshot.copy(gameResults = played)
    }

    private fun simulateNationalFinal(
        snapshot: NcbaMainBracketSnapshot,
        week: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        seed: Long,
    ): NcbaMainBracketSnapshot {
        val semiWeek = week - 1
        val semiWinners = snapshot.gameResults
            .filter { it.rsWeekIndex == semiWeek }
            .map { BracketGameMerge.winnerOf(it) }
        require(semiWinners.size == 2) { "semifinal winners required" }
        val played = snapshot.gameResults.toMutableList()
        val home = semiWinners[0]
        val away = semiWinners[1]
        if (!BracketGameMerge.alreadyPlayed(played, home, away)) {
            played += BracketGameMerge.resolveGame(home, away, week, userTeamId, userResult, seed)
        }
        val champion = played.filter { it.rsWeekIndex == week }.singleOrNull()?.let { BracketGameMerge.winnerOf(it) }
        return snapshot.copy(
            gameResults = played,
            championTeamId = champion,
            completed = champion != null,
        )
    }
}

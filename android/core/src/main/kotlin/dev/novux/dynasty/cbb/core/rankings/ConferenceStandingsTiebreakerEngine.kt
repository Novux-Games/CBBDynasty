package dev.novux.dynasty.cbb.core.rankings

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import kotlin.math.abs

/**
 * Applies pack `standingsTiebreakers` stack after W-L ([planning/game-design.md#conference-standings--tiebreakers]).
 */
object ConferenceStandingsTiebreakerEngine {
    private const val TOP_N_IN_CONFERENCE = 4

    fun sortConferenceTeams(
        conferenceId: String,
        teams: List<TeamDisplayOverlay>,
        records: Map<String, LeagueTeamRecord>,
        seasonResults: List<LeagueGameResult>,
        tiebreakers: List<String>,
    ): List<ConferenceTeamRecordRow> {
        val teamIds = teams.map { it.teamId }.toSet()
        val confGames = seasonResults.filter { game ->
            game.homeTeamId in teamIds && game.awayTeamId in teamIds
        }
        val rows = teams.map { team ->
            val rec = records[team.teamId] ?: LeagueTeamRecord(teamId = team.teamId)
            val confRec = conferenceRecord(team.teamId, confGames)
            ConferenceTeamRecordRow(
                teamId = team.teamId,
                displayName = team.displayName,
                wins = confRec.first,
                losses = confRec.second,
            )
        }
        val stack = tiebreakers.ifEmpty { DEFAULT_STACK }
        val context = SortContext(
            conferenceId = conferenceId,
            teamIds = teamIds,
            confGames = confGames,
            overallRecords = records,
            stack = stack,
        )
        return rows.sortedWith { a, b ->
            compareByRecord(a, b).takeIf { it != 0 } ?: run {
                compareTiebreakStack(a, b, context).takeIf { it != 0 } ?: a.displayName.compareTo(b.displayName)
            }
        }
    }

    private val DEFAULT_STACK = listOf("h2h", "h2h-point-diff", "record-vs-top-teams", "coin-flip")

    private fun compareByRecord(a: ConferenceTeamRecordRow, b: ConferenceTeamRecordRow): Int =
        compareByDescending<ConferenceTeamRecordRow> { it.wins }
            .thenBy { it.losses }
            .compare(a, b)

    private fun compareTiebreakStack(
        a: ConferenceTeamRecordRow,
        b: ConferenceTeamRecordRow,
        context: SortContext,
    ): Int {
        if (a.wins != b.wins || a.losses != b.losses) return 0
        for (key in context.stack) {
            val cmp = when (key.trim().lowercase()) {
                "h2h" -> compareH2H(a.teamId, b.teamId, context.confGames)
                "h2h-point-diff" -> compareH2HPointDiff(a.teamId, b.teamId, context.confGames)
                "conference-record" -> 0
                "record-vs-top-teams" -> compareRecordVsTop(a.teamId, b.teamId, context)
                "record-vs-common-opponents" -> compareCommonOpponents(a.teamId, b.teamId, context)
                "point-differential" -> compareConfPointDiff(a.teamId, b.teamId, context.confGames)
                "coin-flip" -> coinFlip(a.teamId, b.teamId, context.conferenceId)
                else -> 0
            }
            if (cmp != 0) return cmp
        }
        return 0
    }

    private fun conferenceRecord(teamId: String, confGames: List<LeagueGameResult>): Pair<Int, Int> {
        var wins = 0
        var losses = 0
        confGames.forEach { game ->
            val homeWon = game.homeScore > game.awayScore
            when (teamId) {
                game.homeTeamId -> if (homeWon) wins++ else losses++
                game.awayTeamId -> if (homeWon) losses++ else wins++
            }
        }
        return wins to losses
    }

    private fun compareH2H(a: String, b: String, games: List<LeagueGameResult>): Int =
        h2hWins(b, a, games).compareTo(h2hWins(a, b, games))

    private fun h2hWins(team: String, opponent: String, games: List<LeagueGameResult>): Int =
        games.count { match ->
            (match.homeTeamId == team && match.awayTeamId == opponent && match.homeScore > match.awayScore) ||
                (match.awayTeamId == team && match.homeTeamId == opponent && match.awayScore > match.homeScore)
        }

    private fun compareH2HPointDiff(a: String, b: String, games: List<LeagueGameResult>): Int =
        h2hPointDiff(b, a, games).compareTo(h2hPointDiff(a, b, games))

    private fun h2hPointDiff(team: String, opponent: String, games: List<LeagueGameResult>): Int =
        games.sumOf { game ->
            when {
                game.homeTeamId == team && game.awayTeamId == opponent -> game.homeScore - game.awayScore
                game.awayTeamId == team && game.homeTeamId == opponent -> game.awayScore - game.homeScore
                else -> 0
            }
        }

    private fun compareRecordVsTop(a: String, b: String, context: SortContext): Int {
        val top = topTeamsInConference(context)
        val winsA = winsAgainstSet(a, top, context.confGames)
        val winsB = winsAgainstSet(b, top, context.confGames)
        return winsB.compareTo(winsA)
    }

    private fun topTeamsInConference(context: SortContext): Set<String> {
        val ranked = context.teamIds
            .map { id ->
                val rec = conferenceRecord(id, context.confGames)
                id to (rec.first - rec.second)
            }
            .sortedByDescending { it.second }
            .map { it.first }
        return ranked.take(TOP_N_IN_CONFERENCE).toSet()
    }

    private fun winsAgainstSet(team: String, opponents: Set<String>, games: List<LeagueGameResult>): Int =
        games.count { game ->
            val opp = when (team) {
                game.homeTeamId -> game.awayTeamId
                game.awayTeamId -> game.homeTeamId
                else -> null
            }
            opp != null && opp in opponents && (
                (game.homeTeamId == team && game.homeScore > game.awayScore) ||
                    (game.awayTeamId == team && game.awayScore > game.homeScore)
                )
        }

    private fun compareCommonOpponents(a: String, b: String, context: SortContext): Int {
        val oppsA = opponentsPlayed(a, context.confGames)
        val oppsB = opponentsPlayed(b, context.confGames)
        val common = oppsA.intersect(oppsB)
        val winsA = winsAgainstSet(a, common, context.confGames)
        val winsB = winsAgainstSet(b, common, context.confGames)
        return winsB.compareTo(winsA)
    }

    private fun opponentsPlayed(team: String, games: List<LeagueGameResult>): Set<String> =
        games.mapNotNull { game ->
            when (team) {
                game.homeTeamId -> game.awayTeamId
                game.awayTeamId -> game.homeTeamId
                else -> null
            }
        }.toSet()

    private fun compareConfPointDiff(a: String, b: String, games: List<LeagueGameResult>): Int {
        val diffA = conferencePointDiff(a, games)
        val diffB = conferencePointDiff(b, games)
        return diffB.compareTo(diffA)
    }

    private fun conferencePointDiff(team: String, games: List<LeagueGameResult>): Int =
        games.sumOf { game ->
            when (team) {
                game.homeTeamId -> game.homeScore - game.awayScore
                game.awayTeamId -> game.awayScore - game.homeScore
                else -> 0
            }
        }

    private fun coinFlip(a: String, b: String, conferenceId: String): Int {
        val rollA = abs((a + conferenceId).hashCode())
        val rollB = abs((b + conferenceId).hashCode())
        return rollB.compareTo(rollA)
    }

    private data class SortContext(
        val conferenceId: String,
        val teamIds: Set<String>,
        val confGames: List<LeagueGameResult>,
        val overallRecords: Map<String, LeagueTeamRecord>,
        val stack: List<String>,
    )
}

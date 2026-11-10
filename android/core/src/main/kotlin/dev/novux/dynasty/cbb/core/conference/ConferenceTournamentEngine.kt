package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueFastSim
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueMatchup
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine

/** Simulates a conference bracket from pack [TournamentTemplatePack] — [planning/postseason.md#conference-tournaments]. */
object ConferenceTournamentEngine {
    fun simulate(input: ConferenceTournamentInput): ConferenceTournamentResult {
        var alive = ConferenceTournamentQualifier.qualify(
            input.template,
            input.teamIds,
            input.records,
        )
        require(alive.size >= 2) {
            "conference ${input.conferenceId} needs at least two qualifiers"
        }
        val seeds = alive.toList()
        val results = mutableListOf<LeagueGameResult>()
        val eliminated = mutableListOf<String>()
        var runnerUp: String? = null
        input.template.rounds.forEachIndexed { roundIndex, round ->
            if (alive.size <= 1) return@forEachIndexed
            val expectedTeams = (round.games * 2).coerceAtMost(alive.size)
            val byeTeams = alive.take((alive.size - expectedTeams).coerceAtLeast(0))
            val playing = alive.drop(byeTeams.size).take(expectedTeams)
            val pairings = if (playing.size >= 2 && playing.size % 2 == 0) {
                EarlySeasonBracketEngine.openingRoundPairings(playing)
            } else {
                emptyList()
            }
            val winners = mutableListOf<String>()
            pairings.forEachIndexed { gameIndex, (home, away) ->
                val matchup = LeagueMatchup(home, away, input.baseWeekIndex + roundIndex)
                val game = LeagueFastSim.simGame(
                    matchup,
                    input.seed + roundIndex * 31L + gameIndex,
                )
                results += game
                val homeWon = game.homeScore > game.awayScore
                val winner = if (homeWon) home else away
                val loser = if (homeWon) away else home
                winners += winner
                eliminated += loser
                if (roundIndex == input.template.rounds.lastIndex) {
                    runnerUp = loser
                }
            }
            alive = byeTeams + winners
        }
        val champion = alive.singleOrNull() ?: alive.first()
        val finishOrder = buildList {
            add(champion)
            runnerUp?.let { add(it) }
            addAll(eliminated.asReversed().filter { it != champion && it != runnerUp })
        }
        val eligible: (String) -> Boolean = { input.postseasonEligible[it] == true }
        val draft = ConferenceTournamentResult(
            conferenceId = input.conferenceId,
            templateId = input.template.id,
            qualifiedTeamIds = seeds,
            championTeamId = champion,
            runnerUpTeamId = runnerUp,
            finishOrder = finishOrder,
            ncbaAutoBidTeamId = champion,
            gameResults = results,
        )
        val autoBid = NcbaAutoBidResolver.resolve(draft, eligible)
        return draft.copy(ncbaAutoBidTeamId = autoBid)
    }

    fun simulateLeague(
        conferences: List<Pair<String, TournamentTemplatePack>>,
        records: Map<String, LeagueTeamRecord>,
        teamsByConference: Map<String, List<String>>,
        postseasonEligible: Map<String, Boolean>,
        seasonIndex: Int,
        seed: Long,
    ): ConferenceTournamentSeason {
        val results = conferences.mapNotNull { (confId, template) ->
            val teams = teamsByConference[confId].orEmpty()
            if (teams.size < 2) return@mapNotNull null
            val confRecords = teams.associateWith { records[it] ?: LeagueTeamRecord(it) }
            confId to simulate(
                ConferenceTournamentInput(
                    conferenceId = confId,
                    template = template,
                    teamIds = teams,
                    records = confRecords,
                    postseasonEligible = teams.associateWith { postseasonEligible[it] == true },
                    seasonIndex = seasonIndex,
                    seed = seed + confId.hashCode(),
                ),
            )
        }.toMap()
        return ConferenceTournamentSeason(seasonIndex = seasonIndex, resultsByConference = results)
    }
}
